package tcs.custom.ethereum.tokenWhoIsAPIs

import java.security.SecureRandom
import javax.net.ssl.{HttpsURLConnection, SSLContext}

import scalaj.http.Http
import tcs.custom.ethereum.Utils

object TokenWhoIsAPI {

  def getUsedBlockchain(tokenName: String): String = {
    Utils.getMapper.readValue[TokenWhoIsResponse](
      this.sendRequest(tokenName)
    ).blockchain
  }

  def getMarketCap(tokenName: String): Double = {
    Utils.getMapper.readValue[TokenWhoIsResponse](
      this.sendRequest(tokenName)
    ).marketcap
  }

  def getUSDUnitPrice(tokenName: String): Double = {
    Utils.getMapper.readValue[TokenWhoIsResponse](
      this.sendRequest(tokenName)
    ).usdPrice
  }

  def getETHUnitPrice(tokenName: String, tokenSymbol: String): Double = {
    Utils.getMapper.readValue[TokenWhoIsResponse](
      this.sendRequest(tokenName)
    ).market(tokenSymbol).ETH.PRICE
  }

  def getBTCUnitPrice(tokenName: String): Double = {
    Utils.getMapper.readValue[TokenWhoIsResponse](
      this.sendRequest(tokenName)
    ).btcPrice
  }

  def getExchangesNames(tokenName: String): Array[String] = {
    Utils.getMapper.readValue[TokenWhoIsResponse](
      this.sendRequest(tokenName)
    ).exchanges
  }

  private def sendRequest(tokenName: String): String = {
    try {
      val sc = SSLContext.getInstance("SSL")
      sc.init(null, Utils.trustAllCerts, new SecureRandom)
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory)
      Http(
        String.join(
          "/", "http://tokenwhois.com/api/projects", tokenName
        )
      ).asString.body
    } catch {
      case e: Exception =>
        System.out.println(e)
        ""
    }
  }
}

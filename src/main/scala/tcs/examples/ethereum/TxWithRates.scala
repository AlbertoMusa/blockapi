package tcs.examples.ethereum

import java.text.SimpleDateFormat
import java.util.Date

import tcs.blockchain.BlockchainLib
import tcs.custom.ethereum.PriceHistorical
import tcs.mongo.Collection
import tcs.db.DatabaseSettings

object TxWithRates {
  def main(args: Array[String]): Unit = {
    val blockchain = BlockchainLib.getEthereumBlockchain("http://localhost:8545").setStart(70000).setEnd(150000)
    val mongo = new DatabaseSettings("myDatabase")
    val weiIntoEth = BigInt("1000000000000000000")
    val txWithRates = new Collection("txWithRates", mongo)
    val format = new SimpleDateFormat("yyyy-MM-dd")
    val priceHistorical = PriceHistorical.getPriceHistorical()

    blockchain.foreach(block => {
      if(block.number % 1000 == 0){
        println("Current block ->" + block.number)
      }
      val date = new Date(block.timeStamp.longValue()*1000)
      val dateFormatted = format.format(date)
      block.transactions.foreach(tx => {
        val to = if(tx.to == null) "" else tx.to
        val list = List(
          ("txHash", tx.hash),
          ("blockHeight", tx.blockNumber.toString()),
          ("txIndex", tx.transactionIndex),
          ("date", date),
          ("from", tx.from),
          ("to", to),
          ("value", tx.value.doubleValue()/weiIntoEth.doubleValue()),
          ("rate", if(block.timeStamp.longValue() < 1438905600) 0 else priceHistorical.price_usd(dateFormatted))
        )
        txWithRates.append(list)
      })
    })

    txWithRates.close
  }
}

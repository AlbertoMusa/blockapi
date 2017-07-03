package tcs.blockchain

import tcs.blockchain.bitcoin.{BitcoinBlockchain, BitcoinSettings}

/**
  * Created by stefano on 08/06/17.
  */
object BlockchainLib {
  def getBitcoinBlockchain(settings: BitcoinSettings) = {
    new BitcoinBlockchain(settings)
  }
}

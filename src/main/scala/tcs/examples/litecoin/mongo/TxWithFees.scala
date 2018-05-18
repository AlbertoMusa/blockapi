package tcs.examples.litecoin.mongo

import tcs.blockchain.BlockchainLib
import tcs.blockchain.litecoin.{LitecoinSettings, MainNet}
import tcs.custom.litecoin.Exchange
import tcs.db.DatabaseSettings
import tcs.mongo.Collection
import tcs.utils.DateConverter

/**
  * Created by Giulia on 16/06/2017.
  */
object TxWithFeesLite {
  def main(args: Array[String]): Unit = {

    val blockchain = BlockchainLib.getLitecoinBlockchain(new LitecoinSettings("user", "password", "9332", MainNet, true))
    val mongo = new DatabaseSettings("myDatabase")

    val txWithFees = new Collection("txWithFees", mongo)

    blockchain.end(473100).foreach(block => {

      if (block.height % 10000 == 0) println(DateConverter.formatTimestamp(System.currentTimeMillis()) + " - Block: " + block.height)

      block.txs.foreach(tx => {
        txWithFees.append(List(
          ("blockHash", block.hash),
          ("txHash", tx.hash),
          ("date", block.date),
          ("fee", tx.getInputsSum() - tx.getOutputsSum()),
          ("rate", Exchange.getRate(block.date))
        ))
      })
    })

    txWithFees.close
  }
}

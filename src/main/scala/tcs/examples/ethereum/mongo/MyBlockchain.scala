package tcs.examples.ethereum.mongo.levensthein

import java.util.Date

import tcs.blockchain.BlockchainLib
import tcs.db.DatabaseSettings
import tcs.mongo.Collection

object MyBlockchain {
  def main(args: Array[String]): Unit = {
    val blockchain = BlockchainLib.getEthereumBlockchain("http://localhost:8545")
      .setStart(4900000).setEnd(4901000)
    val mongo = new DatabaseSettings("myDatabase")
    val weiIntoEth = BigInt("1000000000000000000")
    val myBlockchain = new Collection("myBlockchain", mongo)

    blockchain.foreach(block => {
      if(block.number % 1000 == 0){
        println("Current block ->" + block.number)
      }
      val date = new Date(block.timeStamp.longValue()*1000)
      block.transactions.foreach(tx => {
        val internalTransactions = block.internalTransactions.filter(itx => itx.parentTxHash.equals(tx.hash))
        val creates = if(tx.creates == null) "" else tx.creates
        val to = if(tx.to == null) "" else tx.to
        val list = List(
          ("txHash", tx.hash),
          ("blockHeight", tx.blockNumber.toString()),
          ("txIndex", tx.transactionIndex),
          ("date", date),
          ("from", tx.from),
          ("to", to),
          ("value", tx.value.doubleValue()/weiIntoEth.doubleValue()),
          ("creates", creates),
          ("internalTransactions", internalTransactions)
        )
        myBlockchain.append(list)
      })
    })

    myBlockchain.close
  }
}

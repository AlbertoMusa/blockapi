package tcs.examples.ethereum.mongo

import org.bson.Document
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import tcs.blockchain.BlockchainLib
import tcs.blockchain.ethereum.EthereumSettings
import tcs.db.DatabaseSettings
import tcs.mongo.Collection

object EthereumERC20Info {

  def main(args: Array[String]): Unit = {

    val blockchain = BlockchainLib.getEthereumBlockchain(new EthereumSettings("http://localhost:8545"))
    val mongo = new DatabaseSettings("EthereumTokens")
    val tokens = new Collection("EthereumTokens", mongo)

    val mongoClient: MongoClient = MongoClient("mongodb://localhost:27017/Sandbox")
    val database: MongoDatabase = mongoClient.getDatabase("EthereumTokens")
    val collection: MongoCollection[Document] = database.getCollection("EthereumTokens")

    // Iterating each block
    blockchain.start(1703600).end(2100000).foreach(block => {
      if(block.height%100 == 0){
        println("Current Block " + block.height)
      }

      if (block.internalTransactions != List.empty){

        block.internalTransactions.foreach(itx => {
            println(itx)
        })

      } else {
        //println("No internal transaction")  //testing code
      }

      block.txs.foreach(tx => {

      })
    })
    tokens.close
  }

}

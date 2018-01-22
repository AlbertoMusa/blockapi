package tcs.examples.ethereum.sql

import tcs.blockchain.BlockchainLib
import tcs.custom.ethereum.ICO
import tcs.db.{DatabaseSettings, MySQL}


object ICOInfos {
  def main(args: Array[String]): Unit = {
    val zerox = new ICO("0x")
    val ico = new ICO(zerox.getContractAddress)
    println(ico.getName)
    //val blockchain = BlockchainLib.getEthereumBlockchain("http://localhost:8545")
    //  .setStart(0).setEnd(1500000)
    //val pg = new DatabaseSettings("ethereum", MySQL, "postgres")
  }
}

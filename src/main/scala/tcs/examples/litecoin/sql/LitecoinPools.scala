package tcs.examples.litecoin.sql

import scalikejdbc._
import tcs.blockchain.BlockchainLib
import tcs.blockchain.litecoin.{LitecoinSettings, MainNet}
import tcs.db.sql.Table
import tcs.db.{DatabaseSettings, MySQL}
import tcs.utils.converter.DateConverter.convertDate


object LitecoinPools{
  def main(args: Array[String]): Unit ={

    val blockchain = BlockchainLib.getLitecoinBlockchain(new LitecoinSettings("user", "password", "9332", MainNet))
    val mySQL = new DatabaseSettings("myblockchainlite", MySQL, "root", "password")

    val txTable = new Table(sql"""
      create table if not exists ltcpools(
        blockHash varchar(256) not null,
        timestamp TIMESTAMP not null,
        pool varchar(256) not null
      ) """,
      sql"""insert into ltcpools(blockHash, timestamp, pool) values (?, ?, ?)""",
      mySQL)


    blockchain.start(800000).end(1200000).foreach(block => {
        txTable.insert(Seq(block.hash.toString(), convertDate(block.date), block.getMiningPool()))
        if(block.height%100000==0)
          println("Done working on block @ height " + block.height)
    })

    txTable.close
  }
}
package tcs.examples.ethereum.sql

import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject
import scalikejdbc._
import tcs.db.{DatabaseSettings, MySQL}
import tcs.blockchain.BlockchainLib
import tcs.blockchain.ethereum.{EthereumContract, EthereumSettings}
import tcs.db.sql.Table
import tcs.utils.HttpRequester

object DuplicatedContracts {
  def main(args: Array[String]): Unit = {

    val blockchain = BlockchainLib.getEthereumBlockchain(new EthereumSettings("http://localhost:8545", true))//connection
    val mongo = new DatabaseSettings("ethereum")//creates DB mongoDB
    val contracts = new Collection("contracts", mongo) //creates the collection

    var number = "" //number of contracts duplicated
    var addressContract ="" //temporaney address of contract duplicated

    /*
      SQL SETTINGS

      val pg = new DatabaseSettings("ethereum", MySQL, "root", "toor")
      val contractTable = new Table(
      sql"""
          CREATE TABLE IF NOT EXISTS contract(
            address CHARACTER VARYING(100) NOT NULL PRIMARY KEY,
            source_code LONGTEXT NOT NULL,
            date TIMESTAMP NOT NULL,
            name CHARACTER VARYING(100) NOT NULL
          )
         """,
      sql"""
          INSERT INTO contract(address, source_code, date, name) VALUES (?, ?, ?, ?)
         """,
      pg, 1
    )*/

    blockchain.start(49400).foreach(block => {

      if (block.height % 100 == 0) {
        println(block.height)
      }

      block.txs.foreach(tx => {

        println("Block: " + block.height + " Transaction: " + tx.hash + " Address created: " + tx.addressCreated)

        if (tx.hasContract)
        {

          try {

                val content = HttpRequester.get("http://etherscan.io/find-similiar-contracts?a=" + tx.contract.address)
                //println(content)
                if(!content.contains(" Found a total of <b>0</b> EXACT match(es)<br><br>"))
                {
                  println("il contratto ha dei duplicati")

                  // recupero il numero esatto di quante volte è stato duplicato
                  val strForNumDuplicated = " Found a total of <b>"
                  number = content.substring(content.indexOf(strForNumDuplicated)+strForNumDuplicated.length)
                  number = number.substring(0, number.indexOf("<"))
                  println(number)


                  //indirizzi dei contratti che hanno lo stesso codice sorgente

                  //memorizzo la tabella
                  var subContent = content.substring(content.indexOf("</tr>\n<tr><td>Exact [100]</td><td>"),content.indexOf("</table>"))
                  //println(subContent)
                  val strForAddress ="</span></td><td><a href='/address/"

                  do
                  {
                    subContent = subContent.substring(subContent.indexOf(strForAddress) + strForAddress.length)
                    addressContract = subContent.substring(0, 42)
                    println("###      indirizzo contratto      ###")
                    println(addressContract)

                  }while(subContent.contains(strForAddress))

                }

          } catch {
                case ioe: java.io.IOException => {ioe.printStackTrace(); return tx.contract.address}
                case ste: java.net.SocketTimeoutException => {ste.printStackTrace(); return tx.contract.address}
                case e: Exception => {e.printStackTrace(); return tx.contract.address}
          }


        }//ifHasContrac
      }) //foreach block
    })//foreach blockChain
  }//main
}//object

    /*  MONGO SETTINGS

        val list = List(
            ("contractAddress", tx.contract.address),
            ("contractName", tx.contract.name),
            ("date", block.date),
            ("sourceCode", tx.contract.sourceCode)
                            )
          //contracts.append(list)  //saves table into MongDB
        }

        })
        }

    //contracts.close
    */

    /*

    SCRIPT PARSING DI ESEMPIO

    val addressExample = "0x82EDa86610356B90e92ed23f2a5c3AA9670681A3"
    var number = ""
    var addressContract =""

    try {

      val content = HttpRequester.get("http://etherscan.io/find-similiar-contracts?a=" + addressExample)
      //println(content)
      if(!content.contains(" Found a total of <b>0</b> EXACT match(es)<br><br>"))
        {
          println("il contratto ha dei duplicati")

          // recupero il numero esatto di quante volte è stato duplicato
          val strForNumDuplicated = " Found a total of <b>"
          number = content.substring(content.indexOf(strForNumDuplicated)+strForNumDuplicated.length)
          number = number.substring(0, number.indexOf("<"))
          println(number)


          //indirizzi dei contratti che hanno lo stesso codice sorgente

          //memorizzo la tabella
          var subContent = content.substring(content.indexOf("</tr>\n<tr><td>Exact [100]</td><td>"),content.indexOf("</table>"))
          //println(subContent)
          val strForAddress ="</span></td><td><a href='/address/"

          do
          {
            subContent = subContent.substring(subContent.indexOf(strForAddress) + strForAddress.length)
            addressContract = subContent.substring(0, 42)
            println("#########  indirizzo contratto ##########")
            println(addressContract)

          }while(subContent.contains(strForAddress))

        }

    } catch {
      case ioe: java.io.IOException => {ioe.printStackTrace(); return addressExample}
      case ste: java.net.SocketTimeoutException => {ste.printStackTrace(); return addressExample}
      case e: Exception => {e.printStackTrace(); return addressExample}
    }


    */




package tcs.utils

import java.util.Date

import org.mongodb.scala.bson.Document
import tcs.blockchain.bitcoin.{BitcoinInput, BitcoinOutput}

/**
  * Created by Livio on 11/09/2017.
  */

object Convert {
  def convertPair(e: (String, Any)): Document = {
    e._2 match {

      case x: List[(String, Any)] =>  Document(e._1 -> x.map(i => Seq(convert(i))).reduce(_ ++ _))
      case x: Boolean => Document(e._1 -> x)
      case x: Int => Document(e._1 -> x)
      case x: Long => Document(e._1 -> x)
      case x: Double => Document(e._1 -> x)
      case x: Array[Byte] => Document(e._1 -> x)
      case x: Date => Document(e._1 -> x)
      case None => Document(e._1 -> None)
      case x: Seq[Any] => Document(e._1 -> x.map(i => Seq(convert(i))).reduce(_ ++ _))
      case x: Any => Document(e._1 -> x.toString)
    }
  }

  def convert(e: Any) = {
    e match {
      case x: BitcoinInput =>
        val inHash = x.redeemedTxHash
        Document("redeemedTxHash" -> (if (inHash != null) inHash.toString() else ""),
          "redeemedOutIndex" -> x.redeemedOutIndex,
          "value" -> x.value,
          "inScript" -> x.inScript.toString,
          "isCoinBase" -> x.isCoinbase)
      case x: BitcoinOutput =>
        Document("index" -> x.index,
          "value" -> x.value,
          "outScript" -> x.outScript.toString)
      case x: Any => Document("v" -> x.toString)
    }
  }


  def toAscii(hex: String) = {
    require(hex.size % 2 == 0,
      "Hex must have an even number of characters. You had " + hex.size)
    val sb = new StringBuilder
    for (i <- 0 until hex.size by 2) {
      val str = hex.substring(i, i + 2)
      sb.append(Integer.parseInt(str, 16).toChar)
    }
    sb.toString
  }
}
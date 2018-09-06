package coop.rchain.utils

import coop.rchain.crypto.codec.Base16

object HexUtil {

  def hex2bytes(hexString: String): Array[Byte] =
    Base16.decode(hexString)

  def bytes2hex(bytes: Array[Byte], sep: Option[String]): String = {
    sep match {
      case None => bytes.map("%02x".format(_)).mkString
      case _    => bytes.map("%02x".format(_)).mkString(sep.get)
    }
  }

  def bytes2hex(bytes: Array[Byte]): String =
    bytes.map("%02x".format(_)).mkString

  def chunk(buf: String): List[(String, Int)] =
    buf.grouped(1 + buf.size / 50000).zipWithIndex.toList
}

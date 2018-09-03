package coop.rchain.utils

object HexBytesUtil {

  def hex2bytes(hex: String): Array[Byte] = {
    if (hex.contains(" ")) {
      hex.split(" ").map(Integer.parseInt(_, 16).toByte)
    } else if (hex.contains("-")) {
      hex.split("-").map(Integer.parseInt(_, 16).toByte)
    } else {
      hex.sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
    }
  }

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

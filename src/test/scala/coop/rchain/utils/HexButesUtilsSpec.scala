package coop.rchain.utils

import org.specs2._

class HexButesUtilsSpec extends Specification {
    s2"""
       Hext to Bytes conversion specs
          to base 16 encoding $e1
    """
  import HexBytesUtil._

  def e1 = {
    val data = "48-65-6c-6c-6f-20-57-6f-72-6c-64-21-21"
    bytes2hex(hex2bytes(data), Option("-")) === data
  }

}

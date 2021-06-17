package utils

import scala.collection.mutable

object ShortenerService {

  val memory_dict = mutable.HashMap[String, Int]()
  val characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
  val base = characters.length

  def encode(id: Int) = {
    var muttableId = id.toDouble
    var digits = List[String]()
    while (muttableId > 0) {
      val rem = muttableId % base
      digits = digits.+:(characters.charAt(rem.toInt).toString)
      muttableId = scala.math.floor(muttableId / base)
    }
    digits.reverse.mkString("")
  }

  def resolve(code: String) = {
    val baseRep = code.map(a => characters.indexOf(a)).reverse.toList
    baseRep.zipWithIndex.map { case (element, index) => element.toInt * scala.math.pow(base, index) }.sum

  }

}

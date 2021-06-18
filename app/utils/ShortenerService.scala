package utils

import scala.annotation.tailrec
import scala.collection.mutable

object ShortenerService {
  val memory_dict = mutable.HashMap[String, Int]()
  val characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
  val base = characters.length

  def encode(id: Int) = {

    @tailrec
    def calBaseNum(num: Double, accumulator: List[String]):List[String] = {
      if (num > 0) {
        val rem = num % base
        val newAcc = accumulator.+:(characters.charAt(rem.toInt).toString)
         calBaseNum(scala.math.floor(num / base), newAcc)
      }
      else {
        accumulator
      }
    }
   calBaseNum(id, List.empty[String]).mkString("")

  }

  def resolve(code: String) = {
    val baseRep = code.map(a => characters.indexOf(a)).reverse.toList
    baseRep.zipWithIndex.map { case (element, index) => element.toInt * scala.math.pow(base, index) }.sum

  }

}

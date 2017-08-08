package akkaseed

import scala.util.Random

trait RandomString {
  val seed = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 .,?!@#$%&*^(){}[]<>|\\/"
  val size = seed.size

  def generate(n: Int): String = {
    (1 to n)
      .map(_ => seed(Random.nextInt.abs % size))
      .mkString
  }

  def generate(): String = {
    generate(Random.nextInt(10))
  }
}

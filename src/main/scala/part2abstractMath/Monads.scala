package part2abstractMath

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object Monads {

  // lists
  val numbersList: List[Int] = List(1,2,3)
  val charsList: List[Char] = List('a', 'b', 'c')

  // TODO 1.1: How do you create all combinations of (number, chars)?
  val combinationsList: List[(Int, Char)] = numbersList.flatMap(n => charsList.map(c => (n, c)))
  val combinationsListFor: List[(Int, Char)] = for {
    n <- numbersList
    c <- charsList
  } yield (n, c) // identical

  // options
  val numberOption: Option[Int] = Option(2)
  val charOption: Option[Char] = Option('d')

  // TODO 1.1: How do you create the combination of (number, chars)?
  val combinationOption: Option[(Int, Char)] = numberOption.flatMap(n => charOption.map(c => (n, c)))
  val combinationOptionFor: Option[(Int, Char)] = for {
    n <- numberOption
    c <- charOption
  } yield (n, c)

  // futures
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))

  def main(args: Array[String]): Unit = {
    println(combinationsList)
    println(combinationsListFor)
  }

}

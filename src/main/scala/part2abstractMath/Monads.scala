package part2abstractMath

import java.util.concurrent.{Executors}
import scala.concurrent.{ExecutionContext, Future}

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
  val numberFuture: Future[Int] = Future(42)
  val charFuture: Future[Char] = Future('Z')

  // TODO 1.3: How do you create a combination of (number, char)?
  val combinationFuture: Future[(Int, Char)] = numberFuture.flatMap(n => charFuture.map(c => (n, c)))
  val combinationForFuture = for {
    n <- numberFuture
    c <- charFuture
  } yield (n, c)

  /*
  Pattern
  - Wrapping a value into a monadic value
  - the flatmap mechanism

  MONADS
   */

  trait MyMonad[M[_]] {
    def pure[A](value: A): M[A]
    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]
  }

  // Cats Monad
  import cats.Monad
  import cats.instances.option._ // impliclit Monad[Option]
  val optionMonad = Monad[Option]
  val anOption = optionMonad.pure(4) // Option(4) == Some(4)
  val aTransformedOption = optionMonad.flatMap(anOption)(x => if (x % 3 == 0) Some(x + 1) else None) // None

  import cats.instances.list._
  val listMondad = Monad[List]
  val aList = listMondad.pure(3) // List(3)
  val aTransformedList = listMondad.flatMap(aList)(x => List(x, x +1)) // List(3, 4)

  // TODO 2: use a Monad[Future]
  import cats.instances.future._
  var futureMonad = Monad[Future]
  val aFuture = futureMonad.pure(43) // requires a implicit ExecutionContext
  val aTransformedFuture = futureMonad.flatMap(aFuture)(x => Future(x + 44)) // future that will end up with a Success(87)

  // Specialized API
  def getPairList(numbers: List[Int], chars: List[Char]): List[(Int, Char)] = numbers.flatMap(n => chars.map(c => (n, c)))
  def getPairOption(number: Option[Int], char: Option[Char]): Option[(Int, Char)] = number.flatMap(n => char.map(c => (n, c)))
  def getPairFuture(number: Future[Int], char: Future[Char]): Future[(Int, Char)] = number.flatMap(n => char.map(c => (n, c)))

  // generalize
  def getPairs[M[_], A, B](ma: M[A], mb: M[B])(implicit monad: Monad[M]): M[(A, B)] =
    monad.flatMap(ma)(a => monad.map(mb)(b => (a, b)))

  def main(args: Array[String]): Unit = {
    println(combinationsList)
    println(combinationsListFor)
    println(combinationFuture)
    println(getPairs(numbersList, charsList))
    println(getPairs(numberOption, charOption))
    getPairs(numberFuture, charFuture).foreach(println)
  }

}

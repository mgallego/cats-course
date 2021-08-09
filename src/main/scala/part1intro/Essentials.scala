package part1intro

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object Essentials {

  // values
  val aBoolean: Boolean = false

  // expressions are EVALUATED to a value
  val anIfExpression: String = if (2>3) "bigger" else "smaller"

  // instruction vs expression
  val theUnit: Unit = println("Hello, Scala")

  // OOP
  class Animal
  class Cat extends Animal
  trait Carnivore {
    def eat(animal: Animal): Unit
  }

  // inheritance model: extends <= 1 class, but inheritance >= 0 traits
  class Crocodile extends Animal with Carnivore {
    override def eat(animal: Animal): Unit = println("Crunch!")
  }

  // singleton
  object MySingleton // singleton pattern in one line

  // companions
  object Carnivore // Companion object of the class Carnivore

  // generics
  class MyList[A]

  // method notation
  val three: Int = 1 + 2
  val anotheThree: Int = 1.+(2)

  // functional programming
  val incrementer: Int => Int = x => x + 1
  val incremented: Int = incrementer(45) // 46

  // map, flatMap, filter
  val processedList: List[Int] = List(1, 2, 3).map(incrementer) // List(2, 3, 4)
  val aLongerList: List[Int] = List(1, 2, 3).flatMap(x => List(x, x +1)) //List(1,2 2,3 3,4)

  // for-comprehensions
  val checkerboard: List[(Int, Char)] = List(1, 2, 3).flatMap(n => List('a', 'b', 'c').map(c => (n, c)))
  val anotherCheckerboard: List[(Int, Char)] = for {
    n <- List(1, 2, 3)
    c <- List('a', 'b', 'c')
  } yield (n, c) // equivalent expressions

  // options and try
  val anOption: Option[Int] = Option(/* Something that might be null */ 3) //Some(3)
  val doubledOption: Option[Int] = anOption.map(_ * 2)
  val anAttempt: Try[Int] = Try(/* somethng that might throw */ 42) // Success(42)
  val aModifiedAttempt: Try[Int] = anAttempt.map(_ * 10) // Success(420)

  // pattern matching
  val anUnknown: Any = 45
  val ordinal: String = anUnknown match {
    case 1 => "first"
    case 2 => "second"
    case _ => "unknown"
  }
  val optionDescription: String = anOption match {
    case Some(value) => s"the option is not empty $value"
    case None => "the option is empty"
  }

  // Futures
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8))
  val aFuture: Future[Int] = Future {
    // a bit of code
    42
  }

  // wait for completion (async)
  aFuture.onComplete {
    case Success(value) => println(s"The async meaning of life is $value")
    case Failure(exception) => println(s"Meaning of value failed: $exception)")
  }

  // map a future
  val anotherFuture: Future[Int] = aFuture.map(_ + 1) // Future(43) when it completes

  // partial functions
/*
  val aPartialFunction = PartialFunction[Int, Int] = {
    case 1 => 43
    case 8 => 56
    case 100 => 999
  }
*/

  // some more advanced stuff
  trait HigherKindedType[F[_]]
  trait SequenceChecker[F[_]] {
    def ifSequential: Boolean
  }
  val listChecker = new SequenceChecker[List] {
    override def ifSequential: Boolean = true
  }

  def main(args: Array[String]): Unit = {

  }

}

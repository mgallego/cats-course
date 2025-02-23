package part2abstractMath

import scala.annotation.tailrec
import scala.util.Try

object Functors {

  val aModifiedList: List[Int] = List(1, 2, 3).map(_ + 1) // List(2, 3, 4)
  val aModifiedOption: Option[Int] = Option(2).map(_ + 1) // Option(3)
  val aModifiedTry: Try[Int] = Try(43).map(_ + 1) // Success(44)

  // simplified definition
  trait MyFunctor[F[_]] {
    def map[A, B](initialValue: F[A])(f: A => B): F[B]
  }

  // Cats Functor
  import cats.Functor
  import cats.instances.list._ // includes Functor[List]
  val listFunctor: Functor[List] = Functor[List]
  val incrementedNumbers: List[Int] = listFunctor.map(List(1,2,3))(_ + 1) // List(2, 3, 4)

  import cats.instances.option._ // includes Functor[Option]
  val optionFunctor: Functor[Option] = Functor[Option]
  val incrementedOption: Option[Int] = optionFunctor.map(Option(2))(_ + 1) // Some(3)

  import cats.instances.try_._
  val aniIncrementedTry: Try[Int] = Functor[Try].map(Try(42))(_ + 1) // Success(43)

  // generalizing an API
  def do10xList(list: List[Int]): List[Int] = list.map(_ * 10)
  def do10xOption(option: Option[Int]): Option[Int] = option.map(_ * 10)
  def do10xTry(attempt: Try[Int]): Try[Int] = attempt.map(_ * 10)

  // generalize
  def do10x[F[_]](container: F[Int])(implicit functor: Functor[F]): F[Int] = functor.map(container)(_ * 10)

  // TODO 1: define your own functor for a binary tree
  // hint: define an objects that extends Functor[Tree]
  trait Tree[+T]
  object Tree {
    // "smart" constructors
    def leaf[T](value: T): Tree[T] = Leaf(value)
    def branch[T](value: T, left: Tree[T], right: Tree[T]): Tree[T] = Branch(value, left, right)
  }
  case class Leaf[+T](value: T) extends Tree[T]
  case class Branch[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T]
  implicit object TreeFunctor extends Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = fa match {
      case Leaf(v) => Leaf(f(v))
      case Branch(v, left, right) => Branch(f(v), map(left)(f), map(right)(f))
    }
  }

  // extension method - map
  import cats.syntax.functor._
  val tree: Tree[Int] = Tree.branch(40, Tree.branch(5, Tree.leaf(10), Tree.leaf(30)), Tree.leaf(20))
  val incrementedTree: Tree[Int] = tree.map(_ + 1)

  // TODO 2: write a shorter do10x method using extension method
  def do10xShorter[F[_]: Functor](container: F[Int]): F[Int] = container.map(_ * 10)


  def main(args: Array[String]): Unit = {
    println(do10x(List(1,2,3)))
    println(do10x(Option(42)))
    println(do10x(Try(10)))
    println(do10xShorter(Tree.branch(30, Tree.leaf(10), Tree.leaf(20))))
  }
}

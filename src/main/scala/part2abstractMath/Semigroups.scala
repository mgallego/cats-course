package part2abstractMath

import cats.syntax.SemigroupOps

object Semigroups {

  // semigroups COMBINE elements of the same type
  import cats.Semigroup
  import cats.instances.int._
  val naturalIntSemigroup: Semigroup[Int] = Semigroup[Int]
  val intCombination: Int = naturalIntSemigroup.combine(2, 46) // addition
  import cats.instances.string._
  val naturalStringSemigroup: Semigroup[String] = Semigroup[String]
  val stringCombination: String = naturalStringSemigroup.combine("I love ", "Cats") // concatenation

  // specific API
  def reduceInts(list: List[Int]): Int = list.reduce(naturalIntSemigroup.combine)
  def reduceStrings(list: List[String]): String = list.reduce(naturalStringSemigroup.combine)

  // general API
  def reduceThings[T](list: List[T])(implicit semigroup: Semigroup[T]): T = list.reduce(semigroup.combine)

  // TODO 1: support a new type
  // hint: use the same pattern we used with Eq
  case class Expense(id: Long, amount: Double)
  implicit val expenseSemigroup: Semigroup[Expense] = Semigroup.instance[Expense] {
    (expense1, expense2) => Expense(Math.max(expense1.id, expense2.id), expense1.amount + expense2.amount )
  }

  // extension methods from Semigroup - |+|
  import cats.syntax.semigroup._
  val anIntSum: Int = 2 |+| 3 // requires the presence of an implicit semigroup Semigroup[Int]
  val aStringConcat: String = "we like " |+| "semigroups"
  val aCombinedExpense: Expense = Expense(4, 80) |+| Expense(2, 10)

  // TODO 2: implement reduceThings2 with |+|
  def reduceThings2[T : Semigroup](list: List[T]): T = list.reduce(_ |+| _)

  def main(args: Array[String]): Unit = {
    println(intCombination)
    println(stringCombination)

    // specific API
    val numbers = (1 to 10).toList
    println(reduceInts(numbers))

    val strings = List("I'm ", "staring  ", "to ", "like ", "semigorups")
    println(reduceStrings(strings))

    // general API
    println(reduceThings(numbers)) // the compiler injects the  implicit Semigroup[Int]
    println(reduceThings(strings)) // the compiler injects the  implicit Semigroup[String]
    import cats.instances.option._
    // compiler will produce an implicit Semigroup[Option[Int]] -- combine will produce another Option with the summed elementsa
    // compiler will produce an implicit Semigroup[Option[String]] -- combine will produce another Option with the concatenated elements
    // same for any type with an implicit Semigroup
    val numberOptions: List[Option[Int]] = numbers.map(Option(_))
    println(reduceThings(numberOptions)) // an Option[Int] containing the sum of all the numbers
    val stringOptions: List[Option[String]] = strings.map(Option(_))
    println(reduceThings(stringOptions))

    // test ex1
    val expenses: List[Expense] = List(Expense(1, 99), Expense(2, 35), Expense(43, 10))
    println(reduceThings(expenses))

    // test ex2
    println(reduceThings2(expenses))
  }

}

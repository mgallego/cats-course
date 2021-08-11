package part2abstractMath

object Monoids {


  import cats.Semigroup
  import cats.instances.int._
  import cats.syntax.semigroup._ // import the |+| extension method
  val numbers = (1 to 1000).toList
  // |+| is always associative
  val sumLeft = numbers.foldLeft(0)(_ |+| _)
  val sumRight = numbers.foldRight(0)(_ |+| _)

  // define a general API
  /*def combineFold[T](list: List[T])(implicit semigroup: Semigroup[T]): T =
    list.foldLeft(/* WHAT?! */)(_ |+| _)
*/

  // MONOIDS
  import cats.Monoid
  val intMonoid: Monoid[Int] = Monoid[Int]
  val combineInt = intMonoid.combine(23, 999) // 1022
  val zero = intMonoid.empty // 0

  import cats.instances.string._ // bring the implicit Monoid[String] in scope
  val emptyString = Monoid[String].empty // ""
  val combineString = Monoid[String].combine("I understand ", "monoids")

  import cats.instances.option._ // construct and implicit Monoid[Option[Int]]
  val emptyOption = Monoid[Option[Int]].empty // None
  val combineOption = Monoid[Option[Int]].combine(Option(2), Option.empty[Int]) // Some(2)
  val combineOption2 = Monoid[Option[Int]].combine(Option(3), Option(6)) // Some(9)

  // extension methods for Monoids - |+|
  // import cats.syntax.monoid._ // either this one or cats.syntax.semigroup._
  val combinedOptionFancy: Option[Int] = Option(3) |+| Option(7)

  // TODO 1: Implement a combineFold
  def combineFold[T : Monoid](list: List[T]): T = list.foldLeft(Monoid[T].empty)(_ |+| _)

  // TODO 2: combine a list of phonebooks as Maps[String, Int]
  // hint: don't construct a monoid - use an import
  val phonebooks = List(
    Map(
      "Alice" -> 235,
      "Bob" -> 647
    ),
    Map(
      "Charlie" -> 372,
      "Daniel" -> 889
    ),
    Map(
      "Tina" -> 123
    )
  )
  import cats.instances.map._
  val massivePhonebook: Map[String, Int] = combineFold(phonebooks)

  // TODO 3 - shopping cart and online stores with Monoids
  // hint: define your monoid. Monoid.instance
  // hint2: use combineByFold
  case class ShoppingCart(items: List[String], total: Double)

  import cats.instances.list._
  import cats.instances.double._
  import cats.syntax.semigroup._

  implicit val shoppingCartMonoid = Monoid.instance[ShoppingCart](ShoppingCart(List.empty[String], 0.0),
    (sc1, sc2) => ShoppingCart(sc1.items |+| sc2.items, sc1.total |+| sc2.total)
    /*(sc1, sc2) => ShoppingCart(sc1.items ++ sc2.items, sc1.total + sc2.total)*/ // The same but more simple
  )
  def checkout(shoppingCarts: List[ShoppingCart]): ShoppingCart = combineFold(shoppingCarts)
  val shoppingCart1 = ShoppingCart(List("item1", "item2"), 20.0)
  val shoppingCart2 = ShoppingCart(List("item3", "item4"), 40.0)
  val shoppingCarts = checkout(List(shoppingCart1, shoppingCart2))

  def main(args: Array[String]): Unit = {
    println(sumLeft)
    println(sumRight)
    println(combineFold(numbers))
    println(combineFold(List("I ", "like ", "monoids")))

    // test ex 2
    println(massivePhonebook)

    println(shoppingCarts)
  }
}

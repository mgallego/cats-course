package part1recap

object Implicits {

  // implicit classes
  case class Person(name: String) {
    def greet: String = s"Hi, my name is $name!"
  }

  implicit class ImpersonableString(name: String) { // The implicit classes always take a single argument
    def greet: String = Person(name).greet
  }

  val greeting: String = "Peter".greet // new ImpersonableString("Peter").greet

  // importing implicits conversions in scope
  import scala.concurrent.duration._
  val oneSec: FiniteDuration = 1.second

  // implicit values and arguments
  def increment(x: Int)(implicit amount: Int): Int = x + amount
  implicit val defaultAmount: Int = 10
  val incremented2: Int = increment(2)//(10)

  def multiply(x: Int)(implicit times: Int): Int = x * times
  val times2: Int = multiply(2)

  // more complex example
  trait JSONSerializer[T] {
    def toJson(value: T): String
  }

  def listToJson[T](list: List[T])(implicit serializer: JSONSerializer[T]): String =
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")

  implicit val personSerializer: JSONSerializer[Person] = new JSONSerializer[Person] {
    override def toJson(person: Person): String =
      s"""
         |{"name": "$person.name"}
         |""".stripMargin
  }
  val personJson: String = listToJson(List(Person("Alice"), Person("Bob")))
  // implicit argunent is used to PROVE THE EXISTENCE of a type

  // implicit methods
  implicit def oneArgCaseClassSerializer[T <: Product]: JSONSerializer[T] = new JSONSerializer[T] {
    override def toJson(value: T): String =
      s"""
         |{"${value.productElementName(0)}": "${value.productElement(0)}"}
         |""".stripMargin.trim
  }
  // for the compiler every case class extends from Product
  case class Cat(catName: String)
  val catsToJson: String = listToJson(List(Cat("Bob"), Cat("Garfield")))
  // in the background: val catsToJson: String = listToJson(List(Cat("Bob"), Cat("Garfield")))(oneArgCaseClassSerializer[Cat])
  // implicit methods are use to PROVE THE EXISTENCE of a type
  // can be used for implicit conversions (DISCOURAGED)

  def main(args: Array[String]): Unit = {
    println(oneArgCaseClassSerializer[Cat].toJson(Cat("Garfield")))
    println(oneArgCaseClassSerializer[Person].toJson(Person("Bob")))
  }
}

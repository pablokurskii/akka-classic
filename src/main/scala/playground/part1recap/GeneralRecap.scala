package playground.part1recap

import scala.annotation.tailrec

object GeneralRecap extends App {
  val aCondition: Boolean = false
  var aVariable = 42
  aVariable += 1 // vars are mutable

  //expressions
  val aConditionedVal = if (aCondition) 42 else 65

  //code block
  val aCodeBlock = {
    if (aCondition) 74
    56
  }

  //types
  //Unit
  val theUnit = println("Hello Scala!")

  def aFunction(x: Int) = x + 1

  //recursion - TAIL recursion
  @tailrec
  def factorial(n: Int, acc: Int): Int =
    if (n <= 0) acc
    else factorial(n - 1, acc * n)

  // OOP
  class Animal

  class Dog extends Animal

  val aDog: Animal = new Dog

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch!")
  }

  //method notations
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog //same as above

  var aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("eat")
  }
  aCarnivore eat aDog

  // generics
  abstract class MyList[+A]
  // companion objects
  object MyList

  //case classes
  case class Person(name:String, age:Int) //a lot in this course

  //exceptions
  val aPotentialFailure = try {
    throw new RuntimeException("Heyy exceptions")
  } catch {
    case e:Exception => "I caugth an exc"
  } finally {
    println("some logs")
  }

  //functional dev


}

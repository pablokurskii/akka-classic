package playground.part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import playground.part2actors.ActorCapabilities.Person.LiveTheLife

object ActorCapabilities extends App {
  class SimpleActor extends Actor {
    override def receive: Receive = {
      case "Hi!" => sender() ! s"$self replied to $sender Hello, there!" //replying to message
      case message: String => println(s"$message")
      case number: Int => println(s"[simple actor] I have received a NUMBER $number")
      case SpeciaMessage(contents) => println(s"[simple actor] I have received a SPECIAL $contents")
      case SendMessageToYourself(content) =>
        self ! content
      case SayHiTo(ref) => ref ! "Hi!"
      case WiPhoneMessage(content, ref) => ref forward (content + "s")
    }
  }

  val system = ActorSystem("actorCapabilitiesDemo")
  val simpleActor: ActorRef = system.actorOf(Props[SimpleActor], "simpleActor")

  //  simpleActor ! "hello, actor"

  // 1- messages can be of any type
  //  simpleActor ! 42

  case class SpeciaMessage(contents: String)

  //  simpleActor ! SpeciaMessage("some special content")

  case class SendMessageToYourself(content: String)

  //  simpleActor ! SendMessageToYourself("I am an actor")

  //  3- actors can reply to messages
  val alice = system.actorOf(Props[SimpleActor], "alice")
  val bob = system.actorOf(Props[SimpleActor], "bob")

  case class SayHiTo(ref: ActorRef)

  //  alice ! SayHiTo(bob)

  case class WiPhoneMessage(content: String, ref: ActorRef)

  //  alice ! WiPhoneMessage("Hi", bob)

  /**
   * Excercises
   * */
  object Counter {
    case object Increment

    case object Decrement

    case object Print
  }

  class Counter extends Actor {

    import Counter._

    var count = 0

    override def receive: Receive = {
      case Increment => count += 1
      case Decrement => count -= 1
      case Print => println(s"My current count is $count")
    }
  }

  import Counter._

  val counter = system.actorOf(Props[Counter], "myCounter")
  (1 to 5).foreach(_ => counter ! Increment)
  (1 to 3).foreach(_ => counter ! Decrement)
  counter ! Print


  object BankAccount {
    case class Deposit(amount: Int)

    case class Withdraw(amount: Int)

    case object Statement

    case class TransactionSuccess(message: String)

    case class TransactionFailure(message: String)
  }

  class BankAccount extends Actor {

    import BankAccount._

    var funds = 0

    override def receive: Receive = {
      case Deposit(amount) =>
        if (amount < 0) sender() ! TransactionFailure("Invalid deposit amount")
        else {
          funds += amount
          sender() ! TransactionSuccess(s"Successfully added $amount")
        }
      case Withdraw(amount) =>
        if (amount < 0) sender() ! TransactionFailure("Invalid withdraw amount")
        else if (amount > funds) sender() ! TransactionFailure("Insufficient funds")
        else {
          funds -= amount
          sender() ! TransactionSuccess(s"Successfully withdrew $amount")
        }
      case Statement => sender() ! s"Your balance is $funds"
    }
  }

  object Person {
    case class LiveTheLife(account: ActorRef)
  }

  class Person extends Actor {

    import BankAccount._
    import Person._

    override def receive: Receive = {
      case LiveTheLife(account) =>
        account ! Deposit(1000)
        account ! Withdraw(11000)
        account ! Withdraw(500)
        account ! Statement
      case message => println(message.toString)
    }
  }

  val account = system.actorOf(Props[BankAccount], "myBankAccount")
  val person = system.actorOf(Props[Person], "billionaire")

  person ! LiveTheLife(account)
}

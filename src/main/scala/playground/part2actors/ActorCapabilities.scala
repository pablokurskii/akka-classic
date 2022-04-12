package playground.part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

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

  alice ! SayHiTo(bob)

  case class WiPhoneMessage(content: String, ref: ActorRef)
  alice ! WiPhoneMessage("Hi", bob)
}

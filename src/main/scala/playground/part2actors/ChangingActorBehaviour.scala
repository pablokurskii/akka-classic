package playground.part2actors

import akka.actor.{Actor, ActorSystem, Props}

object ChangingActorBehaviour extends App {

  val system = ActorSystem("actorCapabilitiesDemo")


  /*recreate with context.become and NO MUTABLE STATE*/
  object Counter {
    case object Increment

    case object Decrement

    case object Print
  }

  class Counter extends Actor {

    import Counter._

    override def receive: Receive = countReceive(0)

    def countReceive(currentCount: Int): Receive = {
      case Increment => context.become(countReceive(currentCount + 1))
      case Decrement => context.become(countReceive(currentCount - 1))
      case Print => println(currentCount)
    }
  }

  import Counter._

  val counter = system.actorOf(Props[Counter], "myCounter")
  (1 to 5).foreach(_ => counter ! Increment)
  (1 to 3).foreach(_ => counter ! Decrement)
  counter ! Print
}

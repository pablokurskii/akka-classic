package playground

import akka.actor.ActorSystem

object Playground extends App {

  val system = ActorSystem("Playground")
  println(system.name)
}
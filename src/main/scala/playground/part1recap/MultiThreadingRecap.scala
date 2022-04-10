package playground.part1recap

object MultiThreadingRecap extends App {
  /*val aThread1 = new Thread(new Runnable {
    override def run(): Unit = println("I am running in parallel")
  })*/

  val aThread = new Thread(() => println("I am running in parallel"))
  aThread.start()
  aThread.join() //wait for a main thread to finish this thread

  val threadHello = new Thread(() => (1 to 1000).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 1000).foreach(_ => println("goodbye")))
  threadHello.start()
  threadGoodbye.start()

  //different runs produce different results

  /*Old way with volatile or this.synchronized*/
  class BankAccount(@volatile private var amount: Int) {
    override def toString: String = "" + amount

    def withdraw(money: Int): Unit = this.amount -= money

    def safeWithdraw(money: Int): Unit = this.synchronized {
      this.amount -= money
    }
  }
}


package playground.part1recap

object ThreadModelLimitations extends App {
  /*#1*/
  class BankAccount(private var amount: Int) {
    override def toString: String = "" + amount

    def withdraw(money: Int): Unit = this.amount -= money

    def deposit(money: Int): Unit = this.amount += money

    def getAmount: Int = amount;
  }

//  val account = new BankAccount(2000)
//  for (_ <- 1 to 1000) {
//    new Thread(() => account.withdraw(1)).start()
//  }
//  for (_ <- 1 to 1000) {
//    new Thread(() => account.deposit(1)).start()
//  }
//  println(account.getAmount)

  /*#2*/
  //  Task - how to send a task to the running thread

  var task: Runnable = null

  val runningThread: Thread = new Thread(() => {
    while (true) {
      while (task == null) {
        runningThread.synchronized {
          println("[background] waiting for a task...")
          runningThread.wait()
        }
      }

      task.synchronized {
        println("[background] I have a task!")
        task.run()
        task = null
      }
    }
  })

  def delegateTaskToBackgroundThread(r: Runnable): Unit = {
    if (task == null) task = r
    runningThread.synchronized {
      runningThread.notify()
    }
  }

  runningThread.start()
  Thread.sleep(1000)
  delegateTaskToBackgroundThread(() => println(42))
  Thread.sleep(1000)
  delegateTaskToBackgroundThread(() => println("this should run in the background"))

  /*#3*/
}

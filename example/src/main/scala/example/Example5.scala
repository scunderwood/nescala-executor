package example

import play.Execution

import scala.concurrent.Future


object TrampolineContext extends App {


  def sleepInTheFuture(): Future[String] = {
    Future {
        val threadName = Thread.currentThread.getName
        println(s"Thread: $threadName Sleeping")
        Thread.sleep(2000)
        threadName
    } (Execution.trampoline)
  }

  for (_ <- 1 to 50) {
    sleepInTheFuture().map({ threadName =>
      println(s"${Thread.currentThread.getName} Got Callback after $threadName woke up")
    })(Execution.trampoline)
  }

}

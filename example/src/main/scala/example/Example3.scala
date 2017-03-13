package example

import java.util.concurrent.ForkJoinPool

import debug.{DebugExecutor, ForkJoinMonitor}

import scala.concurrent._

object BlockingFutures extends App {

  val debugExecutorService = DebugExecutor.createExecutorService(Some(1))
  val global = ExecutionContext.fromExecutorService(debugExecutorService)

  val forkJoinMonitor = new ForkJoinMonitor(debugExecutorService.asInstanceOf[ForkJoinPool])
  new Thread(forkJoinMonitor).start()

  def sleepInTheFuture(): Future[String] = {
    Future {
        val threadName = Thread.currentThread.getName
        println(s"Thread: $threadName Sleeping")
        blocking {
          Thread.sleep(2000)
        }
        threadName
    } (global)
  }

  for (i <- 1 to 50) {
    sleepInTheFuture().map({ threadName =>
      println(s"${Thread.currentThread.getName} Got Callback after $threadName woke up")
      if (i == 50) {
        println("Turning off Monitor")
        forkJoinMonitor.finish()
      }
    })(global)
  }

}

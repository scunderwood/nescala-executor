package example

import java.util.concurrent.{Executors, ThreadPoolExecutor}

import debug.ThreadPoolExecutorMonitor

import scala.concurrent._

object FixedThreadPool extends App {

  val fixedThreadPool = Executors.newFixedThreadPool(1)
  val fixedThreadPoolEC = ExecutionContext.fromExecutor(fixedThreadPool)

  val fixedThreadPoolMonitor = new ThreadPoolExecutorMonitor(fixedThreadPool.asInstanceOf[ThreadPoolExecutor])
  new Thread(fixedThreadPoolMonitor).start()

  def sleepInTheFuture(): Future[String] = {
    Future {
        val threadName = Thread.currentThread.getName
        println(s"Thread: $threadName Sleeping")
        blocking {
          Thread.sleep(2000)
        }
        threadName
    } (fixedThreadPoolEC)
  }

  for (i <- 1 to 50) {
    sleepInTheFuture().map({ threadName =>
      println(s"${Thread.currentThread.getName} Got Callback after $threadName woke up")
      if (i == 50) {
        println("Turning off Monitor")
        fixedThreadPoolMonitor.finish()
      }
    })(fixedThreadPoolEC)
  }

}

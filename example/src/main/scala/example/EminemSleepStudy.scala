package example

import java.util.concurrent.{Executors, ForkJoinPool, ThreadPoolExecutor}

import debug.concurrent._
import debug.concurrent.impl.ExecutionContextImpl
import monitor.{ForkJoinMonitor, ThreadPoolExecutorMonitor}


object EminemSleepStudy extends App {

  val fjExecutorService = ExecutionContextImpl.createDefaultExecutorService(ExecutionContext.defaultReporter)
  val monitor = new ForkJoinMonitor(fjExecutorService.asInstanceOf[ForkJoinPool])
  implicit val ec = ExecutionContext.fromExecutorService(fjExecutorService)
  new Thread(monitor).start()

//  val fixedThreadPool = Executors.newFixedThreadPool(1)
//  val fixedThreadPoolEC = ExecutionContext.fromExecutor(fixedThreadPool)
//
//  val fixedThreadPoolMonitor = new ThreadPoolExecutorMonitor(fixedThreadPool.asInstanceOf[ThreadPoolExecutor])
//  new Thread(fixedThreadPoolMonitor).start()

  def slimShady(): Future[String] = {
    Future {
      val threadName = Thread.currentThread.getName
      log("Hi! My name is (what?)")
      Thread.sleep(2000)
      threadName
    }
  }

  for (i <- 1 to 20) yield {
    slimShady().map({ name =>
      log(s"My name is $name")
      if (i == 20) monitor.finish()
    })
  }

}

package example

import java.util.concurrent.{Executors, ForkJoinPool, ThreadPoolExecutor}

import debug.concurrent._
import debug.concurrent.impl.ExecutionContextImpl
import monitor.{ForkJoinMonitor, ThreadPoolExecutorMonitor}

import scala.util.control.NonFatal


object GlobalECDebug extends App {

  val fjExecutorService = ExecutionContextImpl.createDefaultExecutorService(ExecutionContext.defaultReporter)
  val monitor = new ForkJoinMonitor(fjExecutorService.asInstanceOf[ForkJoinPool])
  implicit val ec = ExecutionContext.fromExecutorService(fjExecutorService)
  new Thread(monitor).start()

//  val fixedThreadPool = Executors.newFixedThreadPool(1)
//  val fixedThreadPoolEC = ExecutionContext.fromExecutor(fixedThreadPool)
//
//  val fixedThreadPoolMonitor = new ThreadPoolExecutorMonitor(fixedThreadPool.asInstanceOf[ThreadPoolExecutor])
//  new Thread(fixedThreadPoolMonitor).start()


  def doAsyncBusyWork(id: Int): Future[String] = {
    Future {
      //blocking {
        sleepAndEcho(id)
      //}
    }
  }

  for (i <- 1 to 33000) yield {
    try {
      doAsyncBusyWork(i).map({ name =>
        log(s"$name Finished Work for $i")
        if (i == 33000) monitor.finish()
      })
    } catch {
      case NonFatal(t) => t.printStackTrace
    }
  }

}

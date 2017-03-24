package example


import java.util.concurrent.ForkJoinPool

import debug.concurrent._
import debug.concurrent.impl.ExecutionContextImpl
import monitor.{ForkJoinMonitor}

object E extends App {

  val fjExecutorService = ExecutionContextImpl.createDefaultExecutorService(ExecutionContext.defaultReporter)
  val monitor = new ForkJoinMonitor(fjExecutorService.asInstanceOf[ForkJoinPool])
  implicit val ec = ExecutionContext.fromExecutorService(fjExecutorService)
  new Thread(monitor).start()

  def doAsyncBusyWork(id: Int): Future[String] = {
    Future {
      busyWork(id, fibNumber = 1000, shouldLog = false)
    }
  }

  for (i <- 1 to 3000) yield {
    doAsyncBusyWork(i).map({ name =>
      //log(s"$name Finished Work for $i")
      if (i == 3000) monitor.finish()
    })
  }

}

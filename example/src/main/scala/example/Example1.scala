package example

import java.util.concurrent.ForkJoinPool

import debug.{DebugExecutor, ForkJoinMonitor}

import scala.concurrent.duration.Duration
import scala.concurrent._

object GlobalEC extends App {

  val debugExecutorService = DebugExecutor.createExecutorService(Some(1))
  val global = ExecutionContext.fromExecutorService(debugExecutorService)

  val forkJoinMonitor = new ForkJoinMonitor(debugExecutorService.asInstanceOf[ForkJoinPool])
  new Thread(forkJoinMonitor).start()

  def helloWorldInTheFuture = Future {
    println(s"${Thread.currentThread.getName} Hello World")
  } (global)


  Await.ready(helloWorldInTheFuture, Duration.Inf)

  //Turn off Monitor
  forkJoinMonitor.finish()
}





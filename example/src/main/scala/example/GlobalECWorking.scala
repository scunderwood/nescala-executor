package example

import java.util.concurrent.Executors

import debug.concurrent.{Await, ExecutionContext, Future}
import debug.concurrent.duration.Duration

object GlobalECWorking extends App {

  val startTime = System.currentTimeMillis

  //implicit val ec = ExecutionContext.global
  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))

  def doAsyncWork(id: Int): Future[String] = {
    Future {
      busyWork(id)
    }
  }

  val echoes = for (i <- 1 to 50) yield {
    doAsyncWork(i).map({ name =>
      log(s"$name Finished Work for $i")
    })
  }

  Await.ready(Future.sequence(echoes), Duration.Inf)
  log(s"Ran in ${System.currentTimeMillis - startTime}ms")
}

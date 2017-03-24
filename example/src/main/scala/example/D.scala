package example

import debug.concurrent._
import debug.concurrent.duration.Duration

object D extends App {

  val startTime = System.currentTimeMillis

  implicit val ec = ExecutionContext.global

  def doAsyncWork(id: Int): Future[String] = {
    Future {
      blocking {
        busyWork(id)
      }
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

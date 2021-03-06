package example

import debug.concurrent._
import debug.concurrent.duration.Duration

object B extends App {

  val startTime = System.currentTimeMillis

  implicit val ec = ExecutionContext.global

  def doAsyncWork(id: Int): Future[String] = {
    Future {
      blocking {
        sleepAndEcho(id)
      }
    }
  }

  val echoes = for (i <- 1 to 16) yield {
    doAsyncWork(i).map({ name =>
      log(s"$name Finished Sleep for $i")
    })
  }

  Await.ready(Future.sequence(echoes), Duration.Inf)
  log(s"Ran in ${System.currentTimeMillis - startTime}ms")
}


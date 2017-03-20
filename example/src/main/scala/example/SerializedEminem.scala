package example

import akka.SerializedSuspendableExecutionContext
import debug.concurrent.duration.Duration
import debug.concurrent.{Await, ExecutionContext, Future}

object SerializedEminem extends App {

  implicit val ec = new SerializedSuspendableExecutionContext(1)(ExecutionContext.global)

  def slimShady(): Future[String] = {
    Future {
      val threadName = Thread.currentThread.getName
      log("Hi! My name is (what?)")
      Thread.sleep(2000)
      threadName
    }
  }

  val echoes = for (i <- 1 to 20) yield {
    slimShady().map({ name =>
      log(s"My name is $name")
      if (i == 10) {
        log("Taking a Break for 5 seconds")
        ec.suspend()
        Thread.sleep(5000)
        log("Resuming")
        ec.resume()
      }
    })
  }

  Await.ready(Future.sequence(echoes), Duration.Inf)
}

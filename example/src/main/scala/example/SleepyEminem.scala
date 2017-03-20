package example

import debug.concurrent._
import debug.concurrent.duration.Duration

object SleepyEminem extends App {

  implicit val ec = ExecutionContext.global

  def slimShady(): Future[String] = {
    Future {
      val threadName = Thread.currentThread.getName
      log("Hi!, My name is (what?)")
      Thread.sleep(2000)
      threadName
    }
  }

  val echoes = for (_ <- 1 to 20) yield {
    slimShady().map({ name =>
      log(s"My name is $name")
    })
  }

  Await.ready(Future.sequence(echoes), Duration.Inf)
}

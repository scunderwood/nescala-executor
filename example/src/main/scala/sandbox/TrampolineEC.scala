package sandbox

import debug.concurrent.duration.Duration
import debug.concurrent.{Await, Future}
import example.log
import play.Execution

object TrampolineEC {//extends App {

  implicit val ec = Execution.trampoline

  def sleepAndEcho(): Future[String] = {
    Future {
      val threadName = Thread.currentThread.getName
      log("Hi! My name is (what?)")
      Thread.sleep(2000)
      threadName
    }
  }

  val echoes = for (_ <- 1 to 16) yield {
    sleepAndEcho().map({ name =>
      log(s"My name is $name")
    })
  }

  Await.ready(Future.sequence(echoes), Duration.Inf)
}

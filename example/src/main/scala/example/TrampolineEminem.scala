package example

import debug.concurrent.Future
import play.Execution

object TrampolineEminem extends App {

  implicit val ec = Execution.trampoline

  def slimShady(): Future[String] = {
    Future {
      val threadName = Thread.currentThread.getName
      log("Hi! My name is (what?)")
      Thread.sleep(2000)
      threadName
    }
  }

  for (_ <- 1 to 20) yield {
    slimShady().map({ name =>
      log(s"My name is $name")
    })
  }
}

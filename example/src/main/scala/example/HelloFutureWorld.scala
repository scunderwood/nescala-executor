package example

import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object HelloFutureWorld extends App {
  def helloWorldInTheFuture = Future {
    println(s"${Thread.currentThread.getName} hello future world!")
  }
  println(s"${Thread.currentThread.getName} hello present world!")
  Await.ready(helloWorldInTheFuture, Duration.Inf)
}






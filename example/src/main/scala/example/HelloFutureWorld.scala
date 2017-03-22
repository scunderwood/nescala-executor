package example

import debug.concurrent.duration.Duration
import debug.concurrent.{Await, ExecutionContext, Future}

import debug.concurrent.ExecutionContext.Implicits.global

object HelloFutureWorld extends App {
  def helloWorldInTheFuture = Future {
    println(s"${Thread.currentThread.getName} hello future world!")
  }
  println(s"${Thread.currentThread.getName} hello present world!")
  Await.ready(helloWorldInTheFuture, Duration.Inf)
}






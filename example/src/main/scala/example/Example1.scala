package example

import java.util.concurrent.{Executors, ThreadPoolExecutor}

import debug.concurrent.duration.Duration
import debug.concurrent._

object Example1 extends App {
  val modelObject = 1
  type ModelObject = Int

  import debug.concurrent.ExecutionContext.Implicits.global

  val adaptedEC = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(8))

  def sendToApiAsync(model: ModelObject) = Future {
    blocking {
      sendToApi(modelObject)
    }
  }


  def writeToFileAsync(model: ModelObject) = Future {
    writeToFile(modelObject)
  }

  log("Calling Send To API")
  sendToApi(modelObject)
  log("Calling Write To File")
  writeToFile(modelObject)
  log("Continuing")

  //Await.ready(Future.sequence(List(writeFuture, sendFuture)), Duration.Inf)
}






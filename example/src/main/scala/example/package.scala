import java.util.Date

import example.Level.{INTERNAL, PRESENTATION}

package object example {

  sealed trait Level

  object Level {
    case object INTERNAL extends Level
    case object PRESENTATION extends Level
  }

  def log(msg: String, level: Level = PRESENTATION): Unit = level match {
    case PRESENTATION =>
      val dateTime = new Date(System.currentTimeMillis())
      println(s"$dateTime ${Thread.currentThread.getName} $msg")
    case INTERNAL => //println(s"${Thread.currentThread.getName} $msg")
  }

  def busyWork(id: Int): String = {
    val start = System.currentTimeMillis
    log("Computing")
    fibonacci(1000000000)
    log(s"Fib Done in ${System.currentTimeMillis - start}ms")
    Thread.currentThread.getName
  }

  def sleepAndEcho(id: Int): String = {
    log("Sleeping for 2000ms")
    Thread.sleep(2000)
    Thread.currentThread.getName
  }

  def sendToApi(id: Int): Unit = {
    Thread.sleep(1000)
    log(s"Sent to API")
  }

  def writeToFile(id: Int): Unit = {
    log(s"Wrote to File")
  }

  def fibonacci(n : Int) : Int = {
    def fibTail(n: Int, a: Int, b: Int): Int = n match {
      case 0 => a
      case _ => fibTail(n-1, b, a + b)
    }
    fibTail(n, 0, 1)
  }


}

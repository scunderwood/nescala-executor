
import example.Level.{INTERNAL, PRESENTATION}

package object example {

  sealed trait Level

  object Level {
    case object INTERNAL extends Level
    case object PRESENTATION extends Level
  }

  def log(msg: String, level: Level = PRESENTATION): Unit = level match {
    case PRESENTATION =>
      println(s"${Thread.currentThread.getName} $msg")
    case INTERNAL => //println(s"${Thread.currentThread.getName} $msg")
  }

  def busyWork(id: Int, fibNumber: Int = 1000000000, shouldLog: Boolean = true): String = {
    val start = System.currentTimeMillis
    if (shouldLog) log("Computing")
    fibonacci(fibNumber)
    if (shouldLog) log(s"Fib Done in ${System.currentTimeMillis - start}ms")
    Thread.currentThread.getName
  }

  def sleepAndEcho(id: Int): String = {
    log("Sleeping for 2000ms")
    Thread.sleep(2000)
    Thread.currentThread.getName
  }


  def fibonacci(n : Int) : Int = {
    def fibTail(n: Int, a: Int, b: Int): Int = n match {
      case 0 => a
      case _ => fibTail(n-1, b, a + b)
    }
    fibTail(n, 0, 1)
  }


}

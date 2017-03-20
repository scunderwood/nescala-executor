import example.Level.{INTERNAL, PRESENTATION}

package object example {

  sealed trait Level

  object Level {
    case object INTERNAL extends Level
    case object PRESENTATION extends Level
  }


  def log(msg: String, level: Level = PRESENTATION): Unit = level match {
    case PRESENTATION => println(s"${Thread.currentThread.getName} $msg")
    case INTERNAL => //println(s"${Thread.currentThread.getName} $msg")
  }

}

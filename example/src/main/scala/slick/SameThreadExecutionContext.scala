package slick

import debug.concurrent.ExecutionContext


object SameThreadExecutionContext extends ExecutionContext {
  private[this] val trampoline = new ThreadLocal[List[Runnable]]

  private[this] def runTrampoline(first: Runnable): Unit = {
    trampoline.set(Nil)
    try {
      var err: Throwable = null
      var r = first
      while(r ne null) {
        try r.run() catch { case t: Throwable => err = t }
        trampoline.get() match {
          case r2 :: rest =>
            trampoline.set(rest)
            r = r2
          case _ => r = null
        }
      }
      if(err ne null) throw err
    } finally trampoline.set(null)
  }

  override def execute(runnable: Runnable): Unit = trampoline.get() match {
    case null => runTrampoline(runnable)
    case r => trampoline.set(runnable :: r)
  }

  override def reportFailure(t: Throwable): Unit = throw t
}

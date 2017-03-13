package debug

import java.util.concurrent._
import scala.concurrent._

class ThreadPoolExecutorMonitor(executor: ThreadPoolExecutor, delaySeconds: Int = 1) extends Runnable {

  private var running = true

  def finish(): Unit = {
    running = false
  }

  def run(): Unit = {
    while (running) {
      println(s"""
                  |Thread Pool Information
                  |Pool Size:${executor.getPoolSize}
                  |Core Pool Size: ${executor.getCorePoolSize}
                  |Active Count: ${executor.getActiveCount}
                  |Completed Count: ${executor.getCompletedTaskCount}
                  |Current Queue Size: ${executor.getQueue.size}""".stripMargin)
      Thread.sleep(1000 * delaySeconds)
    }
  }

}

class ForkJoinMonitor(forkJoinPool: ForkJoinPool, delaySeconds: Int = 1) extends Runnable {

  private var running = true

  def finish(): Unit = {
    running = false
  }

  def run(): Unit = {
    while (running) {
      println(s"""
                  |Fork Join Information
                  |Pool Size:${forkJoinPool.getPoolSize}
                  |Active Thread Count: ${forkJoinPool.getActiveThreadCount}
                  |Running Thread Count: ${forkJoinPool.getRunningThreadCount}
                  |Queued Task Count: ${forkJoinPool.getQueuedTaskCount}
                  |Queued Submissions: ${forkJoinPool.getQueuedSubmissionCount}""".stripMargin)
      Thread.sleep(1000 * delaySeconds)
    }
  }

}

/*
This code originally comes from scala.concurrent.impl.ExecutionContextImpl, modified to not handle exceptions when the
ForkJoinPool constructor fails.  It should only be used for Debugging/Monitoring the global execution context.
Example Usage is to replace "import import scala.concurrent.ExecutionContext.Implicits.global"
with
val debugExecutorService = DebugExecutor.createExecutorService
implicit val global = ExecutionContext.fromExecutorService(debugExecutorService)
If you want monitoring, use the above Classes, e.g. ( new ForkJoinMonitor(debugExecutorService.asInstanceOf[ForkJoinPool]) )
*/
object DebugExecutor {

  def createExecutorService(parallelismOverride: Option[Int] = None): ExecutorService = {

    def getInt(name: String, default: String) = (try System.getProperty(name, default) catch {
      case e: SecurityException => default
    }) match {
      case s if s.charAt(0) == 'x' => (Runtime.getRuntime.availableProcessors * s.substring(1).toDouble).ceil.toInt
      case other                   => other.toInt
    }

    def range(floor: Int, desired: Int, ceiling: Int) = scala.math.min(scala.math.max(floor, desired), ceiling)

    val desiredParallelism = parallelismOverride getOrElse
      range(
        getInt("scala.concurrent.context.minThreads", "1"),
        getInt("scala.concurrent.context.numThreads", "x1"),
        getInt("scala.concurrent.context.maxThreads", "x1"))

    val threadFactory = new DefaultThreadFactory(daemonic = true)

    //This Code has be modified to remove the Try/Catch logic that falls back to a ThreadPoolExecutor
    new ForkJoinPool(desiredParallelism, threadFactory, Thread.getDefaultUncaughtExceptionHandler, true)
  }

  // Implement BlockContext on FJP threads
  class DefaultThreadFactory(daemonic: Boolean) extends ThreadFactory with ForkJoinPool.ForkJoinWorkerThreadFactory {
    def wire[T <: Thread](thread: T): T = {
      println(s"Wiring Thread $thread")
      thread.setDaemon(daemonic)
      thread.setUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler)
      thread
    }

    def newThread(runnable: Runnable): Thread = wire(new Thread(runnable))

    def newThread(fjp: ForkJoinPool): ForkJoinWorkerThread = wire(new ForkJoinWorkerThread(fjp) with BlockContext {

      override def blockOn[T](thunk: => T)(implicit permission: CanAwait): T = {
        var result: T = null.asInstanceOf[T]
        ForkJoinPool.managedBlock(new ForkJoinPool.ManagedBlocker {
          @volatile var isdone = false

          override def block(): Boolean = {
            result = try thunk finally {
              isdone = true
            }
            true
          }

          override def isReleasable = isdone
        })
        result
      }
    })
  }

}

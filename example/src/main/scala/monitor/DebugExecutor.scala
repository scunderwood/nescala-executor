package monitor

import java.util.concurrent._

//DEBUG USE ONLY!
class ThreadPoolExecutorMonitor(executor: ThreadPoolExecutor, delaySeconds: Int = 1) extends Runnable {

  private var running = true
  def finish(): Unit = {
    running = false
  }

  def run(): Unit = {
    while (running) {
      println(s"""
                  |Thread Pool Stats
                  |Pool Size:${executor.getPoolSize}
                  |Core Pool Size: ${executor.getCorePoolSize}
                  |Active Count: ${executor.getActiveCount}
                  |Completed Count: ${executor.getCompletedTaskCount}
                  |Current Queue Size: ${executor.getQueue.size}""".stripMargin)
      Thread.sleep(1000 * delaySeconds)
    }
  }

}

//DEBUG USE ONLY!
class ForkJoinMonitor(forkJoinPool: ForkJoinPool, delaySeconds: Int = 1) extends Runnable {

  private var running = true
  def finish(): Unit = {
    running = false
  }

  def run(): Unit = {
    while (running) {
      println(s"""
                  |Pool Size:${forkJoinPool.getPoolSize}
                  |Active Thread Count: ${forkJoinPool.getActiveThreadCount}
                  |Queued Submissions: ${forkJoinPool.getQueuedSubmissionCount}
                  |""".stripMargin)
      Thread.sleep(1000 * delaySeconds)
    }
  }

}



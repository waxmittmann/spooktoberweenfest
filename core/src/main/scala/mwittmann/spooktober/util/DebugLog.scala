package mwittmann.spooktober.util

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

object DebugLog {

  val exec = new ScheduledThreadPoolExecutor(1)

  val task = new Runnable {
    override def run() = {
      println(storedLog.toString)
      storedLog.setLength(0)
    }
  }

  val f = exec.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS)

  val storedLog = new StringBuffer()

  def dprintln(s: String): Unit = {
    storedLog.append(s"$s\n")
  }

  def dprint(s: String): Unit = {
    storedLog.append(s)
  }

  def dispose(): Unit = {
    f.cancel(true)
    exec.shutdown()
  }
}

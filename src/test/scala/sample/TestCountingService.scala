package sample

import java.util.concurrent.atomic.AtomicInteger

import config.ConfigModule


// Very interesting extending a class that takes an implicit. You don't have to pass the implicit! Woah.
class TestCountingService(implicit m: ConfigModule) extends CountingService {
  private val called = new AtomicInteger(0)

  override def increment(count: Int) = {
    called.incrementAndGet()
    super.increment(count)
  }

  def getNumberOfCalls: Int = called.get()
}

package sample

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import config.AkkaConfigModule
import org.scalatest.{BeforeAndAfterAll, Matchers, OneInstancePerTest, WordSpecLike}
import sample.CountingActor.{Count, Get}

import scala.concurrent.Await
import scala.concurrent.duration._

class CountingActorSpec(_system: ActorSystem)
    extends TestKit(_system)
        with ImplicitSender
        with WordSpecLike
        with OneInstancePerTest
        with Matchers
        with BeforeAndAfterAll {

  def this() = this(ActorSystem("CountingActorSpec"))

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  "a Parfait-managed count actor" must {
    "send the correct count to its counting service" in {
      val m: SystemModule =
        new SystemModule
            with StandardCountingModule
            with StandardAuditBusModule
            with StandardAuditCompanionModule
            with AkkaConfigModule { mInner: SystemModule =>
          override lazy val actorSystem: ActorSystem = _system
          override lazy val countingService = new TestCountingService()(mInner)
        }

      val counter = m.countingActor

      // tell it to count three times
      counter ! Count
      counter ! Count
      counter ! Count

      // check that it has counted correctly
      val duration = 3.seconds
      val result = counter.ask(Get)(duration).mapTo[Int]
      Await.result(result, duration) should be(3)

      // check that it called the sample.TestCountingService the right number of times
      val testService = m.countingService.asInstanceOf[TestCountingService]
      testService.getNumberOfCalls should be(3)
    }

    "send messages to its audit companion" in {
      val auditCompanionProbe: TestProbe = new TestProbe(_system)
      val m: SystemModule =
        new SystemModule
            with StandardCountingModule
            with StandardAuditBusModule
            with StandardAuditCompanionModule
            with AkkaConfigModule {
          override lazy val actorSystem: ActorSystem = _system
          override lazy val auditCompanion = auditCompanionProbe.ref
        }

      val counter = m.countingActor

      counter ! Count
      auditCompanionProbe.expectMsgClass(classOf[String])
    }
  }

}

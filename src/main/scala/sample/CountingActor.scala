package sample

import akka.actor.{Props, Actor}

object CountingActor {

  // this is not required here -- it is simply a convenience so that the name
  // can be defined and referenced from one place
  val name = "CountingActor"

  case object Count
  case object Get

  def props(implicit m: AuditCompanionModule with CountingModule): Props =
    Props(new CountingActor)
}

/**
 * An actor that can count using an injected CountingService.
 *
 */
class CountingActor(implicit m: AuditCompanionModule with CountingModule) extends Actor {

  import CountingActor._

  val auditCompanion = m.auditCompanion

  private var count: Int = 0

  def receive = {
    case Count =>
      count = m.countingService.increment(count)
      auditCompanion ! s"Count is now $count"
    case Get => sender ! count
  }

}

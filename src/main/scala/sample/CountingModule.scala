package sample

import akka.actor.{ActorRef, Actor}
import config.ConfigModule

/**
 * A config module for the counting actor and service.
 * NOTE: This is called SampleModule in the Guice example of Akka DI.
 */

trait CountingModule {
  val countingActor: ActorRef
  def countingService: CountingService
}

/**
 * A standard implementation configuration for CountingModule
 */
trait StandardCountingModule extends CountingModule {
  m: CountingModule
    with AuditCompanionModule
    with ConfigModule =>

  lazy val countingActor: ActorRef =
    m.actorSystem.actorOf(CountingActor.props(m), CountingActor.name)

  def countingService: CountingService = new CountingService()(m)
}

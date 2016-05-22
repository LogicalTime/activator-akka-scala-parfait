package sample

import akka.actor.ActorRef
import config.ConfigModule



trait AuditBusModule {
  def auditBus: ActorRef
}


// We could put sm: SystemModule here.
// However, you can reduce dependencies by only referring to part of the system and this is preferred so you know what you really want.
// Also limiting this means we can test this without needed all the dependencies a systemModule requires
// It is nice to have the cm here so that we know where stuff is coming from
trait StandardAuditBusModule extends AuditBusModule{ m: ConfigModule =>
  lazy val auditBus: ActorRef = m.actorSystem.actorOf(AuditBus.props, AuditBus.name)
}


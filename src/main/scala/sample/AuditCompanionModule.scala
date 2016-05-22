package sample

import akka.actor.ActorRef
import config.ConfigModule

/**
  * Created by Mark on 5/22/2016.
  */
trait AuditCompanionModule {
  def auditCompanion: ActorRef
}


trait StandardAuditCompanionModule extends AuditCompanionModule{ m: ConfigModule with AuditBusModule =>
  lazy val auditCompanion: ActorRef = m.actorSystem.actorOf(AuditCompanion.props(m), AuditCompanion.name)
}
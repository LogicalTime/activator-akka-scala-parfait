package sample

import config.ConfigModule

/**
 * Created by dick on 12/8/14.
 */
trait SystemModule
    extends CountingModule
        with AuditBusModule
        with AuditCompanionModule
        with ConfigModule

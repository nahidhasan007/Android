package net.sharetrip.b2b.view.more.quickPassengers

import net.sharetrip.b2b.localdb.QuickPassengerDao
import net.sharetrip.b2b.view.more.model.QuickPassenger

class QuickPassengerRepo(private var dao: QuickPassengerDao) {

    suspend fun getQuickPassengerList(): List<QuickPassenger> {
        return dao.getQuickPassengerList()
    }
}
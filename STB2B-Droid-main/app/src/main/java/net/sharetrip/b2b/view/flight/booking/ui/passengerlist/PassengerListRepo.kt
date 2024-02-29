package net.sharetrip.b2b.view.flight.booking.ui.passengerlist

import net.sharetrip.b2b.localdb.PassengerDao
import net.sharetrip.b2b.view.flight.booking.model.Passenger

class PassengerListRepo(private val dao: PassengerDao) {

    suspend fun getPassengerList(): List<Passenger> {
        val list = dao.getPassengerList()
        if(list.isEmpty()) {
            val passenger = Passenger(id = "Adult 1")
            dao.savePassenger(passenger)
            return dao.getPassengerList()
        }
        return list
    }
}

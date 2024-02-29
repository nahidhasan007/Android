package net.sharetrip.b2b.view.flight.booking.ui.flightlist

import net.sharetrip.b2b.localdb.PassengerDao
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericRestResponse
import net.sharetrip.b2b.util.toList
import net.sharetrip.b2b.view.flight.booking.model.FilterData
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch
import net.sharetrip.b2b.view.flight.booking.model.FlightSearchResponse
import net.sharetrip.b2b.view.flight.booking.model.Passenger

class FlightListRepo(
    private val endPoint: FlightEndPoint, private val flightSearch: FlightSearch,
    private val dao: PassengerDao
) {
    var childDob = arrayListOf<String>()
    init {
        for(dob in flightSearch.travellersInfo.childDobList){
            childDob.add(dob.date)
        }
    }

    suspend fun getFlightSearchResponse(): GenericRestResponse<FlightSearchResponse> =
        endPoint.searchFlight(
            flightSearch.tripType,
            flightSearch.travellersInfo.numberOfAdult,
            flightSearch.travellersInfo.numberOfChildren,
            flightSearch.travellersInfo.numberOfInfant,
            flightSearch.travellersInfo.classType,
            toList(flightSearch.origin)!!,
            toList(flightSearch.destination)!!,
            toList(flightSearch.flightDate)!!,
            childAge = childDob

        )

    suspend fun getFlightFilterResponse(filterData: FilterData): GenericRestResponse<FlightSearchResponse> =
        endPoint.flightFilter(filterData)

    suspend fun generatePassengerList() {
        dao.deleteAllPassenger()
        val passengerList = ArrayList<Passenger>()

        val adultList = addPassengers(flightSearch.travellersInfo.numberOfAdult, "Adult")
        passengerList.addAll(adultList)

        if (flightSearch.travellersInfo.numberOfChildren > 0) {
            val childList = addPassengers(flightSearch.travellersInfo.numberOfChildren, "Child")
            passengerList.addAll(childList)
        }

        if (flightSearch.travellersInfo.numberOfInfant > 0) {
            val infantList = addPassengers(flightSearch.travellersInfo.numberOfInfant, "Infant")
            passengerList.addAll(infantList)
        }

        dao.savePassengerList(passengerList)
    }

    private fun addPassengers(count: Int, type: String): List<Passenger> {
        val passengerList = ArrayList<Passenger>()
        if (count == 1) {
            val passenger = Passenger("$type 1")
            passengerList.add(passenger)
        } else if (count > 1) {
            for (index in 1..count) {
                val passenger = Passenger("$type $index")
                passengerList.add(passenger)
            }
        }
        return passengerList
    }
}

package net.sharetrip.b2b.view.flight.booking.ui.segment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.sharetrip.b2b.view.flight.booking.model.Flight
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.view.flight.booking.model.Segments
import net.sharetrip.b2b.view.flight.history.model.FlightHistory

class FlightSegmentVM(flights: Flights?, flightHistory: FlightHistory?) : ViewModel() {
    val segmentList = MutableLiveData<List<Any>>()
    val moveToBack = MutableLiveData<Boolean>()
    private val flightsAndSegments = ArrayList<Any>()

    init {
        val mFlight: List<Flight>? = if (flights != null) {
            flights.flight
        } else {
            flightHistory?.flight
        }

        val mItemSegments: List<Segments>? = if (flights != null) {
            flights.segments
        } else {
            flightHistory?.segments
        }

        if (mItemSegments != null) {
            if (mFlight != null) {
                if (mFlight.size == mItemSegments.size) {
                    val mCount = mFlight.size
                    for (i in 0 until mCount) {
                        val flight = mFlight[i]
                        val mSegments = mItemSegments[i]
                        flightsAndSegments.add(flight)
                        val segments = mSegments.segment
                        segments?.get(segments.size - 1)?.transitTime = null
                        flightsAndSegments.addAll(segments!!)
                        segmentList.value = flightsAndSegments
                    }
                }
            }
        }
    }
}

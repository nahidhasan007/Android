package net.sharetrip.b2b.view.flight.history.bookingdetails

import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.model.ModifyHistory

interface ClickViewDetails {
    fun onClickViewDetails(modifyHistory : ModifyHistory, history: FlightHistory)
}
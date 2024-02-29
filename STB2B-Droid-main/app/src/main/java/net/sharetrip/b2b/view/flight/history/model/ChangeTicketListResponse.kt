package net.sharetrip.b2b.view.flight.history.model

data class ChangeTicketListResponse(
	val data: List<ChangeTicket>? = null,
	val offset: Int? = null,
	val limit: Int? = null,
	val count: Int? = null
)
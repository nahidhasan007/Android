package net.sharetrip.b2b.view.flight.booking.ui.proposal

enum class ProposalConstraintsEnum(val constraintsValue: Int, val constraintsType: String) {
    FIXED_ON_BASE_FARE(0, "Fixed on  Base Fare"),
    PERCENTAGE_ON_BASE_FARE(1, "Percentage on Base Fare (%)"),
    PERCENTAGE_ON_TOTAL_TAX(2, "Percentage on Total Tax (%)")
}
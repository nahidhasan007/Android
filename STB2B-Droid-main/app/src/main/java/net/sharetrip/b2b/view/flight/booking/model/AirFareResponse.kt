package net.sharetrip.b2b.view.flight.booking.model

data class AirFareResponse(
    val airFareRules: List<AirFareRule>?,
    val baggages: List<Baggage>?,
    val fareDetails: String?
) {
    fun getAirFareRulesDetails(): String {
        val builder = StringBuilder()
        if (airFareRules != null) {
            for (airFareRule in airFareRules) {
                builder.append(airFareRule.type)
                    .append("\n\n")
                val rules: List<Rule>? = airFareRule.rules

                for (mRule in rules!!) {
                    builder.append(mRule.type).append("\n\n")
                    builder.append(mRule.text).append("\n")
                }

                builder.append("\n\n")
            }
        }
        return builder.toString()
    }
}

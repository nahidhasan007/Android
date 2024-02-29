package net.sharetrip.b2b.view.flight.history.model

enum class Status(val status: String) {
    VOID("VOID"),
    REFUND("REFUND"),
    PAID("PAID"),
    ISSUED("ISSUED"),
    REISSUED("REISSUED"),
    UNPAID("UNPAID"),
    BOOKED("BOOKED"),
}
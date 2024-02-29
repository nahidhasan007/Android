package net.sharetrip.b2b.view.topup.model

data class GateWay(
    val charge: Double,
    val code: String,
    val currency: Currency,
    val id: String,
    val logo: Logo = Logo(),
    val name: String,
    val series: List<Any> = listOf(),
    val type: String,
    val usdPopup: Boolean,
    var isSelected: Boolean
)

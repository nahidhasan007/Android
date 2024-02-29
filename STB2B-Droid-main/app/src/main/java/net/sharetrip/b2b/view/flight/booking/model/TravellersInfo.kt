package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.sharetrip.b2b.util.MsgUtils

@Parcelize
data class TravellersInfo(
    var classType: String = MsgUtils.economy,
    var numberOfAdult: Int = 1,
    var numberOfChildren: Int = 0,
    var numberOfInfant: Int = 0,
    var childDobList: ArrayList<ChildrenDOB> = arrayListOf(),
    var childAge : ArrayList<String> = arrayListOf(),
    var tripDate: String= "",
) : Parcelable {
    fun totalTravellers(): Int {
        return numberOfAdult + numberOfChildren + numberOfInfant
    }
}

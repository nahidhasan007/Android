package net.sharetrip.b2b.view.flight.history.reissue_change_date.travellernumber

import android.util.Log
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.view.flight.booking.model.ChildrenDOB
import net.sharetrip.b2b.view.flight.booking.model.TravellersInfo
import net.sharetrip.b2b.widgets.BaseOperationalVm
import java.text.SimpleDateFormat

class TravellerNumberViewModel(
    var travellersInfo: TravellersInfo?
): BaseOperationalVm() {

    var numberOfAdult: Int = 0
    var numberOfChildren: Int = 0
    var numberOfInfant: Int = 0
    var tripClassType: String
    val childDOBNumber = MutableLiveData<ArrayList<ChildrenDOB>>()
    var childDobList: ArrayList<ChildrenDOB> = arrayListOf()

    val adultNumber = ObservableInt()
    val childNumber = ObservableInt()
    val infantNumber = ObservableInt()
    var firstTravelDate: Long? = null

    init {
        numberOfAdult = travellersInfo!!.numberOfAdult
        numberOfChildren = travellersInfo!!.numberOfChildren
        numberOfInfant = travellersInfo!!.numberOfInfant
        tripClassType = travellersInfo!!.classType
        val childList: ArrayList<ChildrenDOB>? = travellersInfo?.childDobList
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        Log.d(TAG, "tripDate = ${travellersInfo?.tripDate}")
        Log.d(TAG, "travellersInfo = ${travellersInfo}")
        val date = try {
            travellersInfo?.tripDate?.let { sdf.parse(it) }
        } catch (x: Exception) {
            x.printStackTrace()
            null
        }
        if (date != null) {
            firstTravelDate = date.time
        }

        if (childList != null) {
            if (childList.size > 0) {
                for (aChildObj in childList) {
                    val aChild = ChildrenDOB(aChildObj.title, aChildObj.date)
                    childDobList.add(aChild)
                }
                childDOBNumber.value = childList!!
            }
        }

        adultNumber.set(numberOfAdult)
        childNumber.set(numberOfChildren)
        infantNumber.set(numberOfInfant)
    }

    fun checkDob(): Boolean {
        for (child in childDobList) {
            if (child.date.isEmpty()) {
                showMessage("Please fill up all child Date Of Birth")
                return false
            }
        }
        return true
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {

    }
}
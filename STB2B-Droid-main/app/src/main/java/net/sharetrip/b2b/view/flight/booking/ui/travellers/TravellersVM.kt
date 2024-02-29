package net.sharetrip.b2b.view.flight.booking.ui.travellers

import android.os.Bundle
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.view.flight.booking.model.ChildrenDOB
import net.sharetrip.b2b.view.flight.booking.model.TravellersInfo

class TravellersVM(val bundle: Bundle?) : ViewModel() {
    val travellersInfo: TravellersInfo = bundle?.getParcelable(TRAVELLERS_INFO) ?: TravellersInfo()
    val travellers = TravellersInfo(
        travellersInfo.classType,
        travellersInfo.numberOfAdult,
        travellersInfo.numberOfChildren,
        travellersInfo.numberOfInfant,
    )
    val moveToBack = MutableLiveData<Event<TravellersInfo>>()
    val childDOBNumber = MutableLiveData<ArrayList<ChildrenDOB>>()
    var numberOfAdult: Int = travellers.numberOfAdult
    var numberOfChildren: Int = travellers.numberOfChildren
    var numberOfInfant: Int = travellers.numberOfInfant

    var isChildrenAdded: Boolean = true
    val numOfAdult = ObservableInt()
    val numOfChildren = ObservableInt()
    val numbOfInfant = ObservableInt()

    val showMessage: LiveData<Boolean>
        get() = _showMessage
    private val _showMessage = MutableLiveData<Boolean>()

    init {
        for (childDob in travellersInfo.childDobList) {
            val childrenDOB = ChildrenDOB(childDob.title, childDob.date)
            travellers.childDobList.add(childrenDOB)
        }
        childDOBNumber.value = travellers.childDobList
        numOfAdult.set(numberOfAdult)
        numOfChildren.set(numberOfChildren)
        numbOfInfant.set(numberOfInfant)
    }

    fun onDoneButtonClick() {
        if (isDateAdded()) {
            moveToBack.value = Event(travellers)
        } else {
            _showMessage.value = true
        }
    }

    private fun isDateAdded(): Boolean {
        if (travellers.childDobList.size > 0) {
            for (childrenDob in travellers.childDobList) {
                isChildrenAdded = childrenDob.date.isNotEmpty()
                if (!isChildrenAdded) break
            }
        } else {
            isChildrenAdded = true
        }
        return isChildrenAdded
    }

    fun onAddAdult() {
        if (travellers.numberOfAdult + travellers.numberOfChildren < 7) {
            travellers.numberOfAdult++

            numOfAdult.set(travellers.numberOfAdult)
        }
    }

    /**
     * Adult number is the highest infant number.
     */
    fun onRemoveAdult() {
        if (travellers.numberOfAdult > 1) {
            travellers.numberOfAdult--

            if (travellers.numberOfInfant > travellers.numberOfAdult)
                travellers.numberOfInfant = travellers.numberOfAdult

            numbOfInfant.set(travellers.numberOfInfant)
            numOfAdult.set(travellers.numberOfAdult)

        }
    }

    fun onAddChildren() {
        if (travellers.numberOfAdult + travellers.numberOfChildren < 7) {
            travellers.numberOfChildren++
            numOfChildren.set(travellers.numberOfChildren)

            travellers.childDobList.add(
                ChildrenDOB(
                    MsgUtils.children + travellers.numberOfChildren + MsgUtils.dateOfBirth
                )
            )

            childDOBNumber.value = travellers.childDobList
        }
    }

    fun onRemoveChildren() {
        if (travellers.numberOfChildren > 0) {
            travellers.numberOfChildren--
            numOfChildren.set(travellers.numberOfChildren)

            if (travellers.childDobList.size > 0) {
                travellers.childDobList.removeAt(travellers.childDobList.size - 1)
                childDOBNumber.value = travellers.childDobList
            }
        }
    }

    fun onAddInfant() {
        if (travellers.numberOfInfant < travellers.numberOfAdult) {
            travellers.numberOfInfant++
            numbOfInfant.set(travellers.numberOfInfant)
        }
    }

    fun onRemoveInfant() {
        if (travellers.numberOfInfant > 0) {
            travellers.numberOfInfant--
            numbOfInfant.set(travellers.numberOfInfant)
        }
    }
}

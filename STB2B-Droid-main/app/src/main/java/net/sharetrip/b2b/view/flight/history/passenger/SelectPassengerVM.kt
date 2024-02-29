package net.sharetrip.b2b.view.flight.history.passenger

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.view.flight.history.model.Traveller

class SelectPassengerVM : BaseViewModel() {
    var travellers :List<Traveller>? = null
    var isCheckedAllTraveller = ObservableBoolean(false)

    val updateTravellersLIst = MutableLiveData<List<Traveller>>()

    fun selectAllPassenger() {
        travellers?.forEach {
            it.isChecked = !isCheckedAllTraveller.get()
        }
        isCheckedAllTraveller.set(!isCheckedAllTraveller.get())
        updateTravellersLIst.value = travellers
    }

    fun isPassengerSelected(): Boolean {
        var isChecked = false
        travellers?.forEach passenger@{
            if (it.isChecked) {
                isChecked = it.isChecked
                return@passenger
            }
        }
        return isChecked
    }

    fun isAllSelected(position: Int, isChecked: Boolean) {
        travellers?.get(position)?.isChecked = isChecked

        var selected = 0
        var deSelected = 0

        travellers?.forEach {
            if (it.isChecked) {
                selected += 1
            } else {
                deSelected += 1
            }
        }

        if (selected == travellers?.size) {
            isCheckedAllTraveller.set(true)
        } else if (deSelected == travellers?.size) {
            isCheckedAllTraveller.set(false)
        }
    }
}
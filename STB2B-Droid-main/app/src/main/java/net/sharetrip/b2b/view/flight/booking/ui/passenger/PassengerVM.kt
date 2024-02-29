package net.sharetrip.b2b.view.flight.booking.ui.passenger

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.util.Gender
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.util.PASSPORT
import net.sharetrip.b2b.util.ServiceTagConstants
import net.sharetrip.b2b.util.SingleLiveEvent
import net.sharetrip.b2b.util.isGivenNameValid
import net.sharetrip.b2b.util.isNameExceedsCharLength
import net.sharetrip.b2b.util.isPassportNumberValid
import net.sharetrip.b2b.util.isValidName
import net.sharetrip.b2b.util.selectUserTitle
import net.sharetrip.b2b.view.flight.booking.model.ImageUploadResponse
import net.sharetrip.b2b.view.flight.booking.model.Passenger
import net.sharetrip.b2b.view.more.model.QuickPassenger
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PassengerVM(private val repo: PassengerRepo, val isDomestic: Boolean) : ViewModel() {
    var toolbarTitle = ObservableField<String>()
    var birthDate = ObservableField(String())
    var passportExpireDate = ObservableField(String())
    var passenger = ObservableField<Passenger>()
    var isMale = ObservableBoolean(true)
    val isPassportAdded = ObservableBoolean(false)
    var isVisaAdded = ObservableBoolean(false)
    var date = ""
    val dataLoading = ObservableBoolean(false)
    var quickPassengerList = MutableLiveData<Event<List<QuickPassenger>>>()
    var isFirstNameValid = MutableLiveData<Boolean>()
    var isLastNameValid = MutableLiveData<Boolean>()
    var isPassportNumberValid = MutableLiveData<Boolean>()

    val showMessage: LiveData<String>
        get() = showMessageMutableLiveData
    val showMessageMutableLiveData = MutableLiveData<String>()

    val navigateToDest: LiveData<Boolean>
        get() = _navigateToDest
    private val _navigateToDest = MutableLiveData(false)

    val showAlertDialog: LiveData<Any>
        get() = showAlertSingleEvent
    private val showAlertSingleEvent = SingleLiveEvent<Any>()

    init {
        fetchPassenger()
        getQuickPassengerList()
    }

    private fun fetchPassenger() {
        viewModelScope.launch {
            val data = repo.getPassengerByID()
            val tempDate = repo.getFlightDate()
            if (tempDate != null) {
                date = tempDate
            } else {
                showAlertSingleEvent.call()
            }
            passenger.set(data)
            birthDate.set(data.dateOfBirth)
            passportExpireDate.set(data.passportExpireDate)
            toolbarTitle.set(data.id)

            isPassportAdded.set(data.passportCopy.isNotEmpty())
            isVisaAdded.set(data.visaCopy.isNotEmpty())
        }
    }

    private fun getQuickPassengerList() {
        viewModelScope.launch {
            val quickPassenger = repo.getQuickPassengerList()
            quickPassengerList.value = Event(quickPassenger)
        }
    }

    fun setQuickPickerData(position: Int) {
        val info: QuickPassenger? =
            quickPassengerList.value?.getContent()?.get(position)

        info?.let {
            val passengerData = Passenger(passenger.get()!!.id).apply {
                firstName = info.firstName!!
                lastName = info.lastName!!
                gender = info.gender!!
                nationality = info.nationality!!
                dateOfBirth = info.dateOfBirth!!
                passportNumber = info.passportNumber!!
                passportExpireDate = info.passportExpireDate!!
                frequentFlyerNumber = info.frequentFlyerNumber!!
            }

            birthDate.set(info.dateOfBirth)
            passportExpireDate.set(info.passportExpireDate)
            passenger.set(passengerData)
        }
    }

    fun onClickGender(isMaleSelected: Boolean) {
        isMale.set(isMaleSelected)
    }

    fun clickOnNextButton() {
        val passenger = passenger.get()!!
        val gender = Gender.genderIs(isMale.get())
        passenger.gender = gender
        passenger.dateOfBirth = birthDate.get()!!
        passenger.passportExpireDate = passportExpireDate.get()!!
        if (isDomestic) {
            passenger.passportExpireDate = "2022-05-02"
            passenger.passportNumber = "BG"
        }

        val fullName = passenger.titleName + passenger.firstName + passenger.lastName


        when {

            !passenger.firstName.isGivenNameValid() -> {
                showMessageMutableLiveData.value = MsgUtils.first_name_should_use_letter
                return
            }

            !fullName.isNameExceedsCharLength() -> {
                showMessageMutableLiveData.value = MsgUtils.name_validation_msg
                return
            }

            passenger.lastName.isEmpty() -> {
                showMessageMutableLiveData.value = MsgUtils.last_name_empty
                return
            }

            !passenger.lastName.isValidName() -> {
                showMessageMutableLiveData.value = MsgUtils.last_name_should_use_letter
                return
            }

            !isDomestic && !passenger.passportNumber.isPassportNumberValid() -> {
                showMessageMutableLiveData.value = MsgUtils.invalid_passport
                return
            }

            !passenger.hasRequiredData(isDomestic) -> {
                showMessageMutableLiveData.value = MsgUtils.requiredDataMsg
                return
            }
        }

        passenger.titleName = gender.selectUserTitle(passenger.dateOfBirth, date)
        updatePassenger(passenger)
    }

    private fun updatePassenger(passenger: Passenger) {
        viewModelScope.launch {
            repo.updatePassenger(passenger)
            _navigateToDest.value = true
        }
    }

    fun setNationality(code: String) {
        val data = passenger.get()
        data?.nationality = code
        passenger.set(data)
    }

    fun setDateOfBirth(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val month = if (monthOfYear < 10)
            "0$monthOfYear"
        else
            monthOfYear.toString()

        val day = if (dayOfMonth < 10)
            "0$dayOfMonth"
        else
            dayOfMonth.toString()

        birthDate.set("$year-$month-$day")
    }

    fun setMealCode(code: String) {
        val data = passenger.get()!!
        data.mealPreference = code
        passenger.set(data)
    }

    fun setPassportExpireDate(date: String) {
        passportExpireDate.set(date)
    }

    fun setWheelChairCode(code: String) {
        val data = passenger.get()!!
        data.wheelChair = code
        passenger.set(data)
    }

    fun getUrlFromFilepath(file: File, type: String, mime: String) {
        val requestPhotoFile = file.asRequestBody(mime.toMediaTypeOrNull())

        val image = MultipartBody.Part.createFormData("uploadFile", file.name, requestPhotoFile)

        viewModelScope.launch {
            dataLoading.set(true)
            val data = repo.sendFile(image, ServiceTagConstants.mTraveler)
            imageResponseOperation(data, type)
            dataLoading.set(false)
        }
    }

    private fun imageResponseOperation(
        data: GenericResponse<RestResponse<ImageUploadResponse>>,
        type: String
    ) {
        when (data) {
            is BaseResponse.Success -> {
                if (type == PASSPORT) {
                    isPassportAdded.set(true)
                    passenger.get()!!.passportCopy = data.body.response.path
                } else {
                    isVisaAdded.set(true)
                    passenger.get()!!.visaCopy = data.body.response.path
                }
                showMessageMutableLiveData.value = data.body.message
            }

            is BaseResponse.ApiError ->
                showMessageMutableLiveData.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                showMessageMutableLiveData.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                showMessageMutableLiveData.value = MsgUtils.unKnownErrorMsg
        }
    }

    fun onTextChangedForFirstName(s: CharSequence, start: Int, before: Int, count: Int) {
        isFirstNameValid.value = s.toString().isGivenNameValid()
    }

    fun onTextChangedForLastName(s: CharSequence, start: Int, before: Int, count: Int) {
        isLastNameValid.value = s.toString().isValidName()
    }

    fun onTextChangedForPassportNumber(s: CharSequence, start: Int, before: Int, count: Int) {
        isPassportNumberValid.value = s.toString().isPassportNumberValid()
    }
}

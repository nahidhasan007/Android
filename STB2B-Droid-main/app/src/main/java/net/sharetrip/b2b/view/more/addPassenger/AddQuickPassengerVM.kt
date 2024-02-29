package net.sharetrip.b2b.view.more.addPassenger

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.util.MsgUtils.birth_date_empty
import net.sharetrip.b2b.util.MsgUtils.enter_valid_email
import net.sharetrip.b2b.util.MsgUtils.first_name_should_use_letter
import net.sharetrip.b2b.util.MsgUtils.invalid_passport
import net.sharetrip.b2b.util.MsgUtils.last_name_empty
import net.sharetrip.b2b.util.MsgUtils.last_name_should_use_letter
import net.sharetrip.b2b.util.MsgUtils.passport_expiry_date_empty
import net.sharetrip.b2b.view.flight.booking.model.ImageUploadResponse
import net.sharetrip.b2b.view.more.model.QuickPassenger
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddQuickPassengerVM(private val repo: AddQuickPassengerRepo) : ViewModel() {
    var passenger = QuickPassenger()
    val dataLoading = ObservableBoolean(false)
    var birthDate = ObservableField(String())
    var passportExpireDate = ObservableField(String())
    var isMale = ObservableBoolean(true)
    var date = "2023-06-06"

    val showMessage = MutableLiveData<Event<String>>()
    val isPassportAdded = ObservableBoolean(false)
    var isVisaAdded = ObservableBoolean(false)
    val navigateToDest = MutableLiveData<Event<Boolean>>()
    var isFirstNameValid = MutableLiveData<Boolean>()
    var isLastNameValid = MutableLiveData<Boolean>()
    var isPassportNumberValid = MutableLiveData<Boolean>()

    fun setPrimaryData(dob: String?, passportExpDate: String?, gender: String) {
        birthDate.set(dob)
        passportExpireDate.set(passportExpDate)
        if (gender != Gender.male) {
            isMale.set(false)
        }
    }

    fun onClickSaveButton() {
        when {
            passenger.firstName?.isGivenNameValid() == false -> {
                showMessage.value = Event(first_name_should_use_letter)
                return
            }
            passenger.lastName == null || passenger.lastName?.isEmpty() == true -> {
                showMessage.value = Event(last_name_empty)
                return
            }
            passenger.lastName?.isValidName() == false -> {
                showMessage.value = Event(last_name_should_use_letter)
                return
            }
            passenger.email == null || passenger.email?.isValidEmail() == false -> {
                showMessage.value = Event(enter_valid_email)
                return
            }
            passenger.passportNumber?.isNotEmpty() == true && passenger.passportNumber?.isPassportNumberValid() == false -> {
                showMessage.value = Event(invalid_passport)
                return
            }
            birthDate.get() == null || birthDate.get()?.isEmpty() == true -> {
                showMessage.value = Event(birth_date_empty)
                return
            }
            passenger.passportNumber?.isNotEmpty() == true && (passportExpireDate.get()
                ?.isEmpty() == true || passportExpireDate.get() == null) -> {
                showMessage.value = Event(passport_expiry_date_empty)
                return
            }
        }
        val gender = Gender.genderIs(isMale.get())
        passenger.gender = gender
        passenger.dateOfBirth = birthDate.get()!!
        passenger.passportExpireDate = passportExpireDate.get()!!
        savePassenger(passenger)
    }

    private fun savePassenger(passenger: QuickPassenger) {
        viewModelScope.launch {
            repo.saveQuickPassenger(passenger)
            navigateToDest.value = Event(true)
        }
    }

    fun onClickGender(isMaleSelected: Boolean) {
        isMale.set(isMaleSelected)
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

    fun setPassportExpireDate(date: String) {
        passportExpireDate.set(date)
    }

    fun setNationality(code: String) {
        passenger.nationality = code
    }

    fun getUrlFromFilepath(file: File, type: String, mime: String) {
        val requestPhotoFile = file.asRequestBody(mime.toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("uploadFile", file.name, requestPhotoFile)

        viewModelScope.launch {
            dataLoading.set(true)
            val data = repo.sendFile(image, ServiceTagConstants.mQuickPassenger)
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
                    passenger.passportCopy = data.body.response.path
                } else {
                    isVisaAdded.set(true)
                    passenger.visaCopy = data.body.response.path
                }
                showMessage.value = Event(data.body.message)
            }

            is BaseResponse.ApiError ->
                showMessage.value = Event(data.errorBody.message)

            is BaseResponse.NetworkError ->
                showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError ->
                showMessage.value = Event(MsgUtils.unKnownErrorMsg)
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
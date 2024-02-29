package net.sharetrip.b2b.view.more.addPassenger

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentAddQuickPassengerBinding
import net.sharetrip.b2b.imageCoprresion.ImageZipper
import net.sharetrip.b2b.localdb.LocalDataBase
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.view.flight.booking.model.Nationality
import net.sharetrip.b2b.view.flight.booking.ui.nationality.NATIONALITY_KEY
import net.sharetrip.b2b.view.more.model.QuickPassenger
import net.sharetrip.b2b.view.more.quickPassengers.QuickPassengerFragment
import java.io.File
import java.util.*

class AddQuickPassengerFragment : Fragment() {
    lateinit var bindingView: FragmentAddQuickPassengerBinding
    private val calender = Calendar.getInstance()
    private val year = 2000
    private val month = calender.get(Calendar.MONTH)
    private val day = calender.get(Calendar.DAY_OF_MONTH)
    private var type = ""
    var travellerType = ""
    private var checkedItem = 0
    var quickPassenger: QuickPassenger? = null
    private val travellersTypeList = arrayOf("Adult", "Child", "Infant")

    val viewModel: AddQuickPassengerVM by lazy {
        val dao = LocalDataBase.getDataBase(requireContext()).quickPassengerDao()
        val flightEndPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        AddQuickPassengerVMFactory(AddQuickPassengerRepo(dao, flightEndPoint)).create(
            AddQuickPassengerVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentAddQuickPassengerBinding.inflate(layoutInflater, container, false)
        quickPassenger = arguments?.getParcelable(QuickPassengerFragment.ARG_QUICK_PASSENGER)
        bindingView.viewModel = viewModel
        if (quickPassenger != null) {
            viewModel.passenger = quickPassenger as QuickPassenger
            viewModel.setPrimaryData(
                quickPassenger!!.dateOfBirth!!,
                quickPassenger!!.passportExpireDate,
                quickPassenger!!.gender!!
            )
        }

        observeName()

        observePassportNumber()

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.textViewPassportUpload.setOnClickListener {
            type = PASSPORT
            checkPermission()
        }

        bindingView.textViewVisaUpload.setOnClickListener {
            type = VISA
            checkPermission()
        }

        bindingView.editTextNationality.setOnClickListener {
            findNavController().navigate(R.id.action_passenger_to_nationality)
        }

        bindingView.editTextTravellerType.setOnClickListener {
            selectTravellerType()
        }

        val nationality = getNavigationResultLiveData<Nationality>(NATIONALITY_KEY)
        nationality?.observe(viewLifecycleOwner) { data ->
            viewModel.setNationality(data.code)
        }

        bindingView.editTextDateOfBirth.setOnClickListener {
            showDobDatePicker()
        }

        bindingView.editTextPassportDate.setOnClickListener {
            if (viewModel.passenger.passportNumber?.isPassportNumberValid() == true) {
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        val month = formatToTwoDigit(monthOfYear + 1)
                        val day = formatToTwoDigit(dayOfMonth)
                        val date = "$year-$month-$day"
                        viewModel.setPassportExpireDate(date)
                    },
                    DateUtils.getCalender().year,
                    DateUtils.getCalender().month,
                    DateUtils.getCalender().day
                )
                calender.time = DateUtils.stringToDate(viewModel.date)!!
                datePickerDialog.datePicker.minDate = calender.timeInMillis

                datePickerDialog.show()
            } else {
                viewModel.showMessage.value = Event(getString(R.string.add_passport_number_message))
            }
        }

        viewModel.showMessage.observe(viewLifecycleOwner, EventObserver {
            showToast(requireContext(), it)
        })

        viewModel.navigateToDest.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                showSuccessfulDialog()
            }
        })

        return bindingView.root
    }

    private fun observePassportNumber() {
        viewModel.isPassportNumberValid.observe(viewLifecycleOwner) {
            if (it) {
                bindingView.layoutInputPassportNumber.helperText = getString(R.string.required)
            } else {
                bindingView.layoutInputPassportNumber.error =
                    getString(R.string.invalid_passport)
            }
        }
    }

    private fun observeName() {
        viewModel.isFirstNameValid.observe(viewLifecycleOwner) {
            if (it) {
                bindingView.layoutInputGivenName.helperText = " "
            } else {
                bindingView.layoutInputGivenName.error = getString(R.string.only_use_letters)
            }
        }
        viewModel.isLastNameValid.observe(viewLifecycleOwner) {
            if (it) {
                bindingView.layoutInputLastName.helperText = getString(R.string.required)
            } else {
                bindingView.layoutInputLastName.error = getString(R.string.only_use_letters)
            }
        }
    }

    private fun showDobDatePicker() {
        val dobCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->

                viewModel.setDateOfBirth(year, month + 1, dayOfMonth)

            }, year, month, day
        )

        when (viewModel.passenger.travellerType) {
            travellersTypeList[0] -> {
                //Adult
                dobCalendar.add(Calendar.YEAR, -11)
                datePickerDialog.datePicker.maxDate = dobCalendar.timeInMillis
            }
            travellersTypeList[1] -> {
                //child
                dobCalendar.add(Calendar.YEAR, -2)
                datePickerDialog.datePicker.maxDate = dobCalendar.timeInMillis

                dobCalendar.add(Calendar.YEAR, -9)
                datePickerDialog.datePicker.minDate = dobCalendar.timeInMillis

            }
            else -> {
                //infant
                dobCalendar.add(Calendar.YEAR, -2)
                datePickerDialog.datePicker.minDate = dobCalendar.timeInMillis
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            }
        }

        datePickerDialog.show()
    }

    private fun showSuccessfulDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.success))
            .setMessage(resources.getString(R.string.successfully_saved_passenger))
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                findNavController().navigate(R.id.action_add_quick_passenger_to_more)
            }
            .show()
    }

    private fun selectTravellerType() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.select_traveller_type))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                viewModel.passenger.travellerType = travellerType
                bindingView.editTextTravellerType.setText(travellerType)
                bindingView.editTextDateOfBirth.setText("")
            }
            .setSingleChoiceItems(travellersTypeList, checkedItem) { _, which ->
                checkedItem = which
                travellerType = travellersTypeList[which]
            }
            .show()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    GALLERY_PERMISSION
                )
            } else {
                galleryIntent()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            GALLERY_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent()
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun galleryIntent() {
        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.type = "*/*"
        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(chooseFile, GALLERY_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_PERMISSION && resultCode == Activity.RESULT_OK) {
            try {
                val mime = requireContext().contentResolver.getType(data!!.data!!)
                if (mime!!.contains("image") || mime.contains("pdf")) {
                    val file = File(FileUtils.getRealPath(requireContext(), data.data!!)!!)
                    if (Integer.parseInt((file.length() / 1024).toString()) > 1024 && mime.contains(
                            "image"
                        )
                    ) {
                        viewModel.getUrlFromFilepath(
                            ImageZipper(requireContext()).compressToFile(
                                file
                            ), type, mime
                        )
                    } else {
                        viewModel.getUrlFromFilepath(file, type, mime)
                    }
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.wrong_formatted_document),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
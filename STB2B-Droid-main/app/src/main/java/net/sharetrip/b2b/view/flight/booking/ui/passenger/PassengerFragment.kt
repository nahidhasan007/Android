package net.sharetrip.b2b.view.flight.booking.ui.passenger

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentPassengerBinding
import net.sharetrip.b2b.imageCoprresion.ImageZipper
import net.sharetrip.b2b.localdb.LocalDataBase
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.DateUtils
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.FileUtils
import net.sharetrip.b2b.util.GALLERY_PERMISSION
import net.sharetrip.b2b.util.MsgUtils.emptyBookingDate
import net.sharetrip.b2b.util.PASSPORT
import net.sharetrip.b2b.util.VISA
import net.sharetrip.b2b.util.formatToTwoDigit
import net.sharetrip.b2b.util.getNavigationResultLiveData
import net.sharetrip.b2b.util.isPassportNumberValid
import net.sharetrip.b2b.view.flight.booking.model.Nationality
import net.sharetrip.b2b.view.flight.booking.model.SpecialServiceRequest
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment
import net.sharetrip.b2b.view.flight.booking.ui.nationality.NATIONALITY_KEY
import java.io.File
import java.util.Calendar

class PassengerFragment : Fragment() {
    private val viewModel: PassengerVM by lazy {
        val dao = LocalDataBase.getDataBase(requireContext()).passengerDao()
        var passengerId = arguments?.getString(KEY_PASSENGER_ID)
        val isDomestic = arguments?.getBoolean(FlightDetailsFragment.ARG_IS_DOMESTIC)!!
        val flightEndPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        val flightSearchDao = LocalDataBase.getDataBase(requireContext()).flightSearchDao()
        val quickPassengerDao = LocalDataBase.getDataBase(requireContext()).quickPassengerDao()

        if (passengerId == null) passengerId = "Adult 1"
        PassengerVMFactory(
            PassengerRepo(dao, passengerId, flightEndPoint, flightSearchDao, quickPassengerDao),
            isDomestic
        ).create(
            PassengerVM::class.java
        )
    }
    private val calender = Calendar.getInstance()
    private val year = 2000
    private val month = calender.get(Calendar.MONTH)
    private val day = calender.get(Calendar.DAY_OF_MONTH)
    private var type = ""

    private lateinit var bindingView: FragmentPassengerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentPassengerBinding.inflate(inflater, container, false)
        bindingView.viewModel = viewModel

        bindingView.editTextNationality.setOnClickListener {
            findNavController().navigate(R.id.action_passenger_to_nationality)
        }

        observeAlertDialog()

        val nationality = getNavigationResultLiveData<Nationality>(NATIONALITY_KEY)
        nationality?.observe(viewLifecycleOwner) { data ->
            viewModel.setNationality(data.code)
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

        bindingView.editTextDateOfBirth.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(), { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    viewModel.setDateOfBirth(year, month + 1, dayOfMonth)
                }, year, month, day
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        bindingView.editTextPassportDate.setOnClickListener {
            if (viewModel.passenger.get()?.passportNumber?.isPassportNumberValid() == true) {
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
                calender.add(Calendar.MONTH, 6)
                datePickerDialog.datePicker.minDate = calender.timeInMillis

                datePickerDialog.show()
            } else {
                viewModel.showMessageMutableLiveData.value =
                    getString(R.string.add_passport_number_message)
            }
        }

        bindingView.cardViewMeal.setOnClickListener {
            val bundle = bundleOf(
                ARG_BOTTOM_SHEET_TITLE to "Meal Preference(Optional)",
                ARG_NAVIGATION_RESULT_KEY to KEY_MEAL
            )
            findNavController().navigate(R.id.action_passenger_to_bottom_sheet, bundle)
        }

        val mealCode = getNavigationResultLiveData<SpecialServiceRequest>(KEY_MEAL)
        mealCode?.observe(viewLifecycleOwner) { meal ->
            bindingView.mealSummary.text = meal.name
            viewModel.setMealCode(meal.code)
        }

        bindingView.cardViewWheelchair.setOnClickListener {
            val bundle = bundleOf(
                ARG_BOTTOM_SHEET_TITLE to "WheelChair (If needed)",
                ARG_NAVIGATION_RESULT_KEY to KEY_WHEEL_CHAIR
            )
            findNavController().navigate(R.id.action_passenger_to_bottom_sheet, bundle)
        }

        val chairCode = getNavigationResultLiveData<SpecialServiceRequest>(KEY_WHEEL_CHAIR)
        chairCode?.observe(viewLifecycleOwner) { wheelChair ->
            bindingView.wheelchairSummary.text = wheelChair.name
            viewModel.setWheelChairCode(wheelChair.code)
        }

        viewModel.showMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.navigateToDest.observe(viewLifecycleOwner, androidx.lifecycle.Observer { isTrue ->
            isTrue.let {
                if (it) findNavController().navigateUp()
            }
        })

        viewModel.quickPassengerList.observe(viewLifecycleOwner, EventObserver { passengerList ->
            val dataList = ArrayList<String>()
            for (passengerNameList in passengerList) {
                passengerNameList.lastName?.let { name ->
                    dataList.add(name)
                }
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                dataList
            )
            bindingView.quickPickAutoCompleteTextView.setAdapter(adapter)
            bindingView.quickPickAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
                viewModel.setQuickPickerData(position)
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

    private fun observeAlertDialog() {
        viewModel.showAlertDialog.observe(
            viewLifecycleOwner
        ) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.error))
            builder.setMessage(emptyBookingDate)
            builder.setCancelable(false)
            builder.setPositiveButton(getString(R.string.try_again)) { _, _ ->
                findNavController().popBackStack()
            }
            builder.show()
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                || requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    GALLERY_PERMISSION
                )
            } else {
                galleryIntent()
            }
        } else {
            if (
                requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                || requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    GALLERY_PERMISSION
                )
            } else {
                galleryIntent()
            }
        }
    }

    private fun galleryIntent() {
        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.type = "*/*"
        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(chooseFile, GALLERY_PERMISSION)
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
                            ImageZipper(requireContext()).compressToFile(file), type, mime
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

const val KEY_MEAL = "key_meal"
const val KEY_WHEEL_CHAIR = "key_wheel_chair"
const val KEY_PASSENGER_ID = "key_passenger_id"
const val IMAGE_URL = "url"

package net.sharetrip.b2b.view.flight.history.reissue_change_date

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.aceinteract.android.stepper.StepperNavListener
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.FragmentReissueChangeDateBinding
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.flight.history.historydetails.FlightHistoryDetailsFragment
import net.sharetrip.b2b.view.flight.history.historydetails.FlightHistoryDetailsFragment.Companion.BOOKING_CODE
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueEligibilityResponse
import net.sharetrip.b2b.widgets.BaseFragment

class ReissueChangeDateFragment : BaseFragment<FragmentReissueChangeDateBinding>(),
    StepperNavListener {

    companion object {
        const val TAG: String = "ReissueChangeDateFragment"
    }


    private val sharedViewModel: ReissueChangeDateSharedViewModel by activityViewModels()

    private var stepNo = 0
    private var _navController: NavController? = null
    private val navController: NavController get() = _navController!!

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val eligibilityResponse = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requireArguments().getParcelable(
                    FlightHistoryDetailsFragment.ARG_REISSUE_ELIGIBILITY_RESPONSE,
                    ReissueEligibilityResponse::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                requireArguments().getParcelable(FlightHistoryDetailsFragment.ARG_REISSUE_ELIGIBILITY_RESPONSE)
            }

            val mBookingCode = requireArguments().getString(BOOKING_CODE)
                ?: throw IllegalStateException("Booking Code must not be null!")
            sharedViewModel.bookingCode = mBookingCode


            if (sharedViewModel.reissueEligibilityResponse == null) {
                sharedViewModel.reissueEligibilityResponse = eligibilityResponse

            }
        }
    }


    override fun onDestroyView() {
        onBackPressedCallback.remove()
        super.onDestroyView()
        sharedViewModel.clearViewModel()
    }

    override fun layoutId(): Int = R.layout.fragment_reissue_change_date
    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        _navController = navHostFragment.navController
        bindingView.stepperNavigationView.setupWithNavController(navController)
        bindingView.stepperNavigationView.stepperNavListener = this
        bindingView.reissueSharedVM = sharedViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.travellerSelectionFragment -> {
                    sharedViewModel.subtitle.postValue("STEP 1: Select Traveller(s)")
                }

                R.id.reissueFlightSelectionFragment -> {
                    sharedViewModel.subtitle.postValue("STEP 2: Select Flight to change")
                }

                R.id.reissueFlightSearchFragment -> {
                    sharedViewModel.title.postValue("Date Change")
                    sharedViewModel.subtitle.postValue("STEP 3: Select New Flight")
                }
            }

        }

        bindingView.backButton.setOnClickListener {
            if (navController.currentDestination?.id == R.id.travellerSelectionFragment) {
                findNavController().navigateUp()
            } else if (navController.currentDestination?.id == R.id.flightDetailsFragment2 && sharedViewModel.reissueEligibilityResponse?.reissueSearch?.status == "QUOTED"
                || navController.currentDestination?.id == R.id.flightListFragment2
                && sharedViewModel.reissueEligibilityResponse?.reissueSearch?.status == "QUOTED"
            ) {
                findNavController().navigateUp()

            } else {
                bindingView.stepperNavigationView.goToPreviousStep()
            }
        }

        sharedViewModel.termsAndConditionCheckbox.observe(viewLifecycleOwner) {
            bindingView.nextButton.isEnabled = it == true
        }
        sharedViewModel.selectedPassengers.observe(this) {
            bindingView.nextButton.isEnabled = it.size > 0
        }


        bindingView.nextButton.setOnClickListener {
            if (navController.currentDestination?.id == R.id.reissueFlightSearchFragment) {
                sharedViewModel.onSearchClick.postValue(Event(true))
                sharedViewModel.isQuotationCalled.value = false
            }
            if (navController.currentDestination?.id == R.id.flightDetailsFragment2
                && sharedViewModel.reissueEligibilityResponse?.automationSupported == false
            ) {
                sharedViewModel.isQuotationCalled.value = true
            }
            sharedViewModel.isQuotationCalled.value = false
            bindingView.stepperNavigationView.goToNextStep()
        }
        if (sharedViewModel.reissueEligibilityResponse!!.skipSelection) {
            for (i in 1..2) {
                bindingView.stepperNavigationView.goToNextStep()
            }
        }
    }

    override fun onCompleted() {}

    override fun onStepChanged(step: Int) {
        when (step) {
            0 -> {
                stepNo = 1
                bindingView.nextButton.text = getString(R.string.next_btn)
                bindingView.nextButton.isVisible = true
                bindingView.nextButton.isEnabled =
                    !sharedViewModel.selectedPassengers.value.isNullOrEmpty()
                sharedViewModel.title.postValue("Date Change")
                val res = if (sharedViewModel.quotationConfirmCheck.value == true) {
                    findNavController().navigateUp()
                } else if (sharedViewModel.manualCancelCheck.value == true) {

                    findNavController().navigateUp()
                } else if (sharedViewModel.reissueConfirmCheck.value == true) {
                    findNavController().navigateUp()
                } else if (sharedViewModel.reissueSuccess.value == true) {

                    findNavController().navigateUp()
                } else {
                    false
                }
            }

            1 -> {
                stepNo = 2
                bindingView.nextButton.text = getString(R.string.next_btn)
                bindingView.nextButton.isVisible = true
                bindingView.nextButton.isEnabled = true
                sharedViewModel.title.postValue("Date Change")
            }

            else -> {
                stepNo = step
                bindingView.nextButton.isVisible = false
                if (navController.currentDestination?.id == R.id.reissueFlightSearchFragment) {
                    bindingView.nextButton.text = getString(R.string.search_flight)
                    bindingView.nextButton.isVisible = true
                    sharedViewModel.title.postValue("Date Change")
                }
                if (navController.currentDestination?.id == R.id.flightDetailsFragment2 || navController.currentDestination?.id == R.id.flightListFragment2) {
                    val size = sharedViewModel.manualFlights.size
                    if (sharedViewModel.manualFlights.isNotEmpty()) {
                        sharedViewModel.title.postValue(sharedViewModel.manualFlights[0].origin?.code + "-${sharedViewModel.manualFlights[size - 1].destination?.code}")
                    }
                }

                if (sharedViewModel.reissueEligibilityResponse?.reissueSearch?.status == "QUOTED") {
                    sharedViewModel.title.postValue(sharedViewModel.flightHistory?.flightCode)
                    sharedViewModel.subtitle.postValue("Step 3: Approve Quotation")
                }

            }
        }
    }

    private fun backPressed() {
        Log.d("BackPressed", "You are using me")
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }
}

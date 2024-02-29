package net.sharetrip.b2b.view.transaction.view.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentTransactionListFilterBinding
import net.sharetrip.b2b.util.DateUtils
import net.sharetrip.b2b.view.transaction.model.TransactionFilter
import java.util.*

class TransactionListFilterFragment : Fragment(), View.OnClickListener {
    lateinit var bindingView: FragmentTransactionListFilterBinding
    var transactionFilter: TransactionFilter? = TransactionFilter()
    private val paymentType = arrayListOf(
            "Offline Payment",
            "Flight Booking",
            "Online Payment",
            "Reissue",
            "Refund",
            "Void",
            "Hotel Booking",
            "Service Charge",
            "Baggage Add",
            "Change",
            "Partial Flight Booking",
            "Cancel",
            "Package booking",
            "Others"
    )
    private val sourceType =
            arrayListOf("Agent Balance", "Hotel", "Flight", "Payment", "ShareTrip Balance")
    private val transactionStatus = arrayListOf("Approved", "Declined", "Pending")
    private var selectedIndex = 0
    private var dates = ArrayList<String>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentTransactionListFilterBinding.inflate(inflater, container, false)
        val filterData = arguments?.getString(ARG_TRANSACTION_FILTER_OBJECT)
        if (!filterData.isNullOrEmpty()) {
            transactionFilter = Gson().fromJson(filterData, TransactionFilter::class.java)
        }
        bindingView.transactionFilter = transactionFilter

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.buttonFilterApply.setOnClickListener(this)
        bindingView.includePaymentType.layoutArrow.setOnClickListener(this)
        bindingView.includeSource.layoutArrow.setOnClickListener(this)
        bindingView.includeTransactionStatus.layoutArrow.setOnClickListener(this)
        bindingView.includeCreatedAt.layoutArrow.setOnClickListener(this)
        bindingView.tvReset.setOnClickListener(this)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(
                TransactionFilterBottomSheet.RESULT_CHECKED_DATA
        )?.observe(viewLifecycleOwner) { result ->
            val filterResponse =
                    result.getString(TransactionFilterBottomSheet.ARGUMENT_CHECKED_DATA)
            when (selectedIndex) {
                TransactionFilterTypeEnum.PAYMENT_TYPE.filterCode -> {
                    if (filterResponse != null) {
                        transactionFilter?.type = filterResponse
                    }
                    bindingView.includePaymentType.hasSubtitle = true
                    bindingView.includePaymentType.subTitle = filterResponse
                }
                TransactionFilterTypeEnum.SOURCE.filterCode -> {
                    if (filterResponse != null) {
                        transactionFilter?.source = filterResponse
                    }
                    bindingView.includeSource.hasSubtitle = true
                    bindingView.includeSource.subTitle = if (filterResponse == getString(R.string.hotel_uppercase) || filterResponse == getString(R.string.payment_uppercase) || filterResponse == getString(
                                                R.string.flight_uppercase)) {
                        filterResponse.first().plus(filterResponse.substring(1).toLowerCase(Locale.getDefault()))
                    } else {
                        filterResponse
                    }
                }
                TransactionFilterTypeEnum.STATUS.filterCode -> {
                    if (filterResponse != null) {
                        transactionFilter?.status = filterResponse
                    }
                    bindingView.includeTransactionStatus.hasSubtitle = true
                    bindingView.includeTransactionStatus.subTitle = filterResponse
                }
            }
        }

        return bindingView.root
    }

    override fun onClick(view: View?) {
        when (view) {
            bindingView.includePaymentType.layoutArrow -> {
                selectedIndex = TransactionFilterTypeEnum.PAYMENT_TYPE.filterCode
                val bundle = Bundle()
                bundle.putStringArrayList(ARG_TRANSACTION_FILTER_DATA_LIST, paymentType)
                bundle.putInt(
                        ARG_TRANSACTION_FILTER_TITLE,
                        TransactionFilterTypeEnum.PAYMENT_TYPE.filterCode
                )
                findNavController().navigate(
                        R.id.action_open_transaction_filter_bottom_sheet,
                        bundle
                )
            }
            bindingView.includeSource.layoutArrow -> {
                selectedIndex = TransactionFilterTypeEnum.SOURCE.filterCode
                val bundle = Bundle()
                bundle.putStringArrayList(ARG_TRANSACTION_FILTER_DATA_LIST, sourceType)
                bundle.putInt(
                        ARG_TRANSACTION_FILTER_TITLE,
                        TransactionFilterTypeEnum.SOURCE.filterCode
                )
                findNavController().navigate(
                        R.id.action_open_transaction_filter_bottom_sheet,
                        bundle
                )
            }
            bindingView.includeTransactionStatus.layoutArrow -> {
                selectedIndex = TransactionFilterTypeEnum.STATUS.filterCode
                val bundle = Bundle()
                bundle.putStringArrayList(ARG_TRANSACTION_FILTER_DATA_LIST, transactionStatus)
                bundle.putInt(
                        ARG_TRANSACTION_FILTER_TITLE,
                        TransactionFilterTypeEnum.STATUS.filterCode
                )
                findNavController().navigate(
                        R.id.action_open_transaction_filter_bottom_sheet,
                        bundle
                )
            }
            bindingView.includeCreatedAt.layoutArrow -> {
                showDateSelection()
            }
            bindingView.buttonFilterApply -> {
                setDataForResult(view)
            }
            bindingView.tvReset -> {
                transactionFilter = TransactionFilter()
                bindingView.transactionFilter = transactionFilter
            }
        }
    }

    private fun setDataForResult(view: View?) {
        val bundle = Bundle()
        transactionFilter?.reference = bindingView.includeReference.editText.text.toString()
        bundle.putParcelable(ARG_TRANSACTION_FILTER_OBJECT, transactionFilter)
        view?.findNavController()?.previousBackStackEntry?.savedStateHandle?.set(
                RESULT_TRANSACTION_FILTER,
                bundle
        )
        view?.findNavController()?.popBackStack()
    }

    private fun showDateSelection() {
        val calendar = Calendar.getInstance()
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        calendar.add(Calendar.YEAR, -1)
        val startDate = calendar.timeInMillis
        calendar.timeInMillis = today

        calendar.add(Calendar.YEAR, 1)
        val endDate = calendar.timeInMillis

        if (dates.size > 1) {
            val flightDates = dates
            builder.setSelection(
                    Pair(DateUtils.increaseDay(flightDates[0]), DateUtils.increaseDay(flightDates[1]))
            )
        }
        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setOpenAt(System.currentTimeMillis())
        constraintsBuilder.setStart(startDate)
        constraintsBuilder.setEnd(endDate)
        builder.setCalendarConstraints(constraintsBuilder.build())

        val datePicker = builder.build()
        datePicker.addOnPositiveButtonClickListener {
            dates = arrayListOf(
                    DateUtils.millisecondsToString(it.first, DateUtils.API_DATE_PATTERN)!!,
                    DateUtils.millisecondsToString(it.second, DateUtils.API_DATE_PATTERN)!!
            )
            transactionFilter?.date_from = dates[0]
            transactionFilter?.date_to = dates[1]
            bindingView.includeCreatedAt.hasSubtitle = true
            bindingView.includeCreatedAt.subTitle =
                    DateUtils.apiToDisplayDateFormat(dates[0]) + " - " + DateUtils.apiToDisplayDateFormat(
                            dates[1]
                    )
        }
        datePicker.show(parentFragmentManager, datePicker.toString())
    }

    companion object {
        const val ARG_TRANSACTION_FILTER_TITLE = "arg_transaction_filter_title"
        const val ARG_TRANSACTION_FILTER_DATA_LIST = "arg_transaction_filter_data_list"
        const val ARG_TRANSACTION_FILTER_OBJECT = "arg_transaction_filter_object"
        const val RESULT_TRANSACTION_FILTER = "result_transaction_filter"
    }
}
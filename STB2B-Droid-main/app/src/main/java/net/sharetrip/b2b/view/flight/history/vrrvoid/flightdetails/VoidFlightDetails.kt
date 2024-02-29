package net.sharetrip.b2b.view.flight.history.vrrvoid.flightdetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.sharetrip.b2b.databinding.VrrFlightDetailsBinding
import net.sharetrip.b2b.util.DateUtil
import net.sharetrip.b2b.view.flight.history.refund.RefundVoidSharedViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateDisplayFromApiWithAMPM
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiWithHalfYear
import java.text.ParseException
import java.util.Calendar

class VoidFlightDetails : BottomSheetDialogFragment() {

    private lateinit var bindingView: VrrFlightDetailsBinding

    private val sharedViewModel: RefundVoidSharedViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingView = VrrFlightDetailsBinding.inflate(layoutInflater, container, false)

        if (sharedViewModel.flightHistory?.segments != null) {
            val flight = sharedViewModel.flightHistory?.flight?.get(0)
            val segments = sharedViewModel.flightHistory?.segments
            bindingView.locationFrom.text = segments?.get(0)?.segment?.get(0)?.originCode
            bindingView.locationTo.text = segments?.get(0)?.segment?.get(0)?.destinationCode
            bindingView.flightNo.text = segments?.get(0)?.segment?.get(0)?.flightNumber
            bindingView.airportFrom.text = flight?.originName?.airport
            bindingView.airportTo.text = flight?.destinationName?.airport
            bindingView.airlineImg.load(segments?.get(0)?.segment?.get(0)?.logo)
            bindingView.airlineName.text = segments?.get(0)?.segment?.get(0)?.airlines?.shortName
            bindingView.journeyDayTime.text =
                segments?.get(0)?.segment?.get(0)?.arrivalDateTime?.getDateTime()
                    ?.parseDateDisplayFromApiWithAMPM()
            bindingView.date.text =
                getWeekDay(segments?.get(0)?.segment?.get(0)?.arrivalDateTime?.date) + ", " + (segments?.get(
                    0
                )?.segment?.get(0)?.arrivalDateTime?.date?.parseDateForDisplayFromApiWithHalfYear())
        }
        bindingView.crossBtn.setOnClickListener{
            dismiss()
        }

        return bindingView.root
    }

    fun getWeekDay(dateString: String?): String {
        val calendar = Calendar.getInstance()
        try {
            calendar.timeInMillis = DateUtil.parseDateToMillisecond(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val index = calendar[Calendar.DAY_OF_WEEK]
        return DateUtil.days[index - 1]
    }
}
package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightlist

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemFlightReissueBinding
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.Segment

class ReissueItemFlightView : RelativeLayout {

    lateinit var bindingView: ItemFlightReissueBinding

    constructor(context: Context?) : super(context) {
        initUi()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initUi()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initUi()
    }

    private fun initUi() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        bindingView = DataBindingUtil.inflate(inflater, R.layout.item_flight_reissue, this, true)
    }

    fun setItemFlight(mFlight: Segment) {
        bindingView.departureAirlineCodeTextView.text = mFlight.originName.code
        bindingView.departureTimeTextView.text = mFlight.departureDateTime.time
        bindingView.durationTextView.text = mFlight.duration

        val mStopsCount = mFlight.transits.size
        bindingView.stopTextView.text = mStopsCount.toString()

        if (mStopsCount <= 0) {
            bindingView.flightStopCount.text = "NonStop"
        } else {
            bindingView.flightStopCount.text = "$mStopsCount Stop(s)"
        }
        bindingView.airlineNameTextView.text = mFlight.airlines.shortName
        bindingView.arrivalAirlineCodeTextView.text = mFlight.destinationName.code
        bindingView.arrivalTimeTextView.text = mFlight.arrivalDateTime.time

        Glide.with(bindingView.flightLogo.context)
            .load(mFlight.logo)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(bindingView.flightLogo)
        if (mFlight.hiddenStops) {
            bindingView.relativeTechnicalStoppage.visibility = View.VISIBLE
        }
    }
}

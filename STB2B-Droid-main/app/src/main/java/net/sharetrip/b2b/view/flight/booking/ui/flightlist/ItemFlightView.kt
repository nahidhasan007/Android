package net.sharetrip.b2b.view.flight.booking.ui.flightlist

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import coil.load
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemFlightBinding
import net.sharetrip.b2b.view.flight.booking.model.Flight

class ItemFlightView : RelativeLayout {
    private lateinit var bindingView: ItemFlightBinding

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
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        bindingView = DataBindingUtil.inflate(inflater, R.layout.item_flight, this, true)
    }

    fun setItemFlight(mFlight: Flight) {
        bindingView.textViewDepartureAirline.text = mFlight.originName!!.code
        bindingView.textViewDepartureTime.text = mFlight.departureDateTime!!.time
        bindingView.textViewDuration.text = mFlight.duration

        val mStopsCount = mFlight.stop!!
        /*val mStopsCount = mFlight.stop
        bindingView.stopTextView.text = mStopsCount.toString()*/

        if (mStopsCount <= 0) {
            bindingView.textViewFlightStopCount.text = "NonStop"
        } else {
            bindingView.textViewFlightStopCount.text = "$mStopsCount Stop(s)"
        }
        bindingView.textViewAirlineName.text = mFlight.airlines!!.shortName
        bindingView.textViewArrivalAirlineCode.text = mFlight.destinationName!!.code
        bindingView.textViewArrivalTime.text = mFlight.arrivalDateTime!!.time

        if (mFlight.dayCount == 0) {
            bindingView.textViewExtraDay.visibility = View.INVISIBLE
        } else {
            bindingView.textViewExtraDay.visibility = View.VISIBLE
            bindingView.textViewExtraDay.text = "+" + mFlight.dayCount.toString()
        }

        /*Glide.with(bindingView.flightLogo.context)
                .load(mFlight.logo)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(bindingView.flightLogo)*/
        bindingView.imageViewAirlineLogo.load(mFlight.logo)

        if (mFlight.hiddenStops) {
            bindingView.layoutTechnicalStoppage.visibility = View.VISIBLE
        }
    }
}
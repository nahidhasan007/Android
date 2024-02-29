package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightlist

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemFlightsReissueBinding
import net.sharetrip.b2b.util.convertCurrencyToBengaliFormat
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.FlightX
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.Strings
import java.text.NumberFormat
import java.util.Locale


class FlightAdapter : PagingDataAdapter<FlightX, RecyclerView.ViewHolder>(FlightDiffCallback) {

    override fun onCreateViewHolder(mPrent: ViewGroup, mViewType: Int): RecyclerView.ViewHolder {
        val context = mPrent.context
        val inflater = LayoutInflater.from(context)
        val flightsBinding = DataBindingUtil.inflate<ItemFlightsReissueBinding>(
            inflater,
            R.layout.item_flights_reissue,
            mPrent,
            false
        )
        return FlightViewHolder(flightsBinding)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, mPosition: Int) {
        (viewHolder as FlightViewHolder).bindTo(getItem(mPosition))
    }

    inner class FlightViewHolder(mItemView: ItemFlightsReissueBinding) :
        RecyclerView.ViewHolder(mItemView.root) {
        private val bindingView: ItemFlightsReissueBinding = mItemView

        @SuppressLint("SetTextI18n")
        fun bindTo(flights: FlightX?) {
            val context = itemView.context
            val numberFormat = NumberFormat.getNumberInstance(Locale.US)
            val price =
                flights!!.currency + Strings.SPACE + flights.priceBreakdown.originPrice!!.toLong().convertCurrencyToBengaliFormat()
            bindingView.priceTextView.text = price
            bindingView.priceTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            val discountedPrice =
                flights.currency + Strings.SPACE + flights.priceBreakdown.promotionalAmount!!.toLong().convertCurrencyToBengaliFormat()
            bindingView.textViewDiscountedPrice.text = discountedPrice

            if (flights.priceBreakdown.promotionalDiscount == 0.0) {
                bindingView.priceTextView.visibility = GONE
            } else {
                bindingView.priceTextView.visibility = VISIBLE
                bindingView.priceTextView.textSize = 12f
            }

            val flightFlight = flights.segments
            val flightCount = flightFlight.size
            val childViewCount = bindingView.flightContainer.childCount
            val different = flightCount - childViewCount

            if (different == 0) {
                for (mIndex in 0 until flightCount) {
                    val mView = bindingView.flightContainer.getChildAt(mIndex) as ReissueItemFlightView
                    mView.setItemFlight(flightFlight[mIndex])
                }
            }

            if (different > 0) {
                var index = 0
                while (index < childViewCount) {
                    val mView = bindingView.flightContainer.getChildAt(index) as ReissueItemFlightView
                    mView.setItemFlight(flightFlight[index])
                    index++
                }
                while (index < flightCount) {
                    val mFlight = flightFlight[index]
                    val mFlightView = ReissueItemFlightView(context)
                    mFlightView.setItemFlight(mFlight)
                    bindingView.flightContainer.addView(mFlightView)
                    index++
                }
            } else if (different < 0) {
                var index = 0
                while (index < flightCount) {
                    val flightView = bindingView.flightContainer.getChildAt(index) as ReissueItemFlightView
                    flightView.setItemFlight(flightFlight[index])
                    index++
                }
                while (index < flightCount) {
                    bindingView.flightContainer.removeViewAt(index)
                    index++
                }
            }

            bindingView.textviewRefundableStatus.text = flights.isRefundable
        }
    }

    companion object {
        private val FlightDiffCallback: DiffUtil.ItemCallback<FlightX> =
            object : DiffUtil.ItemCallback<FlightX>() {
                override fun areItemsTheSame(oldItem: FlightX, newItem: FlightX): Boolean {
                    return if (oldItem.reissueSequenceCode != null)oldItem.reissueSequenceCode == newItem.reissueSequenceCode else oldItem.sequenceCode == newItem.sequenceCode
                }

                override fun areContentsTheSame(oldItem: FlightX, newItem: FlightX): Boolean {
                    return oldItem == newItem
                }
            }
    }
}


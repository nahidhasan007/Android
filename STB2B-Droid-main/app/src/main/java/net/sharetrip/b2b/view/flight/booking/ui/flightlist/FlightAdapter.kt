package net.sharetrip.b2b.view.flight.booking.ui.flightlist

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemFlightsBinding
import net.sharetrip.b2b.util.convertCurrencyToBengaliFormat
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.widgets.BaseRecyclerAdapter
import java.text.NumberFormat
import java.util.*

class FlightAdapter : BaseRecyclerAdapter<Flights, FlightAdapter.FlightsViewHolder>() {
    private val selectedItems = SparseBooleanArray()
    private var itemClick: OnItemClick? = null
    private var selectedIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): FlightsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FlightsViewHolder(ItemFlightsBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: FlightsViewHolder, position: Int) {
        holder.bindTo(getItem(position), position)
    }

    inner class FlightsViewHolder(val bindingView: ItemFlightsBinding) :
        RecyclerView.ViewHolder(bindingView.root), View.OnLongClickListener, View.OnClickListener {
        var itemPosition = 0
        private lateinit var flights: Flights

        init {
            bindingView.layoutContainer.setOnLongClickListener(this)
            bindingView.layoutContainer.setOnClickListener(this)
        }

        fun bindTo(mFlights: Flights, value: Int) {
            itemPosition = value
            flights = mFlights
            val mContext = itemView.context
            val numberFormat = NumberFormat.getNumberInstance(Locale.US)

            val clientPrice = mFlights.currency + " " + mFlights.originPrice.toLong().convertCurrencyToBengaliFormat()
            bindingView.textViewClientAmount.text = clientPrice

            val agentPrice = mFlights.currency + " " + mFlights.finalPrice.toLong().convertCurrencyToBengaliFormat()
            bindingView.textViewTotalAmount.text = agentPrice

            bindingView.textViewIsRefundable.text = mFlights.isRefundable

            bindingView.grossFare.setOnClickListener{ view ->
                view.findNavController().navigate(
                    R.id.action_flight_list_fragment_to_cardPriceBottomSheet,
                    bundleOf(AGENT_CLIENT_PRICE to mFlights)
                )
            }

            val mFlightFlight = mFlights.flight
            val mFlightCount = mFlightFlight!!.size
            val mChildViewCount = bindingView.layoutFlightContainer.childCount
            val mDifferent = mFlightCount - mChildViewCount
            if (mDifferent == 0) {
                for (mIndex in 0 until mFlightCount) {
                    val mView =
                        bindingView.layoutFlightContainer.getChildAt(mIndex) as ItemFlightView
                    mView.setItemFlight(mFlightFlight[mIndex])
                }
            }
            if (mDifferent > 0) {
                var mIndex = 0
                while (mIndex < mChildViewCount) {
                    val mView =
                        bindingView.layoutFlightContainer.getChildAt(mIndex) as ItemFlightView
                    mView.setItemFlight(mFlightFlight[mIndex])
                    mIndex++
                }
                while (mIndex < mFlightCount) {
                    val mFlight = mFlightFlight[mIndex]
                    val mFlightView = ItemFlightView(mContext)
                    mFlightView.setItemFlight(mFlight)
                    bindingView.layoutFlightContainer.addView(mFlightView)
                    mIndex++
                }
            } else if (mDifferent < 0) {
                var mIndex = 0
                while (mIndex < mFlightCount) {
                    val mView =
                        bindingView.layoutFlightContainer.getChildAt(mIndex) as ItemFlightView
                    mView.setItemFlight(mFlightFlight[mIndex])
                    mIndex++
                }
                while (mIndex < mFlightCount) {
                    bindingView.layoutFlightContainer.removeViewAt(mIndex)
                    mIndex++
                }
            }

            bindingView.layoutContainer.isActivated = selectedItems[itemPosition, false]
            toggleBackgroundColor(itemPosition)
        }

        private fun toggleBackgroundColor(position: Int) {
            if (selectedItems[position, false]) {
                bindingView.layoutContainer.setBackgroundResource(R.drawable.background_transparent_with_blue_stroke_bold)
                if (selectedIndex == position) selectedIndex = -1
            } else {
                bindingView.layoutContainer.background = null
                if (selectedIndex == position) selectedIndex = -1
            }
        }

        override fun onLongClick(v: View?): Boolean {
            if (itemClick == null)
                return false
            else
                itemClick?.onLongPress(v, flights, itemPosition)
            return true
        }

        override fun onClick(v: View?) {
            if (itemClick == null) return
            itemClick?.onItemClick(v, flights, itemPosition)
        }
    }

    fun toggleSelection(position: Int) {
        selectedIndex = position
        if (selectedItems[position, false]) {
            selectedItems.delete(position)
        } else {
            selectedItems.put(position, true)
        }
        notifyItemChanged(position)
    }

    fun selectedItemCount() = selectedItems.size()

    fun clearSelectedItem() {
        selectedItems.clear()
    }

    fun setItemClick(itemClick: OnItemClick?) {
        this.itemClick = itemClick
    }

    fun getSelectedItems(): List<Int> {
        val items: MutableList<Int> = ArrayList(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    interface OnItemClick {
        fun onItemClick(view: View?, flight: Flights?, position: Int)
        fun onLongPress(view: View?, flight: Flights?, position: Int)
    }
    companion object {
        const val AGENT_CLIENT_PRICE = "AGENT_CLIENT_PRICE"
    }
}

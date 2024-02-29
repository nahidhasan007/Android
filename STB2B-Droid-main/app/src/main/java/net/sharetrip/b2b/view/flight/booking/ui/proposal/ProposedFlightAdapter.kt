package net.sharetrip.b2b.view.flight.booking.ui.proposal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FooterProposalRecyclerBinding
import net.sharetrip.b2b.databinding.ItemFlightsBinding
import net.sharetrip.b2b.util.FLIGHT_PROPOSAL_FOOTER_VIEW
import net.sharetrip.b2b.util.FLIGHT_PROPOSAL_MAIN_VIEW
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.view.flight.booking.ui.flightlist.ItemFlightView
import java.text.NumberFormat
import java.util.*

class ProposedFlightAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var flightList: ArrayList<Flights> = arrayListOf()
    private var boxListener: OnCheckBoxListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int,
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            FLIGHT_PROPOSAL_MAIN_VIEW -> {
                val flightsBinding = DataBindingUtil.inflate<ItemFlightsBinding>(
                    inflater,
                    R.layout.item_flights, parent, false
                )
                return FlightsViewHolder(flightsBinding)
            }
            else -> {
                val flightsBinding = DataBindingUtil.inflate<FooterProposalRecyclerBinding>(
                    inflater,
                    R.layout.footer_proposal_recycler, parent, false
                )
                return FooterViewHolder(flightsBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FlightsViewHolder) {
            holder.bindTo(flightList[position])
        } else if (holder is FooterViewHolder) {
            holder.bindingView.checkBoxCancellationPolicy.setOnCheckedChangeListener { _, isChecked ->
                boxListener?.isCheckBoxClicked(isChecked)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == flightList.size) {
            FLIGHT_PROPOSAL_FOOTER_VIEW
        } else
            FLIGHT_PROPOSAL_MAIN_VIEW
    }

    override fun getItemCount() = flightList.size + 1

    inner class FlightsViewHolder(val bindingView: ItemFlightsBinding) :
        RecyclerView.ViewHolder(bindingView.root) {
        fun bindTo(mFlights: Flights) {
            val mContext = itemView.context
            val numberFormat = NumberFormat.getNumberInstance(Locale.US)

            val clientPrice = mFlights.currency + " " + numberFormat.format(mFlights.originPrice)
            bindingView.textViewClientAmount.text = clientPrice

            val agentPrice = mFlights.currency + " " + numberFormat.format(mFlights.finalPrice)
//            bindingView.textViewAgentAmount.text = agentPrice

            bindingView.textViewIsRefundable.text =
                if (mFlights.refundable) "Refundable" else "Non-Refundable"

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
        }
    }

    inner class FooterViewHolder(val bindingView: FooterProposalRecyclerBinding) :
        RecyclerView.ViewHolder(bindingView.root)

    fun setCheckboxListener(listener: OnCheckBoxListener) {
        this.boxListener = listener
    }

    fun addSelectedFlightList(list: List<Flights>) {
        this.flightList = list as ArrayList<Flights>
        notifyDataSetChanged()
    }

    fun resetListToOriginal(list: List<Flights>) {
        flightList.clear()
        flightList.addAll(list)
        notifyDataSetChanged()
    }

    fun getUpdatedFlightList(): List<Flights> {
        return flightList
    }

    fun updateFlightListWithProposal(
        selectedIndex: Int, selectedIndexValue: Double, totalTravellers: Int,
    ) {
        when (selectedIndex) {
            ProposalConstraintsEnum.FIXED_ON_BASE_FARE.constraintsValue ->
                updateFixedOnBaseFare(selectedIndexValue, totalTravellers)

            ProposalConstraintsEnum.PERCENTAGE_ON_BASE_FARE.constraintsValue ->
                updatePercentageOnBaseFare(selectedIndexValue)

            ProposalConstraintsEnum.PERCENTAGE_ON_TOTAL_TAX.constraintsValue ->
                updatePercentageOnTotalTax(selectedIndexValue, totalTravellers)

        }
    }

    private fun updateFixedOnBaseFare(selectedIndexValue: Double, totalTravellers: Int) {
        flightList.forEach { flight ->
            val perPersonChange = selectedIndexValue / totalTravellers
            var tempBase = 0.0
            var tempTaxes = 0.0
            flight.price?.forEach { it ->
                if (it.numberPaxes > 0) {
                    it.baseFare = it.baseFare.plus(perPersonChange)
                    tempBase = tempBase.plus(it.baseFare * it.numberPaxes)
                    tempTaxes = tempTaxes.plus(it.tax * it.numberPaxes)
                }
            }
            flight.originPrice = tempBase + tempTaxes
        }
        notifyDataSetChanged()
    }

    private fun updatePercentageOnBaseFare(selectedIndexValue: Double) {
        flightList.forEach { flight ->
            var totalBaseFare = 0.0
            var tempBase = 0.0
            var tempTaxes = 0.0
            flight.price?.forEach { it ->
                totalBaseFare += ((it.baseFare * selectedIndexValue) / 100)
                if (it.numberPaxes > 0) {
                    it.baseFare = it.baseFare + ((it.baseFare * selectedIndexValue) / 100)
                    tempBase += (it.baseFare * it.numberPaxes)
                    tempTaxes += (it.tax * it.numberPaxes)
                }
            }
            flight.originPrice = tempBase + tempTaxes
        }
        notifyDataSetChanged()
    }

    private fun updatePercentageOnTotalTax(selectedIndexValue: Double, totalTravellers: Int) {
        flightList.forEach { flight ->
            val totalTax = flight.getTotalTaxes()
            var tempBase = 0.0
            var tempTaxes = 0.0
            val totalTaxChange = totalTax * (selectedIndexValue / 100)
            val singleTaxChange = totalTaxChange / totalTravellers
            flight.price?.forEach { it ->
                if (it.numberPaxes > 0) {
                    it.tax += singleTaxChange
                    tempTaxes += it.tax * it.numberPaxes
                    tempBase += (it.baseFare * it.numberPaxes)
                }
            }
            flight.originPrice = tempBase + tempTaxes
        }
        notifyDataSetChanged()
    }

    interface OnCheckBoxListener {
        fun isCheckBoxClicked(isChecked: Boolean)
    }
}

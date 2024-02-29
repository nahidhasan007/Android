package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemDetailOfFlightReissueSegmentBinding
import net.sharetrip.b2b.databinding.ItemDetailOfReissueFlightBinding
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.Segment
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.SEGMENT
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.Transit
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.TRANSIT

class ReissueFlightDetailsAdapter(
    private val vm: ReissueFlightDetailsVm,
    private val items: List<Any>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun customItemViewType(position: Int): Int {
        return when (items[position]) {
            is Transit -> {
                TRANSIT
            }
            is Segment -> {
                SEGMENT
            }
            else -> {
                -1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mViewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        val customViewType = customItemViewType(viewType)

        mViewHolder = if(customViewType == SEGMENT) {
            val binding = DataBindingUtil.inflate<ItemDetailOfReissueFlightBinding>(
                inflater,
                R.layout.item_detail_of_reissue_flight,
                parent,
                false
            )
            FlightViewHolder(binding)
        } else {

            val binding = DataBindingUtil.inflate<ItemDetailOfFlightReissueSegmentBinding>(
                inflater,
                R.layout.item_detail_of_flight_reissue_segment,
                parent,
                false
            )
            SegmentViewHolder(binding)
        }

        return mViewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mCustomViewType = customItemViewType(position)
        if (SEGMENT == mCustomViewType) {
            if (holder is FlightViewHolder) {
                val item = items[position] as Segment?
                holder.mBinding.segment = item
                if (item != null) {
                    Glide.with(holder.mBinding.flightLogo.context)
                        .load(item.logo)
                        .apply(RequestOptions.bitmapTransform(CircleCrop()))
                        .into(holder.mBinding.flightLogo)
                }
                holder.mBinding.textSeeDetails.setOnClickListener {
                     vm.gotoSegmentFragment(position)
                }

                if (item != null) {
                    if (item.hiddenStops) {
                        holder.mBinding.relativeTechnicalStoppage.visibility = View.VISIBLE
                    } else {
                        holder.mBinding.relativeTechnicalStoppage.visibility = View.GONE
                    }
                }
            }
        } else if (TRANSIT == mCustomViewType) {
            val mSegment = items[position] as Transit?
            val mSegmentViewHolder = holder as SegmentViewHolder
            mSegmentViewHolder.binding.transit = mSegment
        }
    }

    internal inner class FlightViewHolder(val mBinding: ItemDetailOfReissueFlightBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    internal inner class SegmentViewHolder(val binding: ItemDetailOfFlightReissueSegmentBinding) :
        RecyclerView.ViewHolder(binding.root)
}
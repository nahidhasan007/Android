package net.sharetrip.b2b.view.flight.booking.ui.segment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ItemSegmentDetailBinding
import net.sharetrip.b2b.databinding.ItemSegmentFlightDetailBinding
import net.sharetrip.b2b.util.FLIGHT
import net.sharetrip.b2b.util.SEGMENT
import net.sharetrip.b2b.view.flight.booking.model.Flight
import net.sharetrip.b2b.view.flight.booking.model.Segment

class FlightSegmentAdapter(
    private val segments: List<Any>,
    private val flightSegmentVM: FlightSegmentVM,
    private val showToolbar: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)

        viewHolder = if (FLIGHT == viewType) {
            val binding = ItemSegmentFlightDetailBinding.inflate(inflater, parent, false)
            FlightViewHolder(binding)
        } else {
            val binding = ItemSegmentDetailBinding.inflate(inflater, parent, false)
            SegmentViewHolder(binding)
        }

        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        return when (segments[position]) {
            is Flight ->
                FLIGHT

            is Segment ->
                SEGMENT

            else -> -1
        }
    }

    override fun getItemCount(): Int = segments.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == FLIGHT) {
            if (holder is FlightViewHolder) {
                holder.flightDetailBinding.flight = segments[position] as Flight
                holder.flightDetailBinding.isFirstPosition = position == 0
                holder.flightDetailBinding.showToolbar = showToolbar

                holder.flightDetailBinding.imageClose.setOnClickListener {
                    flightSegmentVM.moveToBack.value = true
                }
            }

        } else if (holder.itemViewType == SEGMENT) {
            if (holder is SegmentViewHolder) {
                holder.segmentDetailBinding.segment = segments[position] as Segment
            }
        }
    }

    class FlightViewHolder(val flightDetailBinding: ItemSegmentFlightDetailBinding) :
        RecyclerView.ViewHolder(flightDetailBinding.root)

    class SegmentViewHolder(val segmentDetailBinding: ItemSegmentDetailBinding) :
        RecyclerView.ViewHolder(segmentDetailBinding.root)
}

package net.sharetrip.b2b.view.flight.history.vrrvoid.travellerselection

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.view.flight.booking.model.Segments

class FlightSegmentAdapter(
    val segments: List<Segments>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FlightSegmentVH.create(parent)
    }

    override fun getItemCount(): Int = segments.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FlightSegmentVH -> {
                holder.onBind(segments[position])
            }
        }
    }
}
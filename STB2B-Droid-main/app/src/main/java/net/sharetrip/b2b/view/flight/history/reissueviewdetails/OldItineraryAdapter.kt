package net.sharetrip.b2b.view.flight.history.reissueviewdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.OldItineraryItemBinding
import net.sharetrip.b2b.databinding.ReissueItineraryItemBinding
import net.sharetrip.b2b.view.flight.history.model.OldResultDetail

class OldItineraryAdapter(
    private val oldItineraryDetail: List<OldResultDetail> = listOf(),
) : RecyclerView.Adapter<OldItineraryVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OldItineraryVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<OldItineraryItemBinding>(
            inflater,
            R.layout.old_itinerary_item,
            parent,
            false
        )
        return OldItineraryVH(binding)
    }

    override fun onBindViewHolder(holder: OldItineraryVH, position: Int) {
        holder.onBind(oldItineraryDetail[position], position)
    }

    override fun getItemCount(): Int {
        return oldItineraryDetail.size
    }


}
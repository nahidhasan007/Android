package net.sharetrip.b2b.view.flight.history.reissueviewdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ReissueItineraryItemBinding
import net.sharetrip.b2b.view.flight.history.model.ReissueResultDetail

class ReissueItineraryAdapter(
    private val reissueResultDetail: List<ReissueResultDetail> = listOf(),
) : RecyclerView.Adapter<ItineraryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItineraryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ReissueItineraryItemBinding>(
            inflater,
            R.layout.reissue_itinerary_item,
            parent,
            false
        )
        return ItineraryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItineraryViewHolder, position: Int) {
        holder.onBind(reissueResultDetail[position], position)
    }

    override fun getItemCount(): Int {
        return reissueResultDetail.size
    }


}
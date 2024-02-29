package net.sharetrip.b2b.view.flight.history.reissue_change_date.manualquotation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.OldNewItineraryItemBinding
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.FlightX
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlight
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlightSearch
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueMultiCityModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.Segment

class ReissueManualAdapter(
    private val mList: List<Pair<ReissueFlight, Segment>>,
    private val totalChanges : Int = 0,
    private val flightNoBefore : String = ""
): RecyclerView.Adapter<ReissueManualVh>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReissueManualVh {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<OldNewItineraryItemBinding>(
            inflater,
            R.layout.old_new_itinerary_item,
            parent,
            false
        )
        return ReissueManualVh(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ReissueManualVh, position: Int) {
        holder.onBind(this.mList[position],totalChanges,flightNoBefore)
    }


}
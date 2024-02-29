package net.sharetrip.b2b.view.flight.history.reissue_change_date.travellerselection

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller

class TravellerItemAdapter(
    private var arrayList: List<ReissueTraveller>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return TravellerListViewHolder.create(parent)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TravellerListViewHolder -> {
                holder.travellerBind(traveller = arrayList[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


}

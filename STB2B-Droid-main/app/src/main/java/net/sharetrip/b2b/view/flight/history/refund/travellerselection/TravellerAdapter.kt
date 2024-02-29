package net.sharetrip.b2b.view.flight.history.refund.travellerselection

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.view.flight.history.model.Traveller
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller
import net.sharetrip.b2b.view.flight.history.reissue_change_date.travellerselection.TravellerSelectionViewHolder

class TravellerAdapter(
    private var arrayList: List<ReissueTraveller>,
    val checkboxClickItem: (position: Int, check: Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return TravellerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TravellerViewHolder -> {
                holder.onBind(
                    traveller = arrayList[position],
                    checkboxClickItem,
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    fun getDataSet() = arrayList

}
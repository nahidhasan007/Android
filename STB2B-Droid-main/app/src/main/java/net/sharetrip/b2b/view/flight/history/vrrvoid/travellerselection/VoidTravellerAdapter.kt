package net.sharetrip.b2b.view.flight.history.vrrvoid.travellerselection

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller

class VoidTravellerAdapter(
    private var arrayList: List<ReissueTraveller>,
    val checkboxClickItem: (position: Int, check: Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return VoidTravellerVH.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VoidTravellerVH -> {
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
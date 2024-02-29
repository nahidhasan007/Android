package net.sharetrip.b2b.view.flight.history.reissue_change_date.travellerselection

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller

class TravellerListAdapter(
    private var arrayList: List<ReissueTraveller>,
    private val isSelectAllTraveller: Boolean,
    val checkboxClickItem: (position: Int, check: Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return TravellerSelectionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TravellerSelectionViewHolder -> {
                holder.onBind(
                    traveller = arrayList[position],
                    checkboxClickItem,
                    isSelectAllTraveller
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    fun getDataSet() = arrayList

}

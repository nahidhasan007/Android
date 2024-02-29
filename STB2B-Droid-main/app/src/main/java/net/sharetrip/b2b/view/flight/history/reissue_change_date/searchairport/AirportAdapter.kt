package net.sharetrip.b2b.view.flight.history.reissue_change_date.searchairport

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemAirportBinding
import net.sharetrip.b2b.view.flight.booking.model.Airport
import java.util.ArrayList
import java.util.Locale

class AirportAdapter : BaseFilterRecyclerAdapter<Airport, AirportAdapter.AirportViewHolder?>() {

    private val mFilter: Filter = object : Filter() {
        override fun performFiltering(mConstraint: CharSequence): FilterResults {
            val mSearchText = mConstraint.toString().toLowerCase(Locale.getDefault())
            val mDataSet = dataSet
            val mFilteredAirports: MutableList<Airport?> = ArrayList()
            if (mSearchText.isNotEmpty()) {
                for (mAirport in mDataSet) {
                    if (mAirport.iata.toLowerCase(Locale.getDefault()).contains(mSearchText)
                        || mAirport.city.toLowerCase(Locale.getDefault()).contains(mSearchText)
                        || mAirport.name.toLowerCase(Locale.getDefault()).contains(mSearchText)) {
                        mFilteredAirports.add(mAirport)
                    }
                }
            }
            val mResults = FilterResults()
            mResults.values = mFilteredAirports
            return mResults
        }

        override fun publishResults(constraint: CharSequence?, mResults: FilterResults?) {
            try {
                mResults?.let {
                    val mFilteredAirports = it.values as List<Airport>
                    setFilterDataSet(mFilteredAirports)
                }
            } catch (e: Exception) {
            }
        }
    }
    override fun onCreateViewHolder(mParent: ViewGroup, mViewType: Int): AirportViewHolder {
        val mContext = mParent.context
        val inflater = LayoutInflater.from(mContext)
        val mView = DataBindingUtil.inflate<ItemAirportBinding>(inflater, R.layout.item_airport, mParent, false)
        return AirportViewHolder(mView)
    }

    override fun onBindViewHolder(mHolder: AirportViewHolder, mPosition: Int) {
        val mAirport = getItem(mPosition)
        mHolder.bindingView.codeTextView.text = mAirport.iata
        mHolder.bindingView.addressTextView.text = mAirport.name
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    inner class AirportViewHolder(val bindingView: ItemAirportBinding) : RecyclerView.ViewHolder(bindingView.root)
}


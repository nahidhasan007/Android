package net.sharetrip.b2b.view.flight.booking.ui.nationality

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemNationalityBinding
import net.sharetrip.b2b.view.flight.booking.model.Nationality
import java.util.*

class NationalityAdapter(private val listener: ItemClickListener) : RecyclerView.Adapter<NationalityAdapter.NationalityHolder>() {
    private val countryList = ArrayList<Nationality>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NationalityHolder {
        val countryView = DataBindingUtil.inflate<ItemNationalityBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_nationality,
            parent,
            false
        )
        return NationalityHolder(countryView)
    }

    override fun onBindViewHolder(holder: NationalityHolder, position: Int) {
        holder.itemNationalityBinding.nationality = countryList[position]
        holder.itemNationalityBinding.countryName.setOnClickListener {
            listener.onClickItem(countryList[position])
        }
    }

    override fun getItemCount() = countryList.size

    fun updateCodeList(countryCodeList: List<Nationality>) {
        this.countryList.clear()
        this.countryList.addAll(countryCodeList)
        notifyDataSetChanged()
    }

    inner class NationalityHolder(val itemNationalityBinding: ItemNationalityBinding) :
        RecyclerView.ViewHolder(itemNationalityBinding.root)
}

interface ItemClickListener {
    fun onClickItem(nationality: Nationality)
}
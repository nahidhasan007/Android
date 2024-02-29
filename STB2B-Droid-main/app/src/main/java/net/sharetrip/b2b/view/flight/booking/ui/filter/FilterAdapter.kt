package net.sharetrip.b2b.view.flight.booking.ui.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ItemFiltersBinding
import net.sharetrip.b2b.databinding.ItemScheduleBinding
import net.sharetrip.b2b.view.flight.booking.model.*
import java.util.*
import kotlin.collections.ArrayList

class FilterAdapter(private val dataList: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val codeSet: MutableSet<Any>
    private var isSelectAll = false

    val outBoundCodeList = ArrayList<String>()
    val returnCodeList = ArrayList<String>()
    val isRefundCodeList = ArrayList<Refundable>()


    val codeList: ArrayList<Any>
        get() = ArrayList(codeSet)

    val refundCodes: ArrayList<Refundable>
        get() = ArrayList(isRefundCodeList)

    val refundable: Boolean
        get() = true

    fun setSelection(isSelectAll: Boolean) {
        this.isSelectAll = isSelectAll
    }

    init {
        codeSet = HashSet()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        viewHolder = when (viewType) {

            FilterTypeEnum.TIME.filterCode -> {
                val binding = ItemScheduleBinding.inflate(inflater, parent, false)
                ScheduleViewHolder(binding)
            }

            else -> {
                val binding = ItemFiltersBinding.inflate(inflater, parent, false)
                CommonViewHolder(binding)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            FilterTypeEnum.TIME.filterCode ->
                onScheduleViewHolder(holder as ScheduleViewHolder, position)

            else -> {
                for (i in dataList.indices) {
                    if (isSelectAll) {
                        addCode(i)
                    } else {
                        removeCode(position)
                    }
                }
                bindCommonViewHolder(holder as CommonViewHolder, position)
            }
        }
    }

    private fun bindCommonViewHolder(holder: CommonViewHolder, position: Int) {
        if (dataList[position] is Stop) {
            holder.mBinding.textViewTitle.text = (dataList[position] as Stop).name
            holder.mBinding.textViewTitle.tag = (dataList[position] as Stop).id
        }

        if (dataList[position] is Refundable) {
            holder.mBinding.textViewTitle.text = (dataList[position] as Refundable).key
            holder.mBinding.textViewTitle.tag = (dataList[position] as Refundable).value
        }

        if (dataList[position] is Weight) {
            holder.mBinding.textViewTitle.text = (dataList[position] as Weight).note
            holder.mBinding.textViewTitle.tag = (dataList[position] as Weight).key
        }

        holder.mBinding.switchFilter.isChecked = isSelectAll
        holder.mBinding.switchFilter.setOnClickListener { view: View ->
            if ((view as SwitchCompat).isChecked) {
                addCode(position)
            } else {
                removeCode(position)
            }
            FilterConstrains.isSelectAll.setValue(codeSet.size == dataList.size)
        }
    }

    private fun onScheduleViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = dataList[position] as Schedule
        var count = 0
        for (outbound in schedule.outbound) {
            when (outbound.value) {
                "00 - 06" -> {
                    holder.mBinding.layoutMidnightDepart.visibility = View.VISIBLE
                    holder.mBinding.layoutMidnightDepart.tag = count
                }
                "06 - 12" -> {
                    holder.mBinding.layoutMorningDepart.visibility = View.VISIBLE
                    holder.mBinding.layoutMorningDepart.tag = count
                }
                "12 - 18" -> {
                    holder.mBinding.layoutNoonDepart.visibility = View.VISIBLE
                    holder.mBinding.layoutNoonDepart.tag = count
                }
                "18 - 24" -> {
                    holder.mBinding.layoutNightDepart.visibility = View.VISIBLE
                    holder.mBinding.layoutNightDepart.tag = count
                }
            }
            count++
        }
        count = 0
        for (aReturn in schedule.getReturn()) {
            when (aReturn.value) {
                "00 - 06" -> {
                    holder.mBinding.layoutMidnightReturn.visibility = View.VISIBLE
                    holder.mBinding.layoutMidnightReturn.tag = count
                }
                "06 - 12" -> {
                    holder.mBinding.layoutMorningReturn.visibility = View.VISIBLE
                    holder.mBinding.layoutMorningReturn.tag = count
                }
                "12 - 18" -> {
                    holder.mBinding.layoutNoonReturn.visibility = View.VISIBLE
                    holder.mBinding.layoutNoonReturn.tag = count
                }
                "18 - 24" -> {
                    holder.mBinding.layoutNightReturn.visibility = View.VISIBLE
                    holder.mBinding.layoutNightReturn.tag = count
                }
            }
            count++
        }

        holder.mBinding.layoutMidnightDepart.setOnClickListener {
            outBoundCodeList.clear()
            outBoundCodeList.add(schedule.outbound[(holder.mBinding.layoutMidnightDepart.tag as Int)].key)
            holder.mBinding.onwardTime = DayPart.midNight
        }

        holder.mBinding.layoutMorningDepart.setOnClickListener {
            outBoundCodeList.clear()
            outBoundCodeList.add(schedule.outbound[(holder.mBinding.layoutMorningDepart.tag as Int)].key)
            holder.mBinding.onwardTime = DayPart.morning
        }

        holder.mBinding.layoutNoonDepart.setOnClickListener {
            outBoundCodeList.clear()
            outBoundCodeList.add(schedule.outbound[(holder.mBinding.layoutNoonDepart.tag as Int)].key)
            holder.mBinding.onwardTime = DayPart.afternoon
        }

        holder.mBinding.layoutNightDepart.setOnClickListener {
            outBoundCodeList.clear()
            outBoundCodeList.add(schedule.outbound[(holder.mBinding.layoutNightDepart.tag as Int)].key)
            holder.mBinding.onwardTime = DayPart.evening
        }

        if (schedule.returnList.isEmpty()) {
            holder.mBinding.cardReturn.visibility = View.GONE
            holder.mBinding.returnText.visibility = View.GONE
        }

        holder.mBinding.layoutMidnightReturn.setOnClickListener {
            returnCodeList.clear()
            returnCodeList.add(schedule.getReturn()[(holder.mBinding.layoutMidnightReturn.tag as Int)].key)
            holder.mBinding.returnTime = DayPart.midNight
        }

        holder.mBinding.layoutMorningReturn.setOnClickListener {
            returnCodeList.clear()
            returnCodeList.add(schedule.getReturn()[(holder.mBinding.layoutMorningReturn.tag as Int)].key)
            holder.mBinding.returnTime = DayPart.morning
        }

        holder.mBinding.layoutNoonReturn.setOnClickListener {
            returnCodeList.clear()
            returnCodeList.add(schedule.getReturn()[(holder.mBinding.layoutNoonReturn.tag as Int)].key)
            holder.mBinding.returnTime = DayPart.afternoon
        }

        holder.mBinding.layoutNightReturn.setOnClickListener {
            returnCodeList.clear()
            returnCodeList.add(schedule.getReturn()[(holder.mBinding.layoutNightReturn.tag as Int)].key)
            holder.mBinding.returnTime = DayPart.evening
        }
    }

    private fun addCode(position: Int) {
        when {
            dataList[position] is Stop ->
                codeSet.add((dataList[position] as Stop).id!!)

            dataList[position] is Weight ->
                codeSet.add((dataList[position] as Weight).key!!)

            dataList[position] is Outbound ->
                codeSet.add((dataList[position] as Outbound).key)

            dataList[position] is Return ->
                codeSet.add((dataList[position] as Return).key)

            dataList[position] is Refundable -> {
                val data = dataList[position] as Refundable
                if (!isRefundCodeList.contains(data)) {
                    isRefundCodeList.add((dataList[position] as Refundable))
                }
            }
        }
    }

    private fun removeCode(position: Int) {
        when {
            dataList[position] is Stop ->
                codeSet.remove((dataList[position] as Stop).id)

            dataList[position] is Weight ->
                codeSet.remove((dataList[position] as Weight).key)

            dataList[position] is Outbound ->
                codeSet.remove((dataList[position] as Outbound).key)

            dataList[position] is Return ->
                codeSet.remove((dataList[position] as Return).key)

            dataList[position] is Refundable ->
                isRefundCodeList.remove((dataList[position] as Refundable))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            dataList[position] is Stop ->
                FilterTypeEnum.STOPS.filterCode

            dataList[position] is Refundable ->
                FilterTypeEnum.REFUNDABLE.filterCode

            dataList[position] is Schedule ->
                FilterTypeEnum.TIME.filterCode

            dataList[position] is Weight ->
                FilterTypeEnum.WEIGHT.filterCode

            else -> -1
        }
    }

    override fun getItemCount() = dataList.size

    inner class CommonViewHolder internal constructor(val mBinding: ItemFiltersBinding) :
        RecyclerView.ViewHolder(mBinding.root)

    inner class ScheduleViewHolder internal constructor(val mBinding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(mBinding.root)
}

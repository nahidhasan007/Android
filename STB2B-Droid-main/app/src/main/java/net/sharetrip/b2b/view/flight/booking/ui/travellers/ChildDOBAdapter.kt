package net.sharetrip.b2b.view.flight.booking.ui.travellers

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ItemBirthDateSelectionBinding
import net.sharetrip.b2b.util.DateUtils.getCalender
import net.sharetrip.b2b.util.DateUtils.stringToDate
import net.sharetrip.b2b.util.formatToTwoDigit
import net.sharetrip.b2b.view.flight.booking.model.ChildrenDOB
import java.util.*
import kotlin.collections.ArrayList

class ChildDOBAdapter(val flightdate: ArrayList<String>, val viewModel: TravellersVM) :
    RecyclerView.Adapter<ChildDOBAdapter.ChildDobHolder>() {

    private val dobList: ArrayList<ChildrenDOB> = arrayListOf()
    private val childAgeList: ArrayList<String> = arrayListOf()
    private val calendar = Calendar.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildDobHolder {
        val viewHolder = ItemBirthDateSelectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChildDobHolder(viewHolder)
    }

    override fun getItemCount(): Int = dobList.size

    override fun onBindViewHolder(holder: ChildDobHolder, position: Int) {
        holder.binding.childrenDob = dobList[position]


        holder.binding.textFieldBirthDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                holder.binding.textFieldBirthDate.context, { _, year, monthOfYear, dayOfMonth ->
                    val month = formatToTwoDigit(monthOfYear + 1)
                    val day = formatToTwoDigit(dayOfMonth)

                    val date = "$year-$month-$day"

                    holder.binding.textFieldBirthDate.setText(date)
                    dobList[position] =
                        ChildrenDOB(dobList[position].title, date)
                    childAgeList.add(date)

                    viewModel.travellers.childDobList.clear()
                    viewModel.travellers.childDobList.addAll(dobList)
                    viewModel.travellers.childAge.clear()
                    viewModel.travellers.childAge.addAll(childAgeList)
                }, getCalender().year, getCalender().month, getCalender().year
            )
            calendar.time = stringToDate(flightdate[0])!!
            calendar.add(Calendar.YEAR, -2)
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            calendar.add(Calendar.YEAR, -9)
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }
    }

    fun updateData(list: ArrayList<ChildrenDOB>) {
        dobList.clear()
        dobList.addAll(list)
        notifyDataSetChanged()
    }

    class ChildDobHolder(val binding: ItemBirthDateSelectionBinding) :
        RecyclerView.ViewHolder(binding.root)
}

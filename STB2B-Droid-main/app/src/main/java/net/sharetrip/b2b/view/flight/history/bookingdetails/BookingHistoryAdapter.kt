package net.sharetrip.b2b.view.flight.history.bookingdetails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ItemBookingHistoryDetailsBinding
import net.sharetrip.b2b.util.convertCurrencyToBengaliFormat
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.model.ModifyHistory
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiWithHalfYear
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiWithSlash
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class BookingHistoryAdapter(
    var modifyHistory: List<ModifyHistory>,
    private var historyResponse: FlightHistory,
    val onClickListener: ClickViewDetails
) : RecyclerView.Adapter<BookingHistoryAdapter.BookingHistoryViewHolder>() {
    val listSize = modifyHistory.size

    private val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingHistoryViewHolder {
        val binding = ItemBookingHistoryDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookingHistoryViewHolder(binding)
    }


    override fun onBindViewHolder(holder: BookingHistoryViewHolder, position: Int) {
        holder.bind(modifyHistory[position], historyResponse, position, onClickListener, listSize)
    }


    class BookingHistoryViewHolder(val binding: ItemBookingHistoryDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val numberFormat = NumberFormat.getNumberInstance(Locale.US)

        @SuppressLint("SetTextI18n")
        fun bind(
            modifyHistory: ModifyHistory,
            historyResponse: FlightHistory,
            position: Int,
            onClickListener: ClickViewDetails,
            listSize: Int
        ) {
            if (position == listSize - 1) {
                binding.BottomItemLineView.visibility = View.GONE
            }
            if (historyResponse.searchParams?.tripType == "Return") {
                binding.tripType.text = "Round Trip"
            } else {
                binding.tripType.text = historyResponse.searchParams?.tripType
            }
            if (modifyHistory.modificationType == "REFUND" || modifyHistory.modificationType == "VOID") {
                binding.arrivalDateTime.visibility = View.GONE
                binding.passengerIcon.visibility = View.GONE
                binding.dateImageView2.visibility = View.GONE
                binding.departureDateTime.visibility = View.GONE
                binding.paidTextView.visibility = View.GONE
                binding.issueDate.text =
                    ModifyHistoryResource.MODIFIEDON.status + " ${modifyHistory.modifiedAt?.parseDateForDisplayFromApiWithHalfYear()}"
                binding.resPnrNumber.visibility = View.GONE
                binding.pnrNumber.visibility = View.GONE
                binding.priceTextView.text =
                    ModifyHistoryResource.TAKAVOIDREFUND.status + modifyHistory.priceBreakdown.totalAmount.toLong()
                        .convertCurrencyToBengaliFormat()
            }
            if (modifyHistory.modificationType == "REISSUE") {
                val departureTime =
                    modifyHistory.reissueResultDetails?.getOrNull(position)?.departureDateTime?.let {
                        formatTime(
                            it.time
                        )
                    }
                binding.departureDateTime.text =
                    (modifyHistory.reissueResultDetails?.getOrNull(position)?.departureDateTime?.date?.parseDateForDisplayFromApiWithSlash()) + ", ${departureTime}"

                val arrivalTime =
                    modifyHistory.reissueResultDetails?.getOrNull(position)?.arrivalDateTime?.let {
                        formatTime(
                            it.time
                        )
                    }
                binding.arrivalDateTime.text =
                    (modifyHistory.reissueResultDetails?.getOrNull(position)?.arrivalDateTime?.date?.parseDateForDisplayFromApiWithSlash()) + ", ${arrivalTime}"

                binding.paidTextView.text = modifyHistory.paymentStatus
                binding.pnrNumber.text = AirlinePNR.AIRLINEPNR.status + modifyHistory.airlineResCode
                binding.resPnrNumber.text =
                    AirlineResPNR.AIRLINERESPNR.status + modifyHistory.pnrCode
                binding.issueDate.text =
                    ModifyHistoryResource.ISSUEDON.status + " ${modifyHistory.issuedAt?.parseDateForDisplayFromApiWithHalfYear()}"

                binding.priceTextView.text =
                    ModifyHistoryResource.TAKA.status + modifyHistory.priceBreakdown.totalAmount.toLong()
                        .convertCurrencyToBengaliFormat()
            }
            binding.cabinClass.text = historyResponse.searchParams?.class_


            binding.bookingID.text = ModifyHistoryResource.ID.status + modifyHistory.bookingCode
            binding.clientPriceTextView.text =
                ModifyHistoryResource.CLIENT.status + ModifyHistoryResource.TAKA.status + historyResponse.actualAmount?.toLong()
                    ?.convertCurrencyToBengaliFormat()
//            binding.pnrNumber.text = AirlinePNR.AIRLINEPNR.status + modifyHistory.airlineResCode
//            binding.resPnrNumber.text = AirlineResPNR.AIRLINERESPNR.status + modifyHistory.pnrCode
//            binding.issueDate.text =
//                ModifyHistoryResource.ISSUEDON.status + " ${modifyHistory.issuedAt?.parseDateForDisplayFromApiWithHalfYear()}"
            binding.automationType.text = modifyHistory.automationType
//            binding.statusLabel.text = modifyHistory.status
            binding.modificationType.text = modifyHistory.modificationType
            binding.flightCode.text = historyResponse.flightCode
            binding.itemViewDetails.setOnClickListener() {
                onClickListener.onClickViewDetails(modifyHistory, historyResponse)
            }

        }

        private fun formatTime(inputTime: String): String {
            try {
                val inputFormat = SimpleDateFormat("HH:mm", Locale.US)
                val outputFormat = SimpleDateFormat("hh:mm a", Locale.US)
                val date = inputFormat.parse(inputTime)
                return outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }


    }

    enum class AirlinePNR(val status: String) {
        PROCESSING("Airline PNR : Processing"),
        AIRLINEPNR("Airline PNR : ")
    }

    enum class AirlineResPNR(val status: String) {
        PROCESSING("Res.PNR : Processing"),
        AIRLINERESPNR("Res.PNR : ")
    }

    enum class ModifyHistoryResource(val status: String) {
        ID("ID : "),
        Bdt("+ BDT "),
        ISSUEDON("Issued On"),
        MODIFIEDON("Modified On "),
        TRAVELLERS(" Travellers"),
        TAKA("+৳ "),
        TAKAVOIDREFUND("-৳ "),
        CLIENT("Client: ")


    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<ModifyHistory>) {
        differ.submitList(list)
    }


    companion object {
        private val differCallBack = object : DiffUtil.ItemCallback<ModifyHistory>() {
            override fun areItemsTheSame(
                oldItem: ModifyHistory,
                newItem: ModifyHistory
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ModifyHistory,
                newItem: ModifyHistory
            ): Boolean {
                return oldItem.bookingCode == newItem.bookingCode
            }

        }
    }

}
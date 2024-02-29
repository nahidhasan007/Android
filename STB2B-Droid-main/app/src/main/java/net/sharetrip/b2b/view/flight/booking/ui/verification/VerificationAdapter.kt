package net.sharetrip.b2b.view.flight.booking.ui.verification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemVerificationBinding
import net.sharetrip.b2b.view.flight.booking.model.Passenger
import net.sharetrip.b2b.view.flight.booking.ui.passenger.IMAGE_URL

class VerificationAdapter(private val isDomestic: Boolean) :
    ListAdapter<Passenger, VerificationAdapter.VerifyViewHolder>(PassengerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerifyViewHolder =
        VerifyViewHolder(
            ItemVerificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: VerifyViewHolder, position: Int) {
        val passenger = getItem(position)
        holder.itemVerification.passenger = passenger
        holder.itemVerification.isDomestic = isDomestic

        holder.itemVerification.imageViewVisaCopy.setOnClickListener { view ->
            if (passenger.visaCopy.isNotEmpty()) {
                openImagePreview(view, passenger.visaCopy)
            }
        }

        holder.itemVerification.imageViewPassportCopy.setOnClickListener { view ->
            if (passenger.passportCopy.isNotEmpty()) {
                openImagePreview(view, passenger.passportCopy)
            }
        }
    }

    private fun openImagePreview(view: View, url: String) {
        if (url.isNotEmpty()) {
            val bundle = bundleOf(IMAGE_URL to url)
            view.findNavController().navigate(R.id.action_verification_to_image_preview, bundle)
        }
    }

    inner class VerifyViewHolder(val itemVerification: ItemVerificationBinding) :
        RecyclerView.ViewHolder(itemVerification.root)

    private class PassengerDiffCallback : DiffUtil.ItemCallback<Passenger>() {
        override fun areItemsTheSame(oldItem: Passenger, newItem: Passenger): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Passenger, newItem: Passenger): Boolean {
            return oldItem == newItem
        }
    }
}

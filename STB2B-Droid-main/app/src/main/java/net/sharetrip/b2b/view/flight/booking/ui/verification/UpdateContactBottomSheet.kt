package net.sharetrip.b2b.view.flight.booking.ui.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.sharetrip.b2b.databinding.BottomSheetUpdateContactBinding
import net.sharetrip.b2b.util.setNavigationResult
import net.sharetrip.b2b.view.flight.booking.model.ContactInfo

class UpdateContactBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val bindingView = BottomSheetUpdateContactBinding.inflate(inflater, container, false)

        val contact: ContactInfo = arguments?.getParcelable(CONTACT_VERIFICATION) ?: ContactInfo()
        bindingView.contact = contact

        bindingView.buttonUpdate.setOnClickListener {
            val contactInfo = ContactInfo(
                bindingView.editTextPhoneNumber.text.toString(),
                bindingView.editTextEmail.text.toString(),
            )
            dismiss()
            setNavigationResult(contactInfo, KEY_CONTACT)
        }

        return bindingView.root
    }
}

const val CONTACT_VERIFICATION = "contact_verification"
const val KEY_CONTACT = "key_contact"

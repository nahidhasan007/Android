package net.sharetrip.b2b.view.flight.booking.ui.maildetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import net.sharetrip.b2b.databinding.FragmentEmailDetailsBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator

class MailDetailsFragment : Fragment() {
    private val mailDetailsFragmentArgs by navArgs<MailDetailsFragmentArgs>()

    private val VM: MailDetailsVM by lazy {
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        MailDetailsVMFactory(MailDetailsRepo(endPoint),
            mailDetailsFragmentArgs.flightSearch,
            mailDetailsFragmentArgs.updatedFlightList.toList(),
            mailDetailsFragmentArgs.cancellationPolicy).create(MailDetailsVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val bindingView = FragmentEmailDetailsBinding.inflate(inflater, container, false)
        bindingView.mailDetailsViewModel = VM

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        VM.showMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        VM.isEmailDiscard.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        return bindingView.root
    }
}
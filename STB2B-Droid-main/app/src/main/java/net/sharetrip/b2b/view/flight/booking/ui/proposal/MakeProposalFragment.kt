package net.sharetrip.b2b.view.flight.booking.ui.proposal

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentFlightProposalBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.network.Status
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.STORAGE_PERMISSION
import net.sharetrip.b2b.util.flightListToString
import net.sharetrip.b2b.util.toFlightList
import net.sharetrip.b2b.view.flight.booking.model.FlightDetails
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.view.flight.booking.ui.proposal.IncreaseProposalBottomSheet.Companion.FLIGHT_PROPOSAL_CONSTRAINTS
import net.sharetrip.b2b.view.flight.booking.ui.proposal.IncreaseProposalBottomSheet.Companion.SELECTED_ITEM_INDEX
import net.sharetrip.b2b.view.flight.booking.ui.proposal.IncreaseProposalBottomSheet.Companion.SELECTED_ITEM_VALUE

class MakeProposalFragment : Fragment(), ProposedFlightAdapter.OnCheckBoxListener {
    private val makeProposalFragmentArgs by navArgs<MakeProposalFragmentArgs>()
    private val proposedFlightAdapter = ProposedFlightAdapter()
    lateinit var bindingView: FragmentFlightProposalBinding
    private lateinit var originalFlightList: List<Flights>
    private lateinit var anotherFlightList: List<Flights>
    lateinit var flightSearch: FlightSearch
    private var cancellationPolicy = false
    private var isBackStackDataNeeded = false

    private val proposalViewModel: FlightProposalViewModel by lazy {
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        FlightProposalVMFactory(FlightProposalRepo(endPoint)).create(FlightProposalViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flightSearch = makeProposalFragmentArgs.flightSearch
        val temporaryList = ArrayList(makeProposalFragmentArgs.selectedFlightList.asList())
        originalFlightList = toFlightList(flightListToString(temporaryList))!!
        anotherFlightList = toFlightList(flightListToString(temporaryList))!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bindingView = FragmentFlightProposalBinding.inflate(inflater, container, false)
        bindingView.viewModel = proposalViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner

        bindingView.recyclerSelectedFlights.adapter = proposedFlightAdapter
        proposedFlightAdapter.addSelectedFlightList(originalFlightList)
        proposedFlightAdapter.setCheckboxListener(this)

        proposalViewModel.dataState.observe(requireActivity(), EventObserver {
            when (it.status) {
                Status.RUNNING -> showLoadingStatus()

                Status.FAILED -> showFailedStatus()

                Status.SUCCESS -> {
                    showDataLoadedStatus()
                }
            }
        })

        proposalViewModel.fileMap.observe(requireActivity(), EventObserver {
            startDownloading(it)
        })

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.buttonDownload.setOnClickListener {
            checkPermission()
        }

        bindingView.buttonPrepareMail.setOnClickListener {
            val action = MakeProposalFragmentDirections.actionMakeProposalFragmentToMailDetailsDest(
                flightSearch,
                proposedFlightAdapter.getUpdatedFlightList().toTypedArray(),
                cancellationPolicy
            )
            view?.findNavController()?.navigate(action)
        }

        bindingView.buttonIncreaseDecrease.setOnClickListener {
            isBackStackDataNeeded = true
            val action =
                MakeProposalFragmentDirections.actionMakeProposalFragmentToIncreaseProposalBottomSheet()
            view?.findNavController()?.navigate(action)
        }

        bindingView.imageViewReset.setOnClickListener {
            val temporaryList = ArrayList(makeProposalFragmentArgs.selectedFlightList.asList())
            val flightList = toFlightList(flightListToString(temporaryList))!!
            proposedFlightAdapter.resetListToOriginal(flightList)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(
            FLIGHT_PROPOSAL_CONSTRAINTS
        )?.observe(viewLifecycleOwner) { result ->
            val selectedIndex = result.getInt(SELECTED_ITEM_INDEX, -1)
            val selectedIndexValue = result.getDouble(SELECTED_ITEM_VALUE, 0.0)
            if (selectedIndex != -1 && selectedIndexValue != 0.0 && isBackStackDataNeeded) {
                proposedFlightAdapter.updateFlightListWithProposal(
                    selectedIndex,
                    selectedIndexValue, flightSearch.travellersInfo.totalTravellers()
                )
                isBackStackDataNeeded = false
            }
        }

        return bindingView.root
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
            && requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION
            )
        } else {
            proposalViewModel.getDownloadLinkFromServer(getFlightRequest())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>, grantResults: IntArray,
    ) {
        when (requestCode) {
            STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proposalViewModel.getDownloadLinkFromServer(getFlightRequest())
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun startDownloading(map: Map<String, String>) {
        val manager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        for (keyValue in map.keys) {
            val url = map[keyValue]
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            request.setDescription(keyValue)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            manager.enqueue(request)
        }
    }

    private fun getFlightRequest(): FlightDetails {
        val flightDetails = FlightDetails()
        flightDetails.searchId = flightSearch.searchId
        flightDetails.flightData = proposedFlightAdapter.getUpdatedFlightList()
        return flightDetails
    }

    private fun showDataLoadedStatus() {
        bindingView.layoutLoading.visibility = View.GONE
    }

    private fun showFailedStatus() {
        bindingView.layoutLoading.visibility = View.GONE
    }

    private fun showLoadingStatus() {
        bindingView.layoutLoading.visibility = View.VISIBLE
    }

    override fun isCheckBoxClicked(isChecked: Boolean) {
        cancellationPolicy = isChecked
    }
}

package net.sharetrip.b2b.view.flight.history.reissueviewdetails

import android.Manifest
import android.app.Dialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.ReissuePassengerDetailsBinding
import net.sharetrip.b2b.databinding.ReissueViewDetailsFragmentBinding
import net.sharetrip.b2b.databinding.ViewPriceDetailsBinding
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.util.STORAGE_PERMISSION
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.model.ModifyHistory
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ReissueChangeDateSharedViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails.ReissueFlightDetailsFragment
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails.ReissueFlightDetailsFragment.Companion.MODIFY_HISTORY
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.getParcelableCompat
import net.sharetrip.b2b.view.flight.history.reissuepassengerdetails.ReissuePassengerAdapter
import net.sharetrip.b2b.widgets.BaseFragment

class ReissueDetailsFragment : BaseFragment<ReissueViewDetailsFragmentBinding>() {
    override fun layoutId(): Int = R.layout.reissue_view_details_fragment
    override fun getViewModel(): BaseViewModel? = null

    private val sharedViewModel: ReissueChangeDateSharedViewModel by activityViewModels()
    private var onDownloadComplete: BroadcastReceiver? = null
    var downloadId: Long? = null
    var manager: DownloadManager? = null
    private lateinit var modifyHistory: ModifyHistory

    private val reissueApiService by lazy {
        ServiceGenerator.createService(ReissueApiService::class.java)
    }
    private val viewModel: ReissueDetailsVm by lazy {
        ViewModelProvider(
            this,
            ReissueDetailsVMF(
                modifyHistory,
                AppSharedPreference.accessToken
            )
        )[ReissueDetailsVm::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modifyHistory = requireArguments().getParcelable(MODIFY_HISTORY)!!
    }

    override fun initOnCreateView() {
        val modifyHistory: ModifyHistory =
            requireArguments().getParcelableCompat(
                MODIFY_HISTORY,
                ModifyHistory::class.java
            )!!
        val flightHistory: FlightHistory =
            requireArguments().getParcelableCompat(
                ReissueFlightDetailsFragment.HISTORY_RESPONSE,
                FlightHistory::class.java
            )!!
        bindingView.apply {
            bindingView.flightHistory = flightHistory
            bindingView.modifyHistory = modifyHistory
            val newItineraryAdapter =
                modifyHistory.reissueResultDetails?.let {
                    ReissueItineraryAdapter(
                        it
                    )
                }
            val oldItineraryAdapter = modifyHistory.oldResultDetails?.let {
                OldItineraryAdapter(
                    it
                )
            }
            bindingView.newItineraryRecyclerView.adapter = newItineraryAdapter
            bindingView.newItineraryRecyclerView.addItemDecoration(ItineraryItemDecoration())
            bindingView.oldItineraryRecyclerView.adapter = oldItineraryAdapter
            bindingView.oldItineraryRecyclerView.addItemDecoration(ItineraryItemDecoration())
        }

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        bindingView.includePriceDetails.root.setOnClickListener {
            findNavController().navigate(
                R.id.action_reissueDetailsFragment_to_reissuePricingInformation,
                bundleOf(PRICE_BREAKDOWN to modifyHistory.priceBreakdown)
            )
        }

        bindingView.includePassengerDetails.root.setOnClickListener {
            findNavController().navigate(
                R.id.action_reissueDetailsFragment_to_reissuePassengerInfo,
                bundleOf(TRAVELLERS to modifyHistory)
            )
        }
        viewModel.reissueCode = modifyHistory.reissueCode?.toString().toString()
        viewModel.priceBreakdown = modifyHistory.priceBreakdown
//        viewModel.downloadVoucher(modifyHistory.reissueCode, modifyHistory.priceBreakdown)
        bindingView.includeActionButtons.btnDownloadVoucher.setOnClickListener {
            checkPermission()
        }

        onDownloadComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (downloadId == id) {
                    viewModel.dataLoading.set(false)
                    val query = DownloadManager.Query()
                    query.setFilterById(downloadId!!)
                    val cursor: Cursor = manager?.query(query)!!

                    if (cursor.moveToFirst()) {
                        when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                            DownloadManager.STATUS_SUCCESSFUL -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.download_successfull),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            DownloadManager.STATUS_FAILED -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.download_unsccessfull),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
//        viewModel.isVoucherDownLoaded.observe(viewLifecycleOwner) {
//            if(it){
//                Toast.makeText(requireContext(), "Voucher Downloaded Successfully", Toast.LENGTH_SHORT).show()
//            }
//        }

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
            startDownloading(viewModel.getUri())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>, grantResults: IntArray,
    ) {
        when (requestCode) {
            STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownloading(viewModel.getUri())
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

    private fun startDownloading(url: String) {
        viewModel.dataLoading.set(true)
        manager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setTitle("${sharedViewModel.bookingCode} voucher")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${sharedViewModel.bookingCode} voucher.pdf"
        )
        downloadId = manager?.enqueue(request)
    }



    companion object {
        const val PRICE_BREAKDOWN = "PRICE_BREAKDOWN"
        const val TRAVELLERS = "TRAVELLERS"
    }
}
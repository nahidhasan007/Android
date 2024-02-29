package net.sharetrip.b2b.view.flight.history.downloadvowcher

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentDownloadVoucherBinding
import net.sharetrip.b2b.util.STORAGE_PERMISSION
import net.sharetrip.b2b.view.flight.booking.model.PriceBreakdown
import net.sharetrip.b2b.view.flight.history.historydetails.FlightHistoryDetailsFragment
import net.sharetrip.b2b.view.flight.history.model.FlightHistory

class DownloadVoucherFragment : Fragment() {
    var flightHistory: FlightHistory? = null
    private val viewModel: DownloadVoucherVM by viewModels()
    private var onDownloadComplete: BroadcastReceiver? = null
    var downloadId: Long? = null
    var manager: DownloadManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentDownloadVoucherBinding.inflate(layoutInflater, container, false)
        flightHistory = arguments?.getParcelable(FlightHistoryDetailsFragment.ARG_FLIGHT_HISTORY)
        val adapter = CustomerPricingAdapter(viewModel)
        bindingView.viewModel = viewModel

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        bindingView.recyclerCustomPricing.adapter = adapter
        val prices: ArrayList<PriceBreakdown> = arrayListOf()

        flightHistory?.priceBreakdown?.details?.forEach { price ->
            flightHistory?.travellers?.forEach {
                if (it.travellerType?.contains(price.type, true)!!) {
                    prices.add(price)
                }
            }
        }

        adapter.submitList(prices.distinct())
        flightHistory?.let {
            viewModel.setVoucherDetails(it)
        }

        bindingView.btnDownloadVoucher.setOnClickListener {
            checkPermission()
        }

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
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

        return bindingView.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(
            onDownloadComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(onDownloadComplete)
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
        request.setTitle("${flightHistory?.bookingCode} voucher")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${flightHistory?.bookingCode} voucher.pdf"
        )
        downloadId = manager?.enqueue(request)
    }
}
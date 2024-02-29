package net.sharetrip.b2b.view.topup.ui.paymentrequest

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentCreatePaymentRequestBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.PaymentEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.DateUtils
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.GALLERY_PERMISSION
import net.sharetrip.b2b.util.formatToTwoDigit
import net.sharetrip.b2b.util.getFilePath
import net.sharetrip.b2b.util.setIndicatorPosition
import net.sharetrip.b2b.view.flight.booking.ui.ListItemClickListener
import net.sharetrip.b2b.view.topup.model.GateWay
import net.sharetrip.b2b.view.topup.ui.topup.GateWayAdapter

class PaymentRequestFragment : Fragment(), ListItemClickListener<GateWay> {
    lateinit var adapter: GateWayAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var isCountTotalVisible = true
    private var dotsCount = 0
    private var totalVisibleItem = 0
    private lateinit var dots: Array<ImageView?>

    private val viewModel by lazy {
        val paymentEndPoint = ServiceGenerator.createService(PaymentEndPoint::class.java)
        val flightEndPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        ViewModelProvider(
            this,
            PaymentRequestVMFactory(PaymentRequestRepo(paymentEndPoint, flightEndPoint))
        ).get(
            PaymentRequestVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView =
            FragmentCreatePaymentRequestBinding.inflate(layoutInflater, container, false)

        bindingView.lifecycleOwner = viewLifecycleOwner

        bindingView.viewModel = viewModel
        adapter = GateWayAdapter(this)

        layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        bindingView.includePaymentMethod.recyclerPaymentMethod.layoutManager = layoutManager
        bindingView.includePaymentMethod.recyclerPaymentMethod.adapter = adapter

        viewModel.gateWays.observe(viewLifecycleOwner, EventObserver {
            setAdapterWithGridSpanSize(it)
        })

        bindingView.includePaymentMethod.recyclerPaymentMethod.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        setIndicatorPosition(
                            layoutManager, totalVisibleItem, dotsCount, dots, requireContext()
                        )
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isCountTotalVisible) {
                    isCountTotalVisible = false
                    totalVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition() + 1
                    setImageSliderDots(
                        adapter.itemCount,
                        bindingView.includePaymentMethod.linearSliderDots
                    )
                }
            }
        })

        bindingView.inputLayoutDepositDate.editTextDate.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(
                    requireContext(),
                    { _, year, monthOfYear, dayOfMonth ->
                        val month = formatToTwoDigit(monthOfYear + 1)
                        val day = formatToTwoDigit(dayOfMonth)
                        val date = "$year-$month-$day"

                        bindingView.inputLayoutDepositDate.editTextDate.setText(date)
                    },
                    DateUtils.getCalender().year,
                    DateUtils.getCalender().month,
                    DateUtils.getCalender().day
                )

            datePickerDialog.show()
        }

        bindingView.textViewDocumentUpload.setOnClickListener {
            checkPermission()
        }

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        return bindingView.root
    }

    private fun setAdapterWithGridSpanSize(list: List<GateWay>) {
        when {
            list.size > 4 -> layoutManager.spanCount = 2
            else -> layoutManager.spanCount = 1
        }
        adapter.submitList(list)
        isCountTotalVisible = true
    }

    private fun setImageSliderDots(count: Int, linearSliderDots: LinearLayout) {
        if (count <= 6) {
            linearSliderDots.visibility = View.GONE
            return
        }

        linearSliderDots.removeAllViews()
        linearSliderDots.visibility = View.VISIBLE
        linearSliderDots.post {
            linearSliderDots.invalidate()
            linearSliderDots.requestLayout()
        }

        dotsCount = ((count - totalVisibleItem) / 2)
        if (count % 2 == 0) {
            dotsCount++
        } else {
            dotsCount += 2
        }

        dots = arrayOfNulls(dotsCount)
        for (i in 0 until dotsCount) {
            dots[i] = ImageView(context)
            dots[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.non_active_dot
                )
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(8, 8, 8, 8)
            linearSliderDots.addView(dots[i], params)
        }

        dots[0]?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.active_dot
            )
        )
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                || requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    GALLERY_PERMISSION
                )
            } else {
                galleryIntent()
            }
        } else {
            if (
                requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                || requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    GALLERY_PERMISSION
                )
            } else {
                galleryIntent()
            }
        }
    }

    private fun galleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            GALLERY_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_PERMISSION && resultCode == Activity.RESULT_OK) {
            val filepath = getFilePath(data?.data!!, requireActivity())
            requireContext().contentResolver.getType(data.data!!)?.let {
                viewModel.getUrlFromFilepath(
                    filepath,
                    it
                )
            }
        }
    }

    override fun onClickItem(data: GateWay) {
        viewModel.setGateWayDetails(data)
    }
}

package net.sharetrip.b2b.view.topup.ui.topup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentTopUpOnlineBinding
import net.sharetrip.b2b.network.PaymentEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.setIndicatorPosition
import net.sharetrip.b2b.view.flight.booking.ui.ListItemClickListener
import net.sharetrip.b2b.view.topup.model.GateWay
import net.sharetrip.b2b.view.topup.ui.paymentmethod.PaymentMethodFragment.Companion.ARG_PAYMENT_URL

class TopUpFragment : Fragment(), ListItemClickListener<GateWay> {
    val adapter: GateWayAdapter = GateWayAdapter(this)
    private lateinit var layoutManager: GridLayoutManager
    private var isCountTotalVisible = true
    private var dotsCount = 0
    private var totalVisibleItem = 0
    private lateinit var dots: Array<ImageView?>
    lateinit var bindingView: FragmentTopUpOnlineBinding
    var spanCount = 1

    private val viewModel by lazy {
        val endPoint = ServiceGenerator.createService(PaymentEndPoint::class.java)
        TopUpVMFactory(TopUpRepo(endPoint)).create(TopUpVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentTopUpOnlineBinding.inflate(layoutInflater, container, false)
        bindingView.viewModel = viewModel
        bindingView.includeBottomSheet.viewModel = viewModel

        layoutManager = GridLayoutManager(requireContext(), spanCount, GridLayoutManager.HORIZONTAL, false)
        bindingView.includePaymentMethod.recyclerPaymentMethod.layoutManager = layoutManager
        bindingView.includePaymentMethod.recyclerPaymentMethod.adapter = adapter

        viewModel.paymentUrl.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                R.id.action_payment_confirmation_dest_to_payment_method_dest,
                bundleOf(
                    ARG_PAYMENT_URL to it
                )
            )
        })

        bindingView.includePaymentMethod.recyclerPaymentMethod.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        setIndicatorPosition(
                            layoutManager,
                            totalVisibleItem,
                            dotsCount,
                            dots,
                            requireContext()
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

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.gateWays.observe(viewLifecycleOwner, EventObserver {
            setAdapterWithGridSpanSize(it)
        })

        viewModel.showMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        return bindingView.root
    }

    private fun setAdapterWithGridSpanSize(list: List<GateWay>) {
        spanCount = when {
            list.size > 4 -> 2
            else -> 1
        }
        layoutManager.spanCount = spanCount
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

    override fun onClickItem(data: GateWay) {
        viewModel.setGateWayDetails(data)
        viewModel.setTotalAmount(bindingView.editTextAmount.text.toString())
    }
}

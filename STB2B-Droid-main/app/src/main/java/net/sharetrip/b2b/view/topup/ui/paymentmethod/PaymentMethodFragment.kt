package net.sharetrip.b2b.view.topup.ui.paymentmethod

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentPaymentMethodBinding

class PaymentMethodFragment : Fragment() {
    private val paymentSuccessUrl: String = "https://b2b.sharetrip.net/payment/status?type=success"
    private val paymentDeclineUrl: String = "https://b2b.sharetrip.net/payment/status?type=declined"
    private val paymentCancelUrl: String = "https://b2b.sharetrip.net/payment/status?type=cancelled"
    private var paymentLink = ""
    private lateinit var bindingView: FragmentPaymentMethodBinding
    private var webViewBundle: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentPaymentMethodBinding.inflate(layoutInflater, container, false)
        paymentLink = arguments?.getString(ARG_PAYMENT_URL)!!

        val settings = bindingView.webView.settings
        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.setSupportZoom(true)
        settings.displayZoomControls = false

        bindingView.webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return when (url) {
                    paymentSuccessUrl -> {
                        lifecycleScope.launchWhenResumed {
                            findNavController().navigate(
                                R.id.action_top_up_online_to_confirmation,
                                bundleOf(ARG_IS_CONFIRMED to true)
                            )
                        }
                        true
                    }
                    paymentDeclineUrl, paymentCancelUrl -> {
                        lifecycleScope.launchWhenResumed {
                            findNavController().navigate(
                                R.id.action_top_up_online_to_confirmation,
                                bundleOf(ARG_IS_CONFIRMED to false)
                            )
                        }
                        true
                    }
                    else -> {
                        view.loadUrl(url)
                        true
                    }
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                bindingView.webView.visibility = View.VISIBLE
                bindingView.layoutProgress.visibility = View.GONE
                bindingView.progressBar.isIndeterminate = false
                super.onPageFinished(view, url)
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                bindingView.layoutProgress.visibility = View.VISIBLE
                bindingView.progressBar.isIndeterminate = true
                super.onPageStarted(view, url, favicon)
            }
        }

        bindingView.webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                return true
            }
        }

        bindingView.webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        bindingView.webView.settings.domStorageEnabled = true
        bindingView.webView.settings.allowUniversalAccessFromFileURLs = true

        if (isOnline()) {
            if (webViewBundle == null) {
                bindingView.webView.loadUrl(paymentLink)
            } else {
                bindingView.webView.restoreState(webViewBundle!!)
            }
        } else {
            val summary =
                "<html><body><font color='red'>No Internet Connection</font></body></html>"
            bindingView.webView.loadData(summary, "text/html", null)
            Toast.makeText(context, "No Internet Connection.", Toast.LENGTH_SHORT).show()
        }

        bindingView.webView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                val webView = v as WebView

                when (keyCode) {
                    KeyEvent.KEYCODE_BACK -> if (webView.canGoBack()) {
                        webView.goBack()
                        return@OnKeyListener true
                    }
                }
            }
            false
        })
        return bindingView.root
    }

    override fun onResume() {
        super.onResume()
        bindingView.webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        bindingView.webView.onPause()

        webViewBundle = Bundle()
        bindingView.webView.saveState(webViewBundle!!)
    }

    private fun isOnline(): Boolean {
        val cm =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    companion object {
        const val ARG_PAYMENT_URL = "payment_url"
        const val ARG_IS_CONFIRMED = "is_confirmed"
    }
}

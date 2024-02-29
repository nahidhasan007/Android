package net.sharetrip.b2b.view.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.databinding.FragmentWebviewBinding

class WebViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentWebviewBinding.inflate(inflater, container, false)

        bindingView.toolbarTitle = arguments?.getString(ARGS_TOOLBAR_TITLE)
        var webViewData = arguments?.getString(ARGS_WEB_VIEW_DATA)
        webViewData= webViewData!!.replace("\n", "<br />")

            bindingView.webView.loadDataWithBaseURL(
                null,
                webViewData,
                "text/html",
                "utf-8",
                null
            )

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return bindingView.root
    }

    companion object {
        const val ARGS_WEB_VIEW_DATA = "web_view_data"
        const val ARGS_TOOLBAR_TITLE = "toolbar_title"
    }
}
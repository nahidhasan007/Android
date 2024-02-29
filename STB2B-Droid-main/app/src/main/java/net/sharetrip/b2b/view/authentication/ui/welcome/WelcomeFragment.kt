package net.sharetrip.b2b.view.authentication.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.sharetrip.b2b.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private var pageNo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageNo = it.getInt(PAGE_NO, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentWelcomeBinding.inflate(inflater, container, false)
        bindingView.pageNo = pageNo
        return bindingView.root
    }

    companion object {
        private const val PAGE_NO = "PAGE_NO"
        fun newInstance(pageNo: Int): WelcomeFragment {
            val fragment = WelcomeFragment()
            val args = Bundle()
            args.putInt(PAGE_NO, pageNo)
            fragment.arguments = args
            return fragment
        }
    }
}

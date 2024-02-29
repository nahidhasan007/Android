package net.sharetrip.b2b.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.util.Event

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    protected val bindingView: VB
        get() = viewDataBinding
    private lateinit var viewDataBinding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layoutId = layoutId()
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        initOnCreateView()

        val viewModel = getViewModel()
        viewModel?.showMessage?.observe(viewLifecycleOwner) { eventOfMsg: Event<String> ->
            val msg = eventOfMsg.getContent()
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }


        return viewDataBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewDataBinding.unbind()
    }

    @LayoutRes
    protected abstract fun layoutId(): Int

    protected abstract fun getViewModel(): BaseViewModel?

    protected abstract fun initOnCreateView()
}

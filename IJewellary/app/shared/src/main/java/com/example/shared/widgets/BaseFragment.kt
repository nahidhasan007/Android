package com.example.shared.widgets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


abstract class BaseFragment<VB : ViewDataBinding>: Fragment() {

    protected val bindingView: VB
        get() = viewDataBinding
    private lateinit var viewDataBinding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val layoutId = layoutId()
        viewDataBinding = DataBindingUtil.inflate(inflater,layoutId,container,false)

        initOnCreateView()
        val viewModel = getViewModel()
        return viewDataBinding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        viewDataBinding.unbind()
    }

    @LayoutRes
    protected abstract fun layoutId(): Int

    protected abstract fun getViewModel() : BaseViewModel?

    protected abstract fun initOnCreateView()

    companion object {

    }
}
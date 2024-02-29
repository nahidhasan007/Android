package net.sharetrip.b2b.view.authentication.ui.forget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ForgetPasswordVMFactory(private val repo: ForgetPasswordRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgetPasswordVM::class.java))
            return ForgetPasswordVM(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package net.sharetrip.b2b.view.authentication.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginVMFactory(private val repo: LoginRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginVM::class.java))
            return LoginVM(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package net.sharetrip.b2b.view.more.moreinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoreVMFactory (private val repo: MoreRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoreVM::class.java))
            return MoreVM(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
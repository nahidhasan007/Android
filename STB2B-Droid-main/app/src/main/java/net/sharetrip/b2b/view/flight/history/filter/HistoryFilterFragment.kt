package net.sharetrip.b2b.view.flight.history.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.sharetrip.b2b.databinding.FragmentHistoryFilterBinding

class HistoryFilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentHistoryFilterBinding.inflate(layoutInflater, container, false)

        return bindingView.root
    }
}
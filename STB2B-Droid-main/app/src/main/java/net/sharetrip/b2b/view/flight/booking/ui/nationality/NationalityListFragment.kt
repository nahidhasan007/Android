package net.sharetrip.b2b.view.flight.booking.ui.nationality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import net.sharetrip.b2b.databinding.FragmentNationalityListBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.setNavigationResult
import net.sharetrip.b2b.view.flight.booking.model.Nationality

class NationalityListFragment : Fragment(), ItemClickListener {
    private val viewModel: NationalityListVM by lazy {
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        NationalityListVMFactory(NationalityListRepo(endPoint)).create(NationalityListVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentNationalityListBinding.inflate(layoutInflater, container, false)

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.showMessage.observe(viewLifecycleOwner, { msg ->
            if (!msg.isNullOrEmpty())
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        })

        val adapter = NationalityAdapter(this)
        bindingView.listCountry.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        bindingView.listCountry.itemAnimator = DefaultItemAnimator()
        bindingView.listCountry.adapter = adapter

        viewModel.nationalityList.observe(viewLifecycleOwner, { countryList ->
            countryList?.let {
                adapter.updateCodeList(it)
            }
        })

        return bindingView.root
    }

    override fun onClickItem(nationality: Nationality) {
        setNavigationResult(nationality, NATIONALITY_KEY)
        findNavController().popBackStack()
    }
}

const val NATIONALITY_KEY = "nationality_code"
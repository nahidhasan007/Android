package net.sharetrip.b2b.view.flight.booking.ui.airport

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentAirportSearchBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.*

class AirportSearchFragment : Fragment(), MenuItem.OnActionExpandListener,
    SearchView.OnQueryTextListener {
    private lateinit var adapter: AirportAdapter
    private val viewModel: AirportSearchVM by lazy {
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)

        AirportSearchVMFactory(arguments, AirportSearchRepo(endPoint)).create(
            AirportSearchVM::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentAirportSearchBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).setSupportActionBar(bindingView.toolbar)

        adapter = AirportAdapter(viewModel)
        bindingView.listAirport.setHasFixedSize(true)
        bindingView.listAirport.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        bindingView.listAirport.itemAnimator = DefaultItemAnimator()
        bindingView.listAirport.adapter = adapter

        viewModel.airportList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.moveToBack.observe(viewLifecycleOwner) {
            val navKey = when (viewModel.tripType) {
                ONE_WAY -> AIRPORT_IATA_ONE_WAY

                RETURN -> AIRPORT_IATA_RETURN

                else -> AIRPORT_IATA_MULTI_CITY
            }

            setNavigationResult(it, navKey)
            findNavController().navigateUp()
            hideKeyboard()
        }

        return bindingView.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_airport_search, menu)
        val mSearchMenuItem = menu.findItem(R.id.action_search)
        setupSearchView(mSearchMenuItem)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        findNavController().navigateUp()
        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(queryText: String?): Boolean {
        queryText?.let { query ->
            if (query.length >= 3) {
                viewModel.airportListByKeyText(queryText)
                //airportAdapter.filter(newText)
            } else {
                //airportAdapter.filter(newText)
            }
        }
        return false
    }

    override fun onDestroy() {
        (activity as AppCompatActivity).setSupportActionBar(null)
        super.onDestroy()
    }

    private fun setupSearchView(searchMenuItem: MenuItem) {
        searchMenuItem.setOnActionExpandListener(this)
        val searchView = searchMenuItem.actionView as SearchView
        searchMenuItem.expandActionView()
        searchView.requestFocus()
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = MsgUtils.airportSearchMsg
    }
}

const val AIRPORT_IATA_ONE_WAY = "airport_iata_one_way"
const val AIRPORT_IATA_RETURN = "airport_iata_return"
const val AIRPORT_IATA_MULTI_CITY = "airport_iata_multi_city"

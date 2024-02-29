package net.sharetrip.b2b.view.flight.history.reissue_change_date.searchairport

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentSearchAirportBinding
import net.sharetrip.b2b.localdb.LocalDataBase
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.setNavigationResult
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.ItemClickSupport


class SearchAirportFragment: Fragment(),
    MenuItem.OnActionExpandListener,
    SearchView.OnQueryTextListener {

    companion object {
        const val  TAG = "SearchAirport"
        const val ARG_AIRPORT_CODE = "ARG_AIRPORT_CODE"
        const val ARG_AIRPORT_CITY = "ARG_AIRPORT_CITY"
        const val ARG_AIRPORT_ADDRESS = "ARG_AIRPORT_ADDRESS"

        const val ARG_FLIGHT_SEARCH_AIRPORT = "ARG_FLIGHT_SEARCH_AIRPORT"
        const val ARG_FLIGHT_SEARCH_BUNDLE = "ARG_FLIGHT_SEARCH_BUNDLE"
        const val IS_ORIGIN_OR_DESTINATION = "IS_ORIGIN_OR_DESTINATION"
        const val ARG_CLASS_NAME = "ARG_CLASS_NAME"
    }

    private var _bindingView: FragmentSearchAirportBinding? = null
    private val bindingView: FragmentSearchAirportBinding get() = _bindingView!!

    private val mApiService by lazy {
        ServiceGenerator.createService(ReissueApiService::class.java)
    }

    private val airportDao: AirportDao by lazy {
        LocalDataBase.getDataBase(requireContext()).airportDao()
    }

    private val viewModel by lazy {
        SearchAirportViewModelFactory(
            SearchAirportRepo(mApiService, airportDao)
        ).create(SearchAirportViewModel::class.java)
    }

    private val airportAdapter by lazy { AirportAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this._bindingView = FragmentSearchAirportBinding.inflate(inflater, container, false)
        bindingView.root.setOnClickListener {  }
        return bindingView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity() as MenuHost

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.flight_search, menu)
                val searchMenuItem = menu.findItem(R.id.action_search)
                setupSearchView(searchMenuItem)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        bindingView.airportRecyclerView.adapter = airportAdapter
        val mItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        bindingView.airportRecyclerView.addItemDecoration(mItemDecoration)

        viewModel.airportList.observe(viewLifecycleOwner) {
            airportAdapter.resetItems(it)
            airportAdapter.filter(" ")
        }

        ItemClickSupport.addTo(bindingView.airportRecyclerView)
            .setOnItemClickListener { _, position, _ ->
                if (position >= 0) {
                    val intent = Intent()
                    val airport = airportAdapter.getItem(position)
                    intent.apply {
                        putExtra(
                            IS_ORIGIN_OR_DESTINATION,
                            arguments?.getBundle(ARG_FLIGHT_SEARCH_BUNDLE)?.getBoolean(
                                IS_ORIGIN_OR_DESTINATION
                            )
                        )
                        putExtra(ARG_AIRPORT_CODE, airport.iata)
                        putExtra(ARG_AIRPORT_CITY, airport.city)
                        putExtra(ARG_AIRPORT_ADDRESS, airport.name)
                    }
                    setNavigationResult(
                        intent,
                        arguments?.getBundle(ARG_FLIGHT_SEARCH_BUNDLE)
                            ?.getString(ARG_CLASS_NAME, "")!!
                    )
                    findNavController().navigateUp()
                    viewModel.handleSelectedItem(airportAdapter.getItem(position))
                }
            }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingView = null
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

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrEmpty() && newText.length >= 3) {
            viewModel.getAirportListByName(newText)
            airportAdapter.filter(newText)
        } else if (!newText.isNullOrEmpty()) {
            airportAdapter.filter(newText)
        } else if (newText.isNullOrEmpty()) {
            viewModel.fetchTopAirportListWithLocal()
        }
        return false
    }

    private fun setupSearchView(searchMenuItem: MenuItem) {
        searchMenuItem.setOnActionExpandListener(this)
        val searchView = searchMenuItem.actionView as SearchView
        searchView.queryHint = getString(R.string.origin_city_or_Airport)
        searchMenuItem.expandActionView()
        searchView.requestFocus()
        searchView.setOnQueryTextListener(this)
    }
}
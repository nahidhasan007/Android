package com.example.todaysnews.view.news.newsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todaysnews.databinding.FragmentNewsHomeBinding
import com.example.todaysnews.network.BaseRetrofitBuilder
import com.example.todaysnews.network.NewsApi
import com.example.todaysnews.network.NewsRepository
import com.example.todaysnews.view.news.adapter.NewsAdapter

class NewsHome : Fragment() {

    private val newsViewModel: NewsHomeViewModel by viewModels {
        NewsHomeViewModelFactory(
            NewsRepository(BaseRetrofitBuilder.getInstance().create(NewsApi::class.java))
        )
    }

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentNewsHomeBinding.inflate(inflater, container, false)

        val newsAdapter = NewsAdapter()
        binding.newsRecyclerView.adapter = newsAdapter

        newsViewModel.posts.observe(viewLifecycleOwner) {
            newsAdapter.submitList(it)
        }

        return binding.root
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            NewsHome().apply {

            }
    }

}
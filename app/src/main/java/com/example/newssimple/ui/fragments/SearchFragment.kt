package com.example.newssimple.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newssimple.databinding.FragmentSearchBinding
import com.example.newssimple.model.data.NewsData
import com.example.newssimple.ui.activity.NewsActivity
import com.example.newssimple.ui.adapter.NewsAdapter
import com.example.newssimple.util.QUERY_PAGE_SIZE
import com.example.newssimple.util.Resource
import com.example.newssimple.util.SEARCH_NEWS_TIME_DELAY
import com.example.newssimple.viewModel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() , NewsAdapter.ArticleEvents {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //receiving view model from activity and allowing to use in fragment
        viewModel = (activity as NewsActivity).viewModel
        setupRecycler()
        // settin up a delay for searching
        var job: Job? = null
        binding.edtSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                if (editable.toString().isNotEmpty()) {
                    viewModel.searchNews(editable.toString())

                }
            }
        }
        // Sending Article to Article Fragment


        // getting the data from viewModel and showing it
        viewModel.searchNewsData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newResponse ->
                        newsAdapter.differ.submitList(newResponse.articles.toList())
                        // why +2 ? => it's integer devision so it's always rounded down  also the last page of responses are mainly empty and we don't want that so we add one more
                        val totalPages = newResponse.totalResults / QUERY_PAGE_SIZE + 2
                        // now we check if we are at the last page
                        isLastPage = viewModel.searchNewsPage == totalPages
                        // next lines are for setting padding for Rv so progress bar has the space for itself
                        if (isLastPage) {
                            binding.rvSearchNews.setPadding(0, 0, 0, 0)

                        }
                    }


                }

                is Resource.Error -> {
                    hideProgressBar()
                    Log.e("error data in recycler ", response.message.toString())

                }

                is Resource.Loading -> {
                    showProgressBar()

                }
            }


        })

        // setting up on Item Clicked Listner

    }

    private fun setupRecycler() {
        newsAdapter = NewsAdapter(this)
        binding.rvSearchNews.adapter = newsAdapter
        binding.rvSearchNews.layoutManager = LinearLayoutManager(activity)
        binding.rvSearchNews.addOnScrollListener(this@SearchFragment.onScrollListner)

    }


    private fun hideProgressBar() {
        binding.paginationProgressBarSearch.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBarSearch.visibility = View.VISIBLE
        isLoading = true
    }

    override fun onItemClicked(article: NewsData.Article) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToArticleFragment(article))
    }



    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    val onScrollListner = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            isScrolling = true
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            // there is no automatic method for knowing if we had scrolled until the bottom of page or not so we do it manually by calculating

            val layoutManager = binding.rvSearchNews.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            // with the 3 above numbers we are able to check if we have scrolled until the bottom of page or not
            val isnotLoadingAndNottheLastPage = !isLoading && !isLastPage
            // when the visible items + first visible item is greater than total items we know that the last item is being shown
            val isLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            // with next line we know if we are not at the beginning of the list and first item is not visible
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            //then we should know if we have more items to show or not , each Query contains 20 news so...
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            // now based on the previous parameters we should decide if we should paginate or not
            val shouldPaginate =
                isNotAtBeginning && isTotalMoreThanVisible && isLastItem && isnotLoadingAndNottheLastPage && isScrolling
            if (shouldPaginate) {
                viewModel.searchNews(binding.edtSearch.text.toString())
                isScrolling = false
            }

        }
    }


}
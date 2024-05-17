package com.example.newssimple.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newssimple.databinding.FragmentBreakingNewsBinding
import com.example.newssimple.model.data.NewsData
import com.example.newssimple.ui.activity.NewsActivity
import com.example.newssimple.ui.adapter.NewsAdapter
import com.example.newssimple.util.QUERY_PAGE_SIZE
import com.example.newssimple.util.Resource
import com.example.newssimple.viewModel.NewsViewModel


class BreakingNewsFragment : Fragment(), NewsAdapter.ArticleEvents {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentBreakingNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //receiving view model from activity and allowing to use in fragment
        viewModel = (activity as NewsActivity).viewModel
        setupRecycler()
        // getting the data from viewModel and showing it
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newResponse ->
                        newsAdapter.differ.submitList(newResponse.articles.toList())
                        // why +2 ? => it's integer devision so it's always rounded down  also the last page of responses are mainly empty and we don't want that so we add one more
                        val totalPages = newResponse.totalResults / QUERY_PAGE_SIZE + 2
                        // now we check if we are at the last page
                        isLastPage = viewModel.brekingNewsPage == totalPages

                        // next lines are for setting padding for Rv so progress bar has the space for itself
                        if (isLastPage) {
                            binding.rvBreakingNews.setPadding(0, 0, 0, 0)

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

    }

    private fun setupRecycler() {
        newsAdapter = NewsAdapter(articleEvents = this)
        binding.rvBreakingNews.adapter = newsAdapter
        binding.rvBreakingNews.layoutManager = LinearLayoutManager(activity)
        // now we should add the scroll listner to the Rv but the name is the same as the Scroll Listner as it's own so we shoul pass it with "this" keyword
        binding.rvBreakingNews.addOnScrollListener(this@BreakingNewsFragment.onScrollListner)

    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    override fun onItemClicked(article: NewsData.Article) {
        findNavController().navigate(
            BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(
                article
            )
        )
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

            val layoutManager = binding.rvBreakingNews.layoutManager as LinearLayoutManager
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
                viewModel.getBrekingNews("us")
                isScrolling = false
            }

        }
    }


}
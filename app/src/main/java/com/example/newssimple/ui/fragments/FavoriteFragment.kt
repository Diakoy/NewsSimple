package com.example.newssimple.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newssimple.databinding.FragmentFavoriteBinding
import com.example.newssimple.model.data.NewsData
import com.example.newssimple.ui.activity.NewsActivity
import com.example.newssimple.ui.adapter.NewsAdapter
import com.example.newssimple.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class FavoriteFragment : Fragment(), NewsAdapter.ArticleEvents {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentFavoriteBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecycler()

        // observing to the changes of db and updating recycler view
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Successfully Deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("undo") { viewModel.saveArticle(article) }
                }.show()

            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }


    }

    private fun setupRecycler() {
        newsAdapter = NewsAdapter(this)
        binding.rvSavedNews.adapter = newsAdapter
        binding.rvSavedNews.layoutManager = LinearLayoutManager(activity)

    }

    override fun onItemClicked(article: NewsData.Article) {
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToArticleFragment(
                article
            )
        )
    }


}
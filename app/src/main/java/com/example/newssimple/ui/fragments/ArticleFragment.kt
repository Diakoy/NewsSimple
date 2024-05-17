package com.example.newssimple.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newssimple.databinding.FragmentArticleBinding
import com.example.newssimple.model.data.NewsData
import com.example.newssimple.ui.activity.NewsActivity
import com.example.newssimple.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding
     private val args: ArticleFragmentArgs by navArgs()
    private lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        val data = ArticleFragmentArgs.fromBundle(requireArguments()).articlePrime as NewsData.Article
        if (data!=null){

            binding.webView.apply {
                webViewClient = WebViewClient()
                loadUrl(data.url!!)

            }
        }

        binding.fab.setOnClickListener {
            viewModel.saveArticle(data)
            Snackbar.make(view , "Article Saved Succesfully" , Snackbar.LENGTH_SHORT).show()
        }


    }

}
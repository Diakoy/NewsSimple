package com.example.newssimple.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newssimple.model.NewsRepository
import com.example.newssimple.viewModel.NewsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val newsRepository: NewsRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}
package com.example.newssimple.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newssimple.model.NewsRepository
import com.example.newssimple.model.data.NewsData
import com.example.newssimple.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsData>> = MutableLiveData()
    var brekingNewsPage: Int = 1
    var breakingNewsResponse: NewsData? = null

    val searchNewsData: MutableLiveData<Resource<NewsData>> = MutableLiveData()
    var searchNewsPage: Int = 1
    var searchResponse: NewsData? = null


    // for now i add this to init when its created in activity
    init {
        getBrekingNews("us")
    }

    // receiving Data and emitting it to Resource then putting it in Live Data
    fun getBrekingNews(countryCode: String) =
        viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            // getting info and receiving in response then converting and emmiting to Resource then
            // passing to Live Data instance called breaking news
            val response = repository.getArticlesFromApi(countryCode, brekingNewsPage)
            breakingNews.postValue(handleBreakingNewsResponse(response))


        }

    fun searchNews(searchQuery: String) =
        viewModelScope.launch {
            searchNewsData.postValue(Resource.Loading())
            val response = repository.searchNews(searchQuery, searchNewsPage)
            searchNewsData.postValue(handleSearchingNewsResponse(response))

        }


    // converting Respond<NewsData> to Resource<NewsData>
    // now we just return a single response every time the request is made  but we want all response to be ready to scroll in the page
    // so we should capture the response in a var so we could add the previous response to it for pagination
    private fun handleBreakingNewsResponse(response: Response<NewsData>): Resource<NewsData> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                brekingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse!!.articles
                    val newArticles = resultResponse.articles
                    oldArticles.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(message = response.message())

    }


    // converting Respond<NewsData> to Resource<NewsData>
    private fun handleSearchingNewsResponse(response: Response<NewsData>): Resource<NewsData> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchResponse == null) {
                    searchResponse = resultResponse
                } else {
                    val oldArticles = searchResponse!!.articles
                    val newArticles = resultResponse.articles
                    oldArticles.addAll(newArticles)
                }
                return Resource.Success(searchResponse ?: resultResponse)
            }
        }
        return Resource.Error(message = response.message())

    }


    fun saveArticle(article: NewsData.Article) = viewModelScope.launch {
        repository.upSert(article)
    }

    fun getSavedNews() = repository.getSavedArticles()
    fun deleteArticle(article: NewsData.Article) = viewModelScope.launch {
        repository.removeArticle(article)
    }

}
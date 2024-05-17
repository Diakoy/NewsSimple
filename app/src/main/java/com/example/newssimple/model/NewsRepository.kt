package com.example.newssimple.model

import androidx.lifecycle.LiveData
import com.example.newssimple.model.api.RetrofitSingle
import com.example.newssimple.model.data.NewsData
import com.example.newssimple.model.local.ArticleDao

class NewsRepository(private val articleDao: ArticleDao) {




    //Searching
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitSingle.apiService!!.searchNews(searchQuery, pageNumber)

    // Reading Articles from api
    suspend fun getArticlesFromApi(countryCode: String, pageNumber: Int) =
        RetrofitSingle.apiService!!.getNews(countryCode, pageNumber)


    // reading favorite news from db
     fun getSavedArticles(): LiveData<List<NewsData.Article>> {
        return articleDao.readNewsDatabase()
    }

    // deleting Favorite News from DB
    suspend fun removeArticle(article: NewsData.Article) {
        articleDao.deleteArticle(article)
    }

    // Updating and Inserting Article in Data base
    suspend fun upSert(article : NewsData.Article) = articleDao.updateOrInsert(article)


}
package com.example.newssimple.model.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newssimple.model.data.NewsData

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsert(article :NewsData.Article) :Long


    @Query("SELECT * FROM articles")
     fun readNewsDatabase():LiveData<List<NewsData.Article>>


    @Delete
    suspend fun deleteArticle (article :NewsData.Article)

}
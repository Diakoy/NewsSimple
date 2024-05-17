//package com.example.newssimple.util
//
//import androidx.room.TypeConverter
//import com.example.newssimple.model.data.NewsData
//
//class Converter {
//    @TypeConverter
//    fun fromSource(source: NewsData.Article.Source): String {
//        return source.name
//    }
//
//    @TypeConverter
//    fun toSource(name: String): NewsData.Article.Source {
//        return NewsData.Article.Source(name, name)
//    }
//
//
//}
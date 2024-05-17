package com.example.newssimple.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.newssimple.model.data.NewsData


@Database(entities = [NewsData.Article::class], version = 1, exportSchema = false)
//@TypeConverters(Converter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract val articleDao: ArticleDao

    companion object {
        @Volatile
        var myDatabase: MyDatabase? = null
        fun getDatabase(context: Context): MyDatabase {
            synchronized(this) {
                if (myDatabase == null) {
                    myDatabase = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "newsDatabse.db"
                    )
//                        .addMigrations(MIGRATION_1_2)
                        .build()
                }

            }
            return myDatabase!!


        }

//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // Create the new "Articles" table
//                database.execSQL(
//                    """
//                    CREATE TABLE Articles (
//                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
//                        author TEXT,
//                        title TEXT NOT NULL,
//                        description TEXT,
//                        url TEXT NOT NULL,
//                        urlToImage TEXT,
//                        publishedAt TEXT,
//                        content TEXT
//                    )
//                """.trimIndent()
//                )
//            }
//        }


    }
}
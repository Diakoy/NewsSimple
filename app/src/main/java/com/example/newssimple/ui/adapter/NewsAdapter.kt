package com.example.newssimple.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newssimple.databinding.ItemArticleBinding
import com.example.newssimple.model.data.NewsData

class NewsAdapter(private val articleEvents: ArticleEvents) :
    RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    lateinit var binding: ItemArticleBinding

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindViews(article: NewsData.Article) {
            if (article != null) {

                Log.v("article", article.toString())

                binding.txtPublishedAt.text = article.publishedAt
//                binding.txtSource.text = article.source.name
                binding.txtTitle.text = article.title
                Glide
                    .with(itemView)
                    .load(article.urlToImage)
                    .into(binding.imgArticle)

                itemView.setOnClickListener {
                    articleEvents.onItemClicked(article)

                }
            }


        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<NewsData.Article>() {
        override fun areItemsTheSame(
            oldItem: NewsData.Article,
            newItem: NewsData.Article
        ): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: NewsData.Article,
            newItem: NewsData.Article
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding.root)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bindViews(differ.currentList[position])

    }

    interface ArticleEvents {
        fun onItemClicked(article: NewsData.Article)
    }


}
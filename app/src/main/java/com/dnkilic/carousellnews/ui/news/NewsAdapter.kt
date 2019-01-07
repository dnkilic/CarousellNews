package com.dnkilic.carousellnews.ui.news

import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dnkilic.carousellnews.R
import com.dnkilic.carousellnews.databinding.NewsItemBinding
import com.dnkilic.carousellnews.repository.model.News
import java.util.concurrent.Executors

class NewsAdapter(
        private val dataBindingComponent: DataBindingComponent,
        private val callback: ((News) -> Unit)?
) : ListAdapter<News, DataBoundViewHolder>(
        AsyncDifferConfig.Builder<News>(object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem.bannerUrl == newItem.bannerUrl
                        && oldItem.description == newItem.description
            }}).setBackgroundThreadExecutor(Executors.newFixedThreadPool(THREAD_COUNT)).build())
{
    private fun createBinding(parent: ViewGroup): NewsItemBinding {
        val binding = DataBindingUtil
                .inflate<NewsItemBinding>(
                        LayoutInflater.from(parent.context),
                        R.layout.news_item,
                        parent,
                        false,
                        dataBindingComponent
                )
        binding.root.setOnClickListener {
            binding.contributor?.let {
                callback?.invoke(it)
            }
        }
        return binding
    }

    private fun bind(binding: NewsItemBinding, item: News) {
        binding.contributor = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder {
        val binding = createBinding(parent)
        return DataBoundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder, position: Int) {
        bind(holder.binding, getItem(position))
        holder.binding.executePendingBindings()
    }

    companion object {
        private const val THREAD_COUNT = 3
    }
}

class DataBoundViewHolder constructor(val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

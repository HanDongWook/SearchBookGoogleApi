package com.google.book.ui.searchbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.book.R
import com.google.book.databinding.ViewholderBookItemBinding
import com.google.book.databinding.ViewholderItemProgressbarBinding

internal class SearchBookListAdapter(
    private val context: Context,
    private val listener: BookClickListener
) : ListAdapter<BookItem, BookViewHolder>(object :
    DiffUtil.ItemCallback<BookItem>() {
    override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return when (viewType) {
            BookItemView.Item.type -> {
                val binding = ViewholderBookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SearchBookViewHolder(context, binding, listener)
            }
            BookItemView.Loading.type -> {
                val binding = ViewholderItemProgressbarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding)
            }
            else -> throw IllegalStateException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        getItem(position)?.let { holder.bindTo(it) }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BookItem.Item -> BookItemView.Item.type
            is BookItem.Loading -> BookItemView.Loading.type
        }
    }
}

abstract class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bindTo(b: BookItem)
}

internal class SearchBookViewHolder(
    private val context: Context,
    private val binding: ViewholderBookItemBinding,
    private val listener: BookClickListener
) : BookViewHolder(binding.root) {
    override fun bindTo(b: BookItem) {
        val item = (b as BookItem.Item).info
        with(binding) {
            title.text = item.volumeInfo.title
            author.text = item.volumeInfo.authors.joinToString(", ")

            val imgLink = item.volumeInfo.imageLinks.smallThumbnail
            if (imgLink == null) {
                thumbnail.setImageResource(R.drawable.book_cover_not_found)
            } else {
                Glide
                    .with(context)
                    .load(item.volumeInfo.imageLinks.smallThumbnail)
                    .centerCrop()
                    .error(ContextCompat.getDrawable(context, R.drawable.book_cover_not_found))
                    .into(thumbnail)
            }

            bookItemLayout.setOnClickListener {
                listener.onClick(item.id!!)
            }
        }
    }
}

internal class LoadingViewHolder(binding: ViewholderItemProgressbarBinding) : BookViewHolder(binding.root) {
    override fun bindTo(b: BookItem) {}
}

interface BookClickListener {
    fun onClick(bookId: String)
}
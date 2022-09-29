package com.google.book.ui.searchbook

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.book.R
import com.google.book.databinding.ViewholderBookItemBinding
import com.google.book.domain.entities.BookInfo

internal class SearchBookListAdapter(
    private val context: Context,
    private val listener: BookClickListener
) : ListAdapter<BookInfo, SearchBookViewHolder>(object :
    DiffUtil.ItemCallback<BookInfo>() {
    override fun areItemsTheSame(oldItem: BookInfo, newItem: BookInfo): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BookInfo, newItem: BookInfo): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBookViewHolder {
        val binding = ViewholderBookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchBookViewHolder(context, binding, listener)
    }

    override fun onBindViewHolder(holder: SearchBookViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}

internal class SearchBookViewHolder(
    private val context: Context,
    private val binding: ViewholderBookItemBinding,
    private val listener: BookClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(b: BookInfo) {
        val item = b.volumeInfo
        with(binding) {
            title.text = item.title
            author.text = item.authors.joinToString(", ")

            val imgLink = item.imageLinks.smallThumbnail
            if (imgLink == null) {
                thumbnail.setImageResource(R.drawable.book_cover_not_found)
            } else {
                Glide
                    .with(context)
                    .load(item.imageLinks.smallThumbnail)
                    .centerCrop()
                    .error(ContextCompat.getDrawable(context, R.drawable.book_cover_not_found))
                    .into(thumbnail)
            }

            bookItemLayout.setOnClickListener {
                listener.onClick(b.id!!)
            }
        }
    }
}

interface BookClickListener {
    fun onClick(bookId: String)
}
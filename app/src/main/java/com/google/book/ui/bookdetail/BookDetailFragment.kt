package com.google.book.ui.bookdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.book.base.BaseFragment
import com.google.book.databinding.FragmentBookDetailBinding
import com.google.book.utils.gone
import com.google.book.utils.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

internal class BookDetailFragment : BaseFragment<FragmentBookDetailBinding, BookDetailViewModel>() {

    private val args: BookDetailFragmentArgs by navArgs()
    private val vm: BookDetailViewModel by viewModel() {
        parametersOf(args.id)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBookDetailBinding.inflate(inflater, container, false)
    override fun getViewModel() = vm

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        with(vm) {
            bookInfo.observe(viewLifecycleOwner) {
                binding.title.text = it.volumeInfo.title
                binding.author.text = it.volumeInfo.authors.joinToString(", ")
                binding.pubDate.text = it.volumeInfo.publishedDate
                binding.description.text = it.volumeInfo.description
                binding.language.text = it.volumeInfo.language

                Glide
                    .with(requireContext())
                    .load(it.volumeInfo.imageLinks.smallThumbnail)
                    .centerCrop()
                    .into(binding.thumbnail)
            }

            error.observe(viewLifecycleOwner) {
                showErrorDialog()
            }

            loading.observe(viewLifecycleOwner) {
                binding.progress.apply {
                    if (it) visible() else gone()
                }
            }
        }
    }
}
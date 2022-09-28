package com.google.book.ui.searchbook

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.book.R
import com.google.book.base.BaseFragment
import com.google.book.databinding.FragmentSearchBookBinding
import com.google.book.utils.gone
import com.google.book.utils.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

internal class SearchBookFragment : BaseFragment<FragmentSearchBookBinding, SearchBookViewModel>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchBookBinding.inflate(inflater, container, false)

    private val vm: SearchBookViewModel by viewModel()
    override fun getViewModel() = vm

    private var searchBookListAdapter: SearchBookListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initViewModel()
    }

    private fun initViews() {
        searchBookListAdapter = SearchBookListAdapter(requireContext(), object : BookClickListener{
            override fun onClick(bookId: String) {
                vm.onBookClick(bookId)
            }
        })

        binding.rcBookList.apply {
            adapter = searchBookListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            private var timer: Timer? = null
            private val DELAY: Long = 1000
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                timer?.cancel()
                timer = Timer()
                timer?.schedule(
                    object : TimerTask() {
                        override fun run() {
                            val query = p0.toString().trim()
                            vm.fetch(query)
                        }
                    },
                    DELAY
                )
            }
        })
    }

    private fun initViewModel() {
        with(vm) {
            bookList.observe(viewLifecycleOwner) {
                searchBookListAdapter?.submitList(it)
            }

            totalCount.observe(viewLifecycleOwner) {
                binding.results.text = resources.getString(R.string.results, it.toString())
            }

            error.observe(viewLifecycleOwner) {
                showErrorDialog()
            }

            loading.observe(viewLifecycleOwner) {
                binding.progress.apply {
                    if (it) visible() else gone()
                }
            }

            navigate.observe(viewLifecycleOwner) {
                when(it) {
                    is SearchBookViewModel.Navigate.BookDetail -> {
                        val action = SearchBookFragmentDirections.actionSearchBookFragmentToBookDetailFragment(
                            it.bookId
                        )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchBookListAdapter = null
    }
}
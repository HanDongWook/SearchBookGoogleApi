package com.google.book.ui.searchbook

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        searchBookListAdapter = SearchBookListAdapter(requireContext(), object : BookClickListener {
            override fun onClick(bookId: String) {
                vm.onBookClick(bookId)
            }
        })

        binding.rcBookList.apply {
            adapter = searchBookListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(rv, dx, dy)

                    val lastVisibleItemPosition = (rv.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = rv.adapter!!.itemCount

                    if (!rv.canScrollVertically(1) && (lastVisibleItemPosition + 1) == itemTotalCount && itemTotalCount != 0) {
                        if (itemTotalCount < vm.getResultCount()) vm.loadMore()
                        else showSnackBar(binding.layout, resources.getString(R.string.last_item))
                    }
                }
            })
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            private var timer: Timer? = null
            private val DELAY: Long = 1000
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (binding.searchBar.hasFocus()) {
                    timer?.cancel()
                    timer = Timer()
                    timer?.schedule(
                        object : TimerTask() {
                            override fun run() {
                                val query = p0.toString().trim()
                                Log.d("Test", "addTextChangedListener $query")
                                vm.fetch(query)
                            }
                        },
                        DELAY
                    )
                }
            }
        })

        binding.iconSearch.setOnClickListener {
            val text = binding.searchBar.text.toString().trim()
            if (text.isNotBlank()) vm.fetch(text)
            else showSnackBar(binding.layout, resources.getString(R.string.enter_search_term))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModel() {
        vm.bookList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.rcBookList.gone()
                binding.results.gone()
                if (binding.searchBar.text.toString().isNotBlank()) binding.noResults.visible()
            } else {
                binding.noResults.gone()
                binding.results.visible()
                binding.rcBookList.visible()
                searchBookListAdapter?.submitList(it)
                searchBookListAdapter?.notifyDataSetChanged()
            }
        }

        vm.totalCount.observe(viewLifecycleOwner) {
            binding.results.text = resources.getString(R.string.results, it.toString())
        }

        vm.error.observe(viewLifecycleOwner) {
            showErrorDialog()
        }

        vm.loading.observe(viewLifecycleOwner) {
            binding.progress.apply {
                if (it) visible() else gone()
            }
        }

        vm.navigate.observe(viewLifecycleOwner) {
            when (it) {
                is SearchBookViewModel.Navigate.BookDetail -> {
                    val action =
                        SearchBookFragmentDirections.actionSearchBookFragmentToBookDetailFragment(
                            it.bookId
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchBookListAdapter = null
    }
}
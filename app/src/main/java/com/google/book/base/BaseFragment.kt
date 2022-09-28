package com.google.book.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<V : ViewBinding, VM : ViewModel>() : Fragment() {

    private var _binding: V? = null
    val binding get() = _binding!!

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): V
    abstract fun getViewModel(): VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    fun showErrorDialog(
        title: String? = "Error",
        message: String? = "Error occurred"
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("ok") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun showSnackBar(view: View, text: String, isLong: Boolean = true) {
        val sb = Snackbar.make(view, text, if (isLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT)
        sb.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
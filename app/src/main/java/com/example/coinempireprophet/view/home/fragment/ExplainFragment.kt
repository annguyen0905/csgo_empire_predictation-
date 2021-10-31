package com.example.coinempireprophet.view.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.coinempireprophet.databinding.FragmentExplainBinding
import com.example.coinempireprophet.view.home.viewmodel.HomeViewModel


class ExplainFragment : Fragment() {
    private var _binding: FragmentExplainBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExplainBinding.inflate(inflater, container, false)
        return binding.root
    }

}
package com.example.coinempireprophet.view.home.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.coinempireprophet.R
import com.example.coinempireprophet.databinding.FragmentHomeBinding
import com.example.coinempireprophet.view.home.viewmodel.HomeViewModel


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        setButtonListener(viewModel)
        return binding.root
    }

    private fun setButtonListener(viewModel: HomeViewModel) {

        binding.btnGo.setOnClickListener {

            view?.let { it1 ->
                this.playSound()
                Navigation.findNavController(it1)
                    .navigate(R.id.action_homeFragment_to_betFragment)
            }
        }
        binding.txtEx.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_explainFragment)
        }

        binding.txtAbout.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_aboutFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun playSound() {
        val mp: MediaPlayer = MediaPlayer.create(requireContext(), R.raw.rollstart)
        mp.start()
    }


}
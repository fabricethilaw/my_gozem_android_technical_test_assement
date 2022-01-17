package com.fabricethilaw.gozem.businesscase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.fabricethilaw.gozem.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val sharedViewModel by activityViewModels<BusinessCaseViewModel>()
    private lateinit var progressDialog: DialogProgressBar

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        binding.fragment = this
        progressDialog = DialogProgressBar(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* sharedViewModel.getProfile(onError = {}) {
            binding.text.text = it
        }*/
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
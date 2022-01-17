package com.fabricethilaw.gozem.businesscase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.fabricethilaw.gozem.R
import com.fabricethilaw.gozem.databinding.FragmentHomeBinding
import com.fabricethilaw.gozem.showMessage
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val sharedViewModel by activityViewModels<BusinessCaseViewModel>()
    private lateinit var progressDialog: DialogProgressBar

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        (sharedViewModel.mapContent.value)?.run {

            val position = LatLng(this.latitude ?: 0.0, this.longitude ?: 0.0)
            googleMap.addMarker(
                MarkerOptions().position(position).title(getString(R.string.marker_in, title))
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.viewModel = sharedViewModel
        progressDialog = DialogProgressBar(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog.show()
        sharedViewModel.getProfile(onError = {
            progressDialog.hide()
            binding.root.showMessage(R.string.error_profile, it)
        }) {
            progressDialog.hide()
            MapsInitializer.initialize(requireActivity().applicationContext)
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        }
    }

    private fun refocusMapCameraOn(location: LatLng) {

        val cameraPosition =
            CameraPosition.Builder().target(location).zoom(22f).build()
        googleMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                cameraPosition
            )
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
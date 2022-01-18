package com.fabricethilaw.gozem.businesscase

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.afollestad.assent.Permission.ACCESS_FINE_LOCATION
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.rationale.createDialogRationale
import com.fabricethilaw.gozem.BuildConfig
import com.fabricethilaw.gozem.R
import com.fabricethilaw.gozem.businesscase.location.LocationUpdatesService
import com.fabricethilaw.gozem.businesscase.location.LocationUpdatesService.ACTION_BROADCAST
import com.fabricethilaw.gozem.databinding.FragmentHomeBinding
import com.fabricethilaw.gozem.showMessage
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class HomeFragment : Fragment() {


    private val sharedViewModel by activityViewModels<BusinessCaseViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: DialogProgressBar


    private val callback = OnMapReadyCallback { googleMap ->
        locationUpdatesReceiver.setMapToUpdate(googleMap)
        trackUserPosition(googleMap)
    }

    // A reference to the service used to get location updates.
    private var mService: LocationUpdatesService? = null
    private var mServiceIsBound: Boolean = false

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private val locationUpdatesReceiver: LocationsUpdatesReceiver = LocationsUpdatesReceiver()


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

            val rationaleHandler = createDialogRationale(R.string.location_permission) {
                onPermission(ACCESS_FINE_LOCATION, getString(R.string.fine_location_rationale))
            }

            askForPermissions(
                ACCESS_FINE_LOCATION,
                rationaleHandler = rationaleHandler
            ) { result ->
                val permissionsGranted = result.isAllGranted(ACCESS_FINE_LOCATION)
                if (permissionsGranted) {
                    setupMap()
                    checkGPSEnable()
                } else {
                    binding.root.showMessage(
                        R.string.location_permission,
                        getString(R.string.fine_location_rationale)
                    ) {
                        findNavController().navigate(R.id.SignInFragment)
                    }
                }
            }

        }
    }

    private fun setupMap() {
        MapsInitializer.initialize(requireActivity().applicationContext)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }


    private fun checkGPSEnable() {
        val manager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            binding.root.showMessage(R.string.gps_disabled_want_to_enable) {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }

    }

    private fun trackUserPosition(googleMap: GoogleMap) {
        if (checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Let user allow location permission on the Setting screen
            tellUserWeNeedGPSPermission()
            return
        }
        // otherwise update track the device location
        googleMap.isMyLocationEnabled = true
        mService?.requestLocationUpdates()

    }

    private fun tellUserWeNeedGPSPermission() {
        binding.root.showMessage(
            R.string.location_permission,
            message = getString(R.string.fine_location_rationale),
            buttonText = R.string.settings
        ) { // Build intent that displays the App settings screen.
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri: Uri = Uri.fromParts(
                "package",
                BuildConfig.APPLICATION_ID, null
            )
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }

    // Monitors the state of the connection to the service.
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: LocationUpdatesService.LocalBinder =
                service as LocationUpdatesService.LocalBinder
            mService = binder.service
            mServiceIsBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mServiceIsBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since the fragment's activity is in the foreground, the service can exit foreground mode.
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since the fragment's activity is in the foreground, the service can exit foreground mode.
        requireActivity().bindService(
            Intent(requireActivity(), LocationUpdatesService::class.java), mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(
            locationUpdatesReceiver,
            IntentFilter(ACTION_BROADCAST)
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireActivity())
            .unregisterReceiver(locationUpdatesReceiver)
        super.onPause()
    }


    override fun onStop() {
        super.onStop()
        if (mServiceIsBound) {
            // Unbind from the service.
            // This signals to the service that the fragment's activity
            // is no longer in the foreground, and the service can respond by promoting itself
            // to a foreground service.
            requireActivity().unbindService(mServiceConnection)
            mServiceIsBound = false
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /**
     * Receiver for broadcasts sent by [LocationUpdatesService].
     */
    inner class LocationsUpdatesReceiver : BroadcastReceiver() {
        private var googleMap: GoogleMap? = null
        private var marker: Marker? = null

        override fun onReceive(context: Context, intent: Intent) {
            val location: Location? =
                intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION)
            if (location != null) {
                if (marker == null) {
                    marker = googleMap?.addMarker(
                        MarkerOptions().position(location.asLatLng()).title(
                            getString(
                                R.string.marker_in,
                                sharedViewModel.mapContent.value!!.title
                            )
                        )
                    )
                } else {
                    marker!!.position = location.asLatLng()
                }
                // Focus the camera on the new position of the marker
                googleMap?.moveCamera(CameraUpdateFactory.newLatLng(location.asLatLng()))
            }
        }

        fun setMapToUpdate(googleMap: GoogleMap) {
            this.googleMap = googleMap
        }
    }


}

/**
 * Converts GPS Location to Google Map Location object
 */
fun Location.asLatLng() = LatLng(latitude, longitude)



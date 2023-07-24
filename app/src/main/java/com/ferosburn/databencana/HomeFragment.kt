package com.ferosburn.databencana

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferosburn.databencana.databinding.FragmentHomeBinding
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: MapView

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // mapView binding
        map = binding.mapView
        val mapController = map.controller
        // set map style/type/source
        map.apply {
            setTileSource(TileSourceFactory.OpenTopo)
            setMultiTouchControls(true)
            @Suppress("DEPRECATION")
            setBuiltInZoomControls(false)
        }
        binding.tvFilter.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_filterFragment)
        }
        viewModel.apply {
            bbox.observe(viewLifecycleOwner) {
                val avgLong = averageCoordinate(it[0], it[2])
                val avgLat = averageCoordinate(it[1], it[3])
                mapController.setCenter(GeoPoint(avgLat, avgLong))
                mapController.setZoom(7.0)
            }
            listDisaster.observe(viewLifecycleOwner) {
                map.overlays.clear()
                it.map { itemDisaster ->
                    val marker = Marker(map)
                    marker.position = GeoPoint(itemDisaster.coordinates[1], itemDisaster.coordinates[0])
                    marker.icon = context?.getDrawable(getDisasterIcon(itemDisaster.disasterType))
                    marker.title = "Test Marker"
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    map.overlays.add(marker)
                }
                map.invalidate()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDetach() {
        super.onDetach()
        map.onDetach()
    }

    private fun averageCoordinate(value1: Double, value2: Double): Double {
        return (value1 + value2) / 2
    }

    private fun getDisasterIcon(disasterType: String): Int {
        return when(disasterType) {
            DisasterTypes.FLOOD.value -> R.drawable.ic_flood
            DisasterTypes.EARTHQUAKE.value -> R.drawable.ic_earthquake
            DisasterTypes.HAZE.value -> R.drawable.ic_haze
            DisasterTypes.FIRE.value -> R.drawable.ic_fire
            DisasterTypes.WIND.value -> R.drawable.ic_wind
            DisasterTypes.VOLCANO.value -> R.drawable.ic_volcano
            else -> R.drawable.ic_disaster
        }
    }
}
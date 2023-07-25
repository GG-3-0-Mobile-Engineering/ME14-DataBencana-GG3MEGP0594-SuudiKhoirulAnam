package com.ferosburn.databencana.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ferosburn.databencana.R
import com.ferosburn.databencana.data.DisasterTypes
import com.ferosburn.databencana.data.Provinces
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
        map = binding.mapView
        val mapController = map.controller
        map.apply {
            setTileSource(TileSourceFactory.OpenTopo)
            setMultiTouchControls(true)
            @Suppress("DEPRECATION")
            setBuiltInZoomControls(false)
        }
        mapController.setCenter(GeoPoint(DEFAULT_LATITUDE, DEFAULT_LONGITUDE))
        mapController.setZoom(DEFAULT_ZOOM_LEVEL)
        binding.tvFilter.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_filterFragment)
        }
        viewModel.apply {
            bbox.observe(viewLifecycleOwner) {
                val avgLong = averageCoordinate(it[0], it[2])
                val avgLat = averageCoordinate(it[1], it[3])
                mapController.setCenter(GeoPoint(avgLat, avgLong))
                mapController.setZoom(calculateZoomLevel(it[0], it[2]))
            }
            listDisaster.observe(viewLifecycleOwner) {
                map.overlays.clear()
                it.map { itemDisaster ->
                    val marker = Marker(map)
                    marker.position =
                        GeoPoint(itemDisaster.coordinates[1], itemDisaster.coordinates[0])
                    marker.icon = context?.getDrawable(getDisasterIcon(itemDisaster.disasterType))
                    marker.title = "${getDisasterName(itemDisaster.disasterType)}\n${
                        getProvinceName(itemDisaster.instanceRegionCode)
                    }"
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

    private fun calculateZoomLevel(minLong: Double, maxLong: Double): Double {
        return (maxLong - minLong) * SLOPE + CONSTANTA
    }

    private fun getDisasterIcon(disasterType: String): Int {
        return when (disasterType) {
            DisasterTypes.FLOOD.value -> R.drawable.ic_flood
            DisasterTypes.EARTHQUAKE.value -> R.drawable.ic_earthquake
            DisasterTypes.HAZE.value -> R.drawable.ic_haze
            DisasterTypes.FIRE.value -> R.drawable.ic_fire
            DisasterTypes.WIND.value -> R.drawable.ic_wind
            DisasterTypes.VOLCANO.value -> R.drawable.ic_volcano
            else -> R.drawable.ic_disaster
        }
    }

    private fun getDisasterName(disasterType: String): String {
        return when (disasterType) {
            DisasterTypes.FLOOD.value -> DisasterTypes.FLOOD.disaster
            DisasterTypes.EARTHQUAKE.value -> DisasterTypes.EARTHQUAKE.disaster
            DisasterTypes.HAZE.value -> DisasterTypes.HAZE.disaster
            DisasterTypes.FIRE.value -> DisasterTypes.FIRE.disaster
            DisasterTypes.WIND.value -> DisasterTypes.WIND.disaster
            DisasterTypes.VOLCANO.value -> DisasterTypes.VOLCANO.disaster
            else -> DisasterTypes.ALL.disaster
        }
    }

    private fun getProvinceName(regionCode: String): String {
        return when (regionCode) {
            Provinces.ACEH.code -> Provinces.ACEH.provinceName
            Provinces.BALI.code -> Provinces.BALI.provinceName
            Provinces.BANGKABELITUNG.code -> Provinces.BANGKABELITUNG.provinceName
            Provinces.BANTEN.code -> Provinces.BANTEN.provinceName
            Provinces.BENGKULU.code -> Provinces.BENGKULU.provinceName
            Provinces.GORONTALO.code -> Provinces.GORONTALO.provinceName
            Provinces.JABAR.code -> Provinces.JABAR.provinceName
            Provinces.JAKARTA.code -> Provinces.JAKARTA.provinceName
            Provinces.JAMBI.code -> Provinces.JAMBI.provinceName
            Provinces.JATENG.code -> Provinces.JATENG.provinceName
            Provinces.JATIM.code -> Provinces.JATIM.provinceName
            Provinces.KALBAR.code -> Provinces.KALBAR.provinceName
            Provinces.KALSEL.code -> Provinces.KALSEL.provinceName
            Provinces.KALTARA.code -> Provinces.KALTARA.provinceName
            Provinces.KALTENG.code -> Provinces.KALTENG.provinceName
            Provinces.KALTIM.code -> Provinces.KALTIM.provinceName
            Provinces.KEPRI.code -> Provinces.KEPRI.provinceName
            Provinces.LAMPUNG.code -> Provinces.LAMPUNG.provinceName
            Provinces.MALUKU.code -> Provinces.MALUKU.provinceName
            Provinces.MALUT.code -> Provinces.MALUT.provinceName
            Provinces.NTB.code -> Provinces.NTB.provinceName
            Provinces.NTT.code -> Provinces.NTT.provinceName
            Provinces.PABAR.code -> Provinces.PABAR.provinceName
            Provinces.PAPUA.code -> Provinces.PAPUA.provinceName
            Provinces.RIAU.code -> Provinces.RIAU.provinceName
            Provinces.SULBAR.code -> Provinces.SULBAR.provinceName
            Provinces.SULSEL.code -> Provinces.SULSEL.provinceName
            Provinces.SULTENG.code -> Provinces.SULTENG.provinceName
            Provinces.SULTRA.code -> Provinces.SULTRA.provinceName
            Provinces.SULUT.code -> Provinces.SULUT.provinceName
            Provinces.SUMBAR.code -> Provinces.SUMBAR.provinceName
            Provinces.SUMSEL.code -> Provinces.SUMSEL.provinceName
            Provinces.SUMUT.code -> Provinces.SUMUT.provinceName
            Provinces.YOGYA.code -> Provinces.YOGYA.provinceName
            else -> getString(R.string.indonesia)
        }
    }

    companion object {
        const val DEFAULT_LATITUDE: Double = -2.483383
        const val DEFAULT_LONGITUDE: Double = 117.890285
        const val DEFAULT_ZOOM_LEVEL: Double = 5.0
        const val SLOPE: Double = -0.71
        const val CONSTANTA: Double = 17.0
    }
}
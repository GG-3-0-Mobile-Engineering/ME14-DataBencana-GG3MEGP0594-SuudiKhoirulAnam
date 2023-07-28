package com.ferosburn.databencana.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferosburn.databencana.R
import com.ferosburn.databencana.data.DisasterTypes
import com.ferosburn.databencana.databinding.FragmentHomeBinding
import com.ferosburn.databencana.util.KeyConstant
import com.ferosburn.databencana.util.disasterValueToDisasterTypes
import com.ferosburn.databencana.util.localDateToFormattedDateTime
import com.ferosburn.databencana.util.provinceCodeToProvinces
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: MapView
    private lateinit var mapController: IMapController
    private var avgLong: Double = DEFAULT_LONGITUDE
    private var avgLat: Double = DEFAULT_LATITUDE
    private val disasterListAdapter: DisasterListAdapter = DisasterListAdapter()

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
        initMap()
        binding.apply {
            initAdapter()
        }
        binding.tvFilter.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_filterFragment)
        }
        BottomSheetBehavior.from(binding.disasterListBottomSheet.root)
            .apply {
                peekHeight = 0
                this.state = BottomSheetBehavior.STATE_HIDDEN
                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (state) {
                            BottomSheetBehavior.STATE_COLLAPSED -> {
                                binding.disasterListBottomSheet.layoutDisasterListBottomSheet.radius = 24f
                                binding.disasterListBottomSheet.tvTitle.visibility = View.GONE
                                binding.cvFilter.visibility = View.VISIBLE
                            }
                            BottomSheetBehavior.STATE_DRAGGING -> {
                                binding.disasterListBottomSheet.layoutDisasterListBottomSheet.radius = 24f
                                binding.disasterListBottomSheet.tvTitle.visibility = View.VISIBLE
                                binding.cvFilter.visibility = View.VISIBLE
                            }
                            BottomSheetBehavior.STATE_EXPANDED -> {
                                binding.disasterListBottomSheet.layoutDisasterListBottomSheet.radius = 0f
                                binding.disasterListBottomSheet.tvTitle.visibility = View.VISIBLE
                                binding.cvFilter.visibility = View.GONE
                            }
                            else -> return
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                })
            }
    }

    private fun initMap() {
        map = binding.mapView
        mapController = map.controller
        map.apply {
            setTileSource(TileSourceFactory.OpenTopo)
            setMultiTouchControls(true)
            @Suppress("DEPRECATION")
            (setBuiltInZoomControls(false))
        }
        mapController.setCenter(GeoPoint(DEFAULT_LATITUDE, DEFAULT_LONGITUDE))
        mapController.setZoom(DEFAULT_ZOOM_LEVEL)
    }

    private fun FragmentHomeBinding.initAdapter() {
        disasterListBottomSheet.rvListDisaster.adapter = disasterListAdapter
        disasterListBottomSheet.rvListDisaster.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onResume() {
        super.onResume()
        val disasterType = arguments?.getString(KeyConstant.DISASTER_TYPE_FILTER)
        val startDate = arguments?.getString(KeyConstant.START_DATE_FILTER)
            ?.localDateToFormattedDateTime("dd-MM-yyyy")
        val endDate = arguments?.getString(KeyConstant.END_DATE_FILTER)
            ?.localDateToFormattedDateTime("dd-MM-yyyy")
        val province = arguments?.getString(KeyConstant.PROVINCE_FILTER)

        viewModel.apply {
            if (!disasterType.isNullOrBlank() || (!startDate.isNullOrBlank() && !endDate.isNullOrBlank()) || !province.isNullOrBlank()) {
                fetchFilteredList(disasterType, startDate, endDate, province)
            } else if (isListEmpty()) {
                fetchDisasterData()
            }
            status.observe(viewLifecycleOwner) {
                when (it) {
                    DataStatus.ERROR -> {
                        disasterData.observe(viewLifecycleOwner) {data ->
                            when (data.statusCode) {
                                in 500..599 -> {
                                    Toast.makeText(context, "Server Bermasalah", Toast.LENGTH_LONG)
                                        .show()
                                }
                                in 400..499 -> {
                                    Toast.makeText(context, "Ada Kesalahan Aplikasi", Toast.LENGTH_LONG)
                                        .show()
                                }
                                else -> {
                                    Toast.makeText(context, "Tidak Dapat Terhubung Ke Server", Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                        }
                    }

                    DataStatus.LOADING -> {
                        Toast.makeText(context, "Memuat ...", Toast.LENGTH_SHORT)
                            .show()
                    }

                    DataStatus.DONE -> {
                        Toast.makeText(context, "Berhasil", Toast.LENGTH_SHORT)
                            .show()
                        bbox.observe(viewLifecycleOwner) { coordinates ->
                            avgLong = averageCoordinate(coordinates[0], coordinates[2])
                            avgLat = averageCoordinate(coordinates[1], coordinates[3])
                            mapController.setCenter(GeoPoint(avgLat, avgLong))
                            mapController.zoomTo(
                                calculateZoomLevel(coordinates[0], coordinates[2]),
                                1000
                            )
                        }
                        listDisaster.observe(viewLifecycleOwner) { list ->
                            if (list.isEmpty()) {
                                Toast.makeText(context, "Tidak Ada Data", Toast.LENGTH_SHORT)
                                    .show()
                                BottomSheetBehavior.from(binding.disasterListBottomSheet.root)
                                    .apply {
                                        peekHeight = 0
                                        this.state = BottomSheetBehavior.STATE_HIDDEN
                                    }
                            } else {
                                binding.disasterListBottomSheet.layoutDisasterListBottomSheet.visibility = View.VISIBLE
                                BottomSheetBehavior.from(binding.disasterListBottomSheet.root).apply {
                                    peekHeight = 150
                                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                                }
                            }
                            map.overlays.clear()
                            disasterListAdapter.submitList(list)
                            list.map { itemDisaster ->
                                val marker = Marker(map)
                                marker.position =
                                    GeoPoint(
                                        itemDisaster.coordinates[1],
                                        itemDisaster.coordinates[0]
                                    )
                                marker.icon =
                                    context?.getDrawable(getDisasterIcon(itemDisaster.disasterType))
                                marker.title =
                                    "${itemDisaster.disasterType.disasterValueToDisasterTypes()?.disasterName}\n${
                                        itemDisaster.instanceRegionCode.provinceCodeToProvinces()?.provinceName
                                    }"
                                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                                map.overlays.add(marker)
                            }
                            map.invalidate()
                        }
                    }

                    else -> return@observe
                }
            }
        }

        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun averageCoordinate(value1: Double, value2: Double): Double {
        return (value1 + value2) / 2
    }

    private fun calculateZoomLevel(minLong: Double, maxLong: Double): Double {
        return CURVATURE_DEGREE / ((maxLong - minLong) + CURVATURE_X_OFFSET) + CURVATURE_INITIAL_VALUE
    }

    private fun getDisasterIcon(disasterType: String): Int {
        return when (disasterType) {
            DisasterTypes.FLOOD.disasterValue -> R.drawable.ic_flood
            DisasterTypes.EARTHQUAKE.disasterValue -> R.drawable.ic_earthquake
            DisasterTypes.HAZE.disasterValue -> R.drawable.ic_haze
            DisasterTypes.FIRE.disasterValue -> R.drawable.ic_fire
            DisasterTypes.WIND.disasterValue -> R.drawable.ic_wind
            DisasterTypes.VOLCANO.disasterValue -> R.drawable.ic_volcano
            else -> R.drawable.ic_disaster
        }
    }

    companion object {
        const val DEFAULT_LATITUDE: Double = -2.483383
        const val DEFAULT_LONGITUDE: Double = 117.890285
        const val DEFAULT_ZOOM_LEVEL: Double = 5.0
        const val CURVATURE_INITIAL_VALUE: Double = 3.0
        const val CURVATURE_DEGREE: Double = 50.0
        const val CURVATURE_X_OFFSET: Double = 3.6
    }
}
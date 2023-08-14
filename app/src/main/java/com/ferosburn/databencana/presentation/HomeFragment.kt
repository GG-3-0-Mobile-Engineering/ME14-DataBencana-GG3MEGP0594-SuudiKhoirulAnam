package com.ferosburn.databencana.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ferosburn.databencana.R
import com.ferosburn.databencana.data.Resource
import com.ferosburn.databencana.databinding.FragmentHomeBinding
import com.ferosburn.databencana.domain.model.DisasterModel
import com.ferosburn.databencana.network.DisasterTypes
import com.ferosburn.databencana.utils.KeyConstant
import com.ferosburn.databencana.utils.disasterValueToDisasterTypes
import com.ferosburn.databencana.utils.localDateToFormattedDateTime
import com.ferosburn.databencana.utils.provinceCodeToProvinces
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: MapView
    private lateinit var mapController: IMapController
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
        val disasterType = arguments?.getString(KeyConstant.DISASTER_TYPE_FILTER)
        val startDate = arguments?.getString(KeyConstant.START_DATE_FILTER)
            ?.localDateToFormattedDateTime("dd-MM-yyyy")
        val endDate = arguments?.getString(KeyConstant.END_DATE_FILTER)
            ?.localDateToFormattedDateTime("dd-MM-yyyy")
        val province = arguments?.getString(KeyConstant.PROVINCE_FILTER)
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
        if (!disasterType.isNullOrBlank() || (!startDate.isNullOrBlank() && !endDate.isNullOrBlank()) || !province.isNullOrBlank()) {
            if (startDate.isNullOrBlank() || endDate.isNullOrBlank()) {
                getRecentReports(DAY_TIME_PERIOD * 2, province, disasterType)
            } else {
                getReports(startDate, endDate, province, disasterType)
            }
        } else {
            getRecentReports(DAY_TIME_PERIOD * 2)
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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onResume() {
        super.onResume()
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

    private fun getRecentReports(
        timePeriod: Int,
        provinceCode: String? = null,
        disasterValue: String? = null
    ) {
        viewModel.getRecentReports(timePeriod, provinceCode, disasterValue)
            .observe(viewLifecycleOwner) { disasters ->
                if (disasters != null) {
                    when (disasters) {
                        is Resource.Loading -> {
                            Toast.makeText(context, "Memuat ...", Toast.LENGTH_SHORT)
                                .show()
                        }

                        is Resource.Error -> {
                            Toast.makeText(context, "Ada Kesalahan", Toast.LENGTH_LONG)
                                .show()
                        }

                        is Resource.Success -> {
                            val list = disasters.data
                            if (list.isNullOrEmpty()) {
                                Toast.makeText(context, "Tidak Ada Data", Toast.LENGTH_SHORT)
                                    .show()
                                return@observe
                            }
                            map.overlays.clear()
                            disasterListAdapter.submitList(list)
                            showMapMarkers(list)
                            showDisasterBottomSheet(list)
                        }
                    }
                }
            }
    }

    private fun getReports(
        startTime: String,
        endTime: String,
        provinceCode: String? = null,
        disasterValue: String? = null
    ) {
        viewModel.getReports(startTime, endTime, provinceCode)
            .observe(viewLifecycleOwner) { disasters ->
                if (disasters != null) {
                    when (disasters) {
                        is Resource.Loading -> {
                            Toast.makeText(context, "Memuat ...", Toast.LENGTH_SHORT)
                                .show()
                        }

                        is Resource.Error -> {
                            Toast.makeText(context, "Ada Kesalahan", Toast.LENGTH_LONG)
                                .show()
                        }

                        is Resource.Success -> {
                            val list = if (disasterValue.isNullOrBlank()) {
                                disasters.data
                            } else {
                                disasters.data?.filter { item -> item.disasterType == disasterValue }
                            }
                            if (list.isNullOrEmpty()) {
                                Toast.makeText(context, "Tidak Ada Data", Toast.LENGTH_SHORT)
                                    .show()
                                return@observe
                            }
                            map.overlays.clear()
                            disasterListAdapter.submitList(list)
                            showMapMarkers(list)
                            showDisasterBottomSheet(list)
                        }
                    }
                }
            }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showMapMarkers(data: List<DisasterModel>) {
        if (data.isNotEmpty()) {
            with(data) {
                val north = this.maxBy { it.latitude }.latitude + 1
                val east = this.maxBy { it.longitude }.longitude + 1
                val south = this.minBy { it.latitude }.latitude - 1
                val west = this.minBy { it.longitude }.longitude - 1
                val b = BoundingBox(north, east, south, west)
                map.zoomToBoundingBox(b, true)
                this.map { itemDisaster ->
                    val marker = Marker(map)
                    marker.position =
                        GeoPoint(
                            itemDisaster.latitude,
                            itemDisaster.longitude
                        )
                    marker.icon = context?.getDrawable(getDisasterIcon(itemDisaster.disasterType))
                    marker.title =
                        "${itemDisaster.disasterType.disasterValueToDisasterTypes()?.disasterName}\n${
                            itemDisaster.instanceRegionCode?.provinceCodeToProvinces()?.provinceName
                        }"
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    map.overlays.add(marker)
                }
            }
        }
        map.invalidate()
    }

    private fun showDisasterBottomSheet(data: List<DisasterModel>) {
        if (data.isEmpty()) {
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
        const val DAY_TIME_PERIOD = 86400
        const val DEFAULT_LATITUDE: Double = -2.483383
        const val DEFAULT_LONGITUDE: Double = 117.890285
        const val DEFAULT_ZOOM_LEVEL: Double = 5.0
    }
}
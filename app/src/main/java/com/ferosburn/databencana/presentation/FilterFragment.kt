package com.ferosburn.databencana.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ferosburn.databencana.R
import com.ferosburn.databencana.data.DisasterTypes
import com.ferosburn.databencana.data.Provinces
import com.ferosburn.databencana.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.initBinding(view)
    }

    private fun FragmentFilterBinding.initBinding(v: View) {
        btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_nav_home)
        }
        val disasterAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            setOfDisasterTypes().map { it.disaster })
        (disasterType.editText as AutoCompleteTextView).setAdapter(disasterAdapter)
        val provinceAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            setOfProvinces().map { it.provinceName })
        (inputLayoutRegion.editText as AutoCompleteTextView).setAdapter(provinceAdapter)
    }

    private fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment()
        fragmentManager?.let { newFragment.show(it, "datePicker") }
    }

    private fun setOfDisasterTypes(): Set<DisasterTypes> {
        return setOf(
            DisasterTypes.ALL,
            DisasterTypes.FLOOD,
            DisasterTypes.EARTHQUAKE,
            DisasterTypes.FIRE,
            DisasterTypes.HAZE,
            DisasterTypes.WIND,
            DisasterTypes.VOLCANO,
        )
    }

    private fun setOfProvinces(): Set<Provinces> {
        return setOf(
            Provinces.ACEH,
            Provinces.BALI,
            Provinces.BANGKABELITUNG,
            Provinces.BANTEN,
            Provinces.BENGKULU,
            Provinces.GORONTALO,
            Provinces.JABAR,
            Provinces.JAKARTA,
            Provinces.JAMBI,
            Provinces.JATENG,
            Provinces.JATIM,
            Provinces.KALBAR,
            Provinces.KALSEL,
            Provinces.KALTARA,
            Provinces.KALTENG,
            Provinces.KALTIM,
            Provinces.KEPRI,
            Provinces.LAMPUNG,
            Provinces.MALUKU,
            Provinces.MALUT,
            Provinces.NTB,
            Provinces.NTT,
            Provinces.PABAR,
            Provinces.PAPUA,
            Provinces.RIAU,
            Provinces.SULBAR,
            Provinces.SULSEL,
            Provinces.SULTENG,
            Provinces.SULTRA,
            Provinces.SULUT,
            Provinces.SUMBAR,
            Provinces.SUMSEL,
            Provinces.SUMUT,
            Provinces.YOGYA,
        )
    }

}
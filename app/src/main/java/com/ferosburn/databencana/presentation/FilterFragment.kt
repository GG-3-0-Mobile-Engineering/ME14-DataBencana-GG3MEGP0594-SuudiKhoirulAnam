package com.ferosburn.databencana.presentation

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.widget.doBeforeTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ferosburn.databencana.R
import com.ferosburn.databencana.data.DisasterTypes
import com.ferosburn.databencana.data.Provinces
import com.ferosburn.databencana.databinding.FragmentFilterBinding
import com.ferosburn.databencana.util.KeyConstant
import com.ferosburn.databencana.util.disasterNameToDisasterTypes
import com.ferosburn.databencana.util.provinceNameToProvinces
import com.ferosburn.databencana.util.setInputError
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private var calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.initBinding(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun FragmentFilterBinding.initBinding(v: View) {
        btnFilter.setOnClickListener {
            val disasterTypes =
                if (disasterTypeAutocomplete.text.toString() == DisasterTypes.ALL.disasterName) {
                    null
                } else {
                    disasterTypeAutocomplete.text.toString()
                        .disasterNameToDisasterTypes()?.disasterValue
                }
            val bundle = bundleOf(
                KeyConstant.DISASTER_TYPE_FILTER to disasterTypes,
                KeyConstant.START_DATE_FILTER to inputStartTime.text.toString(),
                KeyConstant.END_DATE_FILTER to inputEndTime.text.toString(),
                KeyConstant.PROVINCE_FILTER to inputRegion.text.toString()
                    .provinceNameToProvinces()?.code
            )
            if (isFilterValid()) {
                findNavController().navigate(R.id.action_filterFragment_to_nav_home, bundle)
            }
        }

        // set adapter for disaster type dropdown menu
        val disasterAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            setOfDisasterTypes().map { it.disasterName })
        (disasterType.editText as AutoCompleteTextView).setAdapter(disasterAdapter)
        disasterTypeAutocomplete.setText(DisasterTypes.ALL.disasterName, false)

        // set adapter for province autocomplete text input
        val provinceAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            setOfProvinces().map { it.provinceName })
        (inputLayoutRegion.editText as AutoCompleteTextView).setAdapter(provinceAdapter)

        inputLayoutStartTime.setEndIconOnClickListener {
            showDatePickerDialog(inputStartTime)
        }
        inputLayoutEndTime.setEndIconOnClickListener {
            showDatePickerDialog(inputEndTime)
        }
        inputRegion.doBeforeTextChanged { _, _, _, _ ->
            inputLayoutRegion.isErrorEnabled = false
        }
        inputStartTime.doBeforeTextChanged { _, _, _, _ ->
            inputLayoutStartTime.isErrorEnabled = false
        }
        inputEndTime.doBeforeTextChanged { _, _, _, _ ->
            inputLayoutEndTime.isErrorEnabled = false
        }
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_filterFragment_to_nav_home)
                }
            })


    }

    private fun showDatePickerDialog(view: EditText) {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(view)
            }
        DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        ).apply {
            if (view == binding.inputStartTime) {
                datePicker.maxDate = Date().time - MILIS_LONG_ONE_DAY
            } else {
                datePicker.maxDate = Date().time
            }
            show()
        }
    }

    private fun updateDateInView(view: EditText) {
        view.setText(SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(calendar.time))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun FragmentFilterBinding.isFilterValid(): Boolean {
        if (inputRegion.text.isNotBlank() && inputRegion.text.toString()
                .provinceNameToProvinces() == null
        ) {
            inputLayoutRegion.setInputError(getString(R.string.province_is_not_found))
            return false
        } else if (inputStartTime.text.toString().isNotBlank() xor inputEndTime.text.toString()
                .isNotBlank()
        ) {
            if (inputStartTime.text.isNullOrBlank()) {
                inputLayoutStartTime.setInputError(getString(R.string.start_date_should_not_be_empty))
            }
            if (inputEndTime.text.isNullOrBlank()) {
                inputLayoutEndTime.setInputError(getString(R.string.end_date_should_not_be_empty))
            }
            return false
        } else if (inputStartTime.text.toString().isNotBlank() && inputEndTime.text.toString()
                .isNotBlank()
        ) {
            if (LocalDate.parse(
                    inputEndTime.text.toString(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                ) > LocalDate.now()
            ) {
                inputLayoutEndTime.setInputError(getString(R.string.end_date_should_not_be_bigger_than_today))
                return false
            }
            if (LocalDate.parse(
                    inputStartTime.text.toString(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                ) > LocalDate.parse(
                    inputEndTime.text.toString(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                )
            ) {
                inputLayoutStartTime.setInputError(getString(R.string.start_date_should_not_be_bigger_than_end_date))
                return false
            }
            if ((LocalDate.parse(
                    inputEndTime.text.toString(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                ).toEpochDay() - LocalDate.parse(
                    inputStartTime.text.toString(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                ).toEpochDay()) * 60 * 60 * 24 >= 18748800
            ) {
                inputLayoutStartTime.setInputError(getString(R.string.time_difference_not_more_than_217_days))
                return false
            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    companion object {
        const val MILIS_LONG_ONE_DAY = 86400000L
    }
}
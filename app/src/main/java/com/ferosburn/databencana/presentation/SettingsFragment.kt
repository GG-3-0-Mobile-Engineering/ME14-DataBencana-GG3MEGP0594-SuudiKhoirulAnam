package com.ferosburn.databencana.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.ferosburn.databencana.databinding.FragmentSettingsBinding
import com.ferosburn.databencana.util.KeyConstant

class SettingsFragment: Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPreferences = activity?.getSharedPreferences(KeyConstant.PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        binding.switchDarkMode.apply {
            when(AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_NO -> isChecked = false
                AppCompatDelegate.MODE_NIGHT_YES -> isChecked = true
            }
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor?.putInt(KeyConstant.THEME, AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor?.putInt(KeyConstant.THEME, AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.taskmaker.ui.settings


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.taskmaker.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val themeMode = preferenceManager.findPreference<Preference>(getString(R.string.pref_key_theme)) as ListPreference

        themeMode.setOnPreferenceChangeListener { _ , newValue ->

            val mode: Int = when (newValue){
                "on" -> MODE_NIGHT_YES
                "off"-> MODE_NIGHT_NO
                else -> MODE_NIGHT_FOLLOW_SYSTEM
            }
            updateTheme(mode)
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }

}

/*
 * Copyright (c) 2016 Markus Ressel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.markusressel.android.tutorialtooltip

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.EditTextPreference
import android.preference.PreferenceFragment
import android.support.annotation.ColorInt
import android.support.v4.content.LocalBroadcastManager

import com.rarepebble.colorpicker.ColorPreference

/**
 * Simple preferences page
 *
 *
 * Created by Markus on 10.11.2016.
 */
class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var startColor: ColorPreference
    private lateinit var endColor: ColorPreference
    private lateinit var startDiameter: EditTextPreference
    private lateinit var targetDiameter: EditTextPreference
    private lateinit var strokeWidth: EditTextPreference
    private lateinit var delayBetweenWaves: EditTextPreference
    private lateinit var duration: EditTextPreference
    private lateinit var waveCount: EditTextPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set preferences file name
        preferenceManager.sharedPreferencesName = PreferencesHelper.SHARED_PREFS_NAME

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences)

        // get references and update summaries
        initPreferences()
    }

    private fun initPreferences() {
        startColor = findPreference(getString(R.string.key_startColor)) as ColorPreference
        endColor = findPreference(getString(R.string.key_endColor)) as ColorPreference

        startDiameter = findPreference(getString(R.string.key_startDiameter)) as EditTextPreference
        targetDiameter = findPreference(getString(R.string.key_targetDiameter)) as EditTextPreference

        strokeWidth = findPreference(getString(R.string.key_strokeWidth)) as EditTextPreference

        delayBetweenWaves = findPreference(getString(R.string.key_delayBetweenWaves)) as EditTextPreference
        duration = findPreference(getString(R.string.key_duration)) as EditTextPreference

        waveCount = findPreference(getString(R.string.key_waveCount)) as EditTextPreference

        updateSummaries()
    }

    private fun updateSummaries() {
        startColor.summary = colorToHex(startColor.color)
        endColor.summary = colorToHex(endColor.color)

        startDiameter.summary = getString(R.string.summary_dp_value, startDiameter.text)
        targetDiameter.summary = getString(R.string.summary_dp_value, targetDiameter.text)

        strokeWidth.summary = getString(R.string.summary_dp_value, strokeWidth.text)

        delayBetweenWaves.summary = getString(R.string.summary_ms_value,
                delayBetweenWaves.text)
        duration.summary = getString(R.string.summary_ms_value, duration.text)

        waveCount.summary = waveCount.text
    }

    /**
     * Converts a color int to an #AARRGGBB hex string representation
     */
    private fun colorToHex(@ColorInt color: Int): String {
        return String.format("#%08X", color)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        updateSummaries()

        notifyPreferenceChanged(activity, key)
    }

    override fun onResume() {
        super.onResume()

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)

        super.onPause()
    }

    companion object {

        val INTENT_ACTION_PREFERENCE_CHANGED = "preference_changed"
        val KEY_PREFERENCE_KEY = "preferenceKey"

        fun newInstance(): SettingsFragment {
            val args = Bundle()
            val fragment = SettingsFragment()
            fragment.arguments = args
            return fragment
        }

        /**
         * Notifies the MainActivity that settings have changed

         * @param context application context
         * *
         * @param key     prefrence key that has changed
         */
        private fun notifyPreferenceChanged(context: Context, key: String) {
            val intent = Intent(INTENT_ACTION_PREFERENCE_CHANGED)
            intent.putExtra(KEY_PREFERENCE_KEY, key)

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }
}
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

package de.markusressel.android.tutorialtooltip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.ColorInt;
import android.support.v4.content.LocalBroadcastManager;

import com.rarepebble.colorpicker.ColorPreference;

/**
 * Simple preferences page
 * <p>
 * Created by Markus on 10.11.2016.
 */
public class SettingsFragment extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String INTENT_ACTION_PREFERENCE_CHANGED = "preference_changed";
    public static final String KEY_PREFERENCE_KEY = "preferenceKey";
    private ColorPreference startColor;
    private ColorPreference endColor;
    private EditTextPreference startDiameter;
    private EditTextPreference targetDiameter;
    private EditTextPreference strokeWidth;
    private EditTextPreference delayBetweenWaves;
    private EditTextPreference duration;
    private EditTextPreference waveCount;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Notifies the MainActivity that settings have changed
     *
     * @param context application context
     * @param key     prefrence key that has changed
     */
    private static void notifyPreferenceChanged(Context context, String key) {
        Intent intent = new Intent(INTENT_ACTION_PREFERENCE_CHANGED);
        intent.putExtra(KEY_PREFERENCE_KEY, key);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set preferences file name
        getPreferenceManager().setSharedPreferencesName(PreferencesHelper.SHARED_PREFS_NAME);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        initPreferences();
    }

    private void initPreferences() {
        startColor = (ColorPreference) findPreference(getString(R.string.key_startColor));
        endColor = (ColorPreference) findPreference(getString(R.string.key_endColor));

        startDiameter = (EditTextPreference) findPreference(getString(R.string.key_startDiameter));
        targetDiameter = (EditTextPreference) findPreference(getString(R.string.key_targetDiameter));

        strokeWidth = (EditTextPreference) findPreference(getString(R.string.key_strokeWidth));

        delayBetweenWaves = (EditTextPreference) findPreference(getString(R.string.key_delayBetweenWaves));
        duration = (EditTextPreference) findPreference(getString(R.string.key_duration));

        waveCount = (EditTextPreference) findPreference(getString(R.string.key_waveCount));

        updateSummaries();
    }

    private void updateSummaries() {
        startColor.setSummary(colorToHex(startColor.getColor()));
        endColor.setSummary(colorToHex(endColor.getColor()));

        startDiameter.setSummary(getString(R.string.summary_dp_value, startDiameter.getText()));
        targetDiameter.setSummary(getString(R.string.summary_dp_value, targetDiameter.getText()));

        strokeWidth.setSummary(getString(R.string.summary_dp_value, strokeWidth.getText()));

        delayBetweenWaves.setSummary(getString(R.string.summary_ms_value,
                delayBetweenWaves.getText()));
        duration.setSummary(getString(R.string.summary_ms_value, duration.getText()));

        waveCount.setSummary(waveCount.getText());
    }

    /**
     * Converts a color int to an #AARRGGBB hex string representation
     */
    private String colorToHex(@ColorInt int color) {
        return String.format("#%08X", (color));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateSummaries();

        notifyPreferenceChanged(getActivity(), key);
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();
    }
}
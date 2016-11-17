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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import de.markusressel.android.library.tutorialtooltip.CircleWaveAlertView;

public class MainActivity extends AppCompatActivity {

    private CircleWaveAlertView circleWaveAlertView;

    private FrameLayout preferenceFrame;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        circleWaveAlertView = (CircleWaveAlertView) findViewById(R.id.circleWaveAlertView);

        // Load Settings view
        getFragmentManager().beginTransaction()
                .replace(R.id.preferenceFrame, SettingsFragment.newInstance())
                .commit();


        // load settings
        initFromPreferenceValues();

        // this receiver will update the view if a preference has changed
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (SettingsFragment.INTENT_ACTION_PREFERENCE_CHANGED.equals(intent.getAction())) {
                    String key = intent.getStringExtra(SettingsFragment.KEY_PREFERENCE_KEY);

                    //                    if (getString(R.string.key_activeIndicatorFillColor).equals(key)) {
                    //                        @ColorInt int activeIndicatorColorFill = PreferencesHelper.getColor(getApplicationContext(), R.string.key_activeIndicatorFillColor, getResources().getColor(R.color.default_value_activeIndicatorFillColor));
                    //                        pageIndicatorView.setActiveIndicatorFillColor(activeIndicatorColorFill);
                    //                    } else if (getString(R.string.key_activeIndicatorStrokeColor).equals(key)) {
                    //                        @ColorInt int activeIndicatorColorStroke = PreferencesHelper.getColor(getApplicationContext(), R.string.key_activeIndicatorStrokeColor, getResources().getColor(R.color.default_value_activeIndicatorStrokeColor));
                    //                        pageIndicatorView.setActiveIndicatorStrokeColor(activeIndicatorColorStroke);
                    //                    } else if (getString(R.string.key_activeIndicatorStrokeWidth).equals(key)) {
                    //                        float activeIndicatorStrokeWidth = PreferencesHelper.getDimen(getApplicationContext(), R.string.key_activeIndicatorStrokeWidth, R.dimen.default_value_activeIndicatorStrokeWidth);
                    //                        pageIndicatorView.setActiveIndicatorStrokeWidth(Math.round(pxFromDp(getApplicationContext(), activeIndicatorStrokeWidth)));
                    //                    } else if (getString(R.string.key_activeIndicatorFillSize).equals(key)) {
                    //                        float activeIndicatorSize = PreferencesHelper.getDimen(getApplicationContext(), R.string.key_activeIndicatorFillSize, R.dimen.default_value_activeIndicatorFillSize);
                    //                        pageIndicatorView.setActiveIndicatorSize(Math.round(pxFromDp(getApplicationContext(), activeIndicatorSize)));
                    //                    } else if (getString(R.string.key_inactiveIndicatorFillColor).equals(key)) {
                    //                        @ColorInt int inactiveIndicatorColorFill = PreferencesHelper.getColor(getApplicationContext(), R.string.key_inactiveIndicatorFillColor, getResources().getColor(R.color.default_value_inactiveIndicatorFillColor));
                    //                        pageIndicatorView.setInactiveIndicatorFillColor(inactiveIndicatorColorFill);
                    //                    } else if (getString(R.string.key_inactiveIndicatorStrokeColor).equals(key)) {
                    //                        @ColorInt int inactiveIndicatorColorStroke = PreferencesHelper.getColor(getApplicationContext(), R.string.key_inactiveIndicatorStrokeColor, getResources().getColor(R.color.default_value_inactiveIndicatorStrokeColor));
                    //                        pageIndicatorView.setInactiveIndicatorStrokeColor(inactiveIndicatorColorStroke);
                    //                    } else if (getString(R.string.key_inactiveIndicatorStrokeWidth).equals(key)) {
                    //                        float inactiveIndicatorStrokeWidth = PreferencesHelper.getDimen(getApplicationContext(), R.string.key_inactiveIndicatorStrokeWidth, R.dimen.default_value_inactiveIndicatorStrokeWidth);
                    //                        pageIndicatorView.setInactiveIndicatorStrokeWidth(Math.round(pxFromDp(getApplicationContext(), inactiveIndicatorStrokeWidth)));
                    //                    } else if (getString(R.string.key_inactiveIndicatorFillSize).equals(key)) {
                    //                        float inactiveIndicatorSize = PreferencesHelper.getDimen(getApplicationContext(), R.string.key_inactiveIndicatorFillSize, R.dimen.default_value_inactiveIndicatorFillSize);
                    //                        pageIndicatorView.setInactiveIndicatorSize(Math.round(pxFromDp(getApplicationContext(), inactiveIndicatorSize)));
                    //
                    //                    } else if (getString(R.string.key_indicatorGap).equals(key)) {
                    //                        float indicatorGap = PreferencesHelper.getDimen(getApplicationContext(), R.string.key_indicatorGap, R.dimen.default_value_indicatorGap);
                    //                        pageIndicatorView.setIndicatorGap(Math.round(pxFromDp(getApplicationContext(), indicatorGap)));
                    //                    } else if (getString(R.string.key_initialPageIndex).equals(key)) {
                    //                        int initialPageIndex = PreferencesHelper.getInteger(getApplicationContext(), R.string.key_initialPageIndex, R.integer.default_value_initialPageIndex);
                    //                        // TODO
                    //                    } else if (getString(R.string.key_pageCount).equals(key)) {
                    //                        int pageCount = PreferencesHelper.getInteger(getApplicationContext(), R.string.key_pageCount, R.integer.default_value_pageCount);
                    //                        customTabAdapter.setCount(pageCount);
                    //                        pageIndicatorView.setPageCount(pageCount);
                    //                    }
                }
            }
        };
    }

    private void initFromPreferenceValues() {
        circleWaveAlertView.setStartDiameter(0);
        circleWaveAlertView.setTargetDiameter(600);

        circleWaveAlertView.setStrokeWidth(3);

        circleWaveAlertView.setStartColor(Color.argb(255, 255, 0, 0));
        circleWaveAlertView.setEndColor(Color.argb(0, 255, 0, 0));

        circleWaveAlertView.setDelayBetweenWaves(300);
        circleWaveAlertView.setDuration(3000);

        circleWaveAlertView.setWaveCount(3);

        circleWaveAlertView.setCustomInterpolator(new FastOutSlowInInterpolator());

        //        @ColorInt int activeIndicatorColorFill =
        //                circleWaveAlertView.setColor(PreferencesHelper.getColor(getApplicationContext(),
        //                        R.string.key_activeIndicatorFillColor,
        //                        getResources().getColor(R.color.default_value_activeIndicatorFillColor)););
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SettingsFragment.INTENT_ACTION_PREFERENCE_CHANGED);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(broadcastReceiver);
        super.onStop();
    }
}

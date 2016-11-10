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

import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;

import de.markusressel.android.library.tutorialtooltip.CircleWaveAlertView;

public class MainActivity extends AppCompatActivity {

    private CircleWaveAlertView circleWaveAlertView;

    @ColorInt
    private int currentColor;
    private ValueAnimator colorAnimator;
    private ValueAnimator thicknessAnimator;
    private int currentThickness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        circleWaveAlertView = (CircleWaveAlertView) findViewById(R.id.circleWaveAlertView);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),
                        Color.argb(255, 255, 0, 0),
                        Color.argb(255, 0, 255, 0),
                        Color.argb(255, 0, 0, 255),
                        Color.argb(255, 255, 0, 0)
                );
                colorAnimator.setDuration(10000);
                colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
                colorAnimator.setRepeatMode(ValueAnimator.RESTART);
                colorAnimator.setInterpolator(new LinearInterpolator());
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        currentColor = (int) animation.getAnimatedValue();
                    }
                });


                thicknessAnimator = ValueAnimator.ofObject(new IntEvaluator(),
                        10, 50, 10
                );
                thicknessAnimator.setDuration(10000);
                thicknessAnimator.setRepeatCount(ValueAnimator.INFINITE);
                thicknessAnimator.setRepeatMode(ValueAnimator.RESTART);
                thicknessAnimator.setInterpolator(new LinearInterpolator());
                thicknessAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        currentThickness = (int) animation.getAnimatedValue();
                    }
                });

                colorAnimator.start();
                thicknessAnimator.start();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    while (true) {
                        Thread.sleep(16);

                        publishProgress();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                circleWaveAlertView.setColor(currentColor);
                circleWaveAlertView.setStrokeWidth(currentThickness);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                colorAnimator.cancel();
            }
        }.execute();
    }
}

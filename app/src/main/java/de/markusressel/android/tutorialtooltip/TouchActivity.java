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

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import de.markusressel.android.library.tutorialtooltip.TutorialTooltip;
import de.markusressel.android.library.tutorialtooltip.TutorialTooltipView;
import de.markusressel.android.library.tutorialtooltip.WaveIndicatorView;
import de.markusressel.android.library.tutorialtooltip.interfaces.OnTutorialTooltipClickedListener;
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipIndicator;
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage;

public class TouchActivity extends AppCompatActivity {

    private int tutorialId1;
    private int tutorialId2;
    private int tutorialId3;

    private Button button1;
    private Button button2;
    private FloatingActionButton fab;
    private Button button3;
    private Button button4;

    private TutorialTooltipView tutorialTooltipView;
    private int tutorialId4;
    private OnTutorialTooltipClickedListener onTutorialTooltipClickedListener;
    private Button buttonDialog;

    private static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        buttonDialog = (Button) findViewById(R.id.buttonDialog);

        final Activity activity = this;

        onTutorialTooltipClickedListener = new OnTutorialTooltipClickedListener() {

            @Override
            public boolean indicatorIsClickable() {
                return true;
            }

            @Override
            public void onIndicatorClicked(int id,
                    TutorialTooltipIndicator indicator, View indicatorView) {
                Toast.makeText(getApplicationContext(),
                        "Indicator " + id + " " + indicatorView.getWidth() + " clicked!",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean messageIsClickable() {
                return true;
            }

            @Override
            public void onMessageClicked(int id,
                    TutorialTooltipMessage indicator, View messageView) {
                Toast.makeText(getApplicationContext(),
                        "Message " + id + " " + messageView.getWidth() + " clicked!",
                        Toast.LENGTH_SHORT).show();
            }
        };

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TutorialTooltip.exists(activity, tutorialId1)) {
                    TutorialTooltip.remove(activity, tutorialId1);
                } else {
                    WaveIndicatorView waveIndicatorView = new WaveIndicatorView(activity);
                    waveIndicatorView.setStartColor(Color.argb(255, 255, 0, 0));
                    waveIndicatorView.setEndColor(Color.argb(0, 255, 0, 0));
                    waveIndicatorView.setStrokeWidth(pxFromDp(activity, 5));
                    waveIndicatorView.setTargetDiameter(pxFromDp(activity, 50));

                    tutorialId1 = TutorialTooltip.show(new TutorialTooltip.Builder(activity).text(
                            getString(R.string.tutorial_message_1),
                            TutorialTooltipView.Gravity.TOP)
                            .anchor(button1, TutorialTooltipView.Gravity.TOP)
                            .customIndicator(waveIndicatorView)
                            .onClickListener(onTutorialTooltipClickedListener)
                            .build());
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TutorialTooltip.exists(activity, tutorialId4)) {
                    TutorialTooltip.remove(activity, tutorialId4);
                } else {
                    tutorialId4 = TutorialTooltip.show(new TutorialTooltip.Builder(activity).text(
                            getString(R.string.tutorial_message_3),
                            TutorialTooltipView.Gravity.LEFT)
                            .anchor(button4)
                            .onClickListener(onTutorialTooltipClickedListener)
                            .build());
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TutorialTooltip.exists(activity, tutorialId2)) {
                    TutorialTooltip.remove(activity, tutorialId2);
                } else {
                    tutorialId2 = TutorialTooltip.show(new TutorialTooltip.Builder(activity).text(
                            getString(R.string.tutorial_message_2),
                            TutorialTooltipView.Gravity.BOTTOM)
                            .anchor(button2, TutorialTooltipView.Gravity.BOTTOM)
                            .onClickListener(onTutorialTooltipClickedListener)
                            .build());
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tutorialTooltipView != null && tutorialTooltipView.isShown()) {
                    tutorialTooltipView.remove();
                } else {
                    WaveIndicatorView waveIndicatorView = new WaveIndicatorView(activity);
                    waveIndicatorView.setStartColor(Color.argb(255, 255, 255, 255));
                    waveIndicatorView.setEndColor(Color.argb(0, 255, 255, 255));
                    waveIndicatorView.setStrokeWidth(pxFromDp(activity, 5));
                    waveIndicatorView.setTargetDiameter(pxFromDp(activity, 50));

                    tutorialTooltipView = TutorialTooltip.make(new TutorialTooltip.Builder(
                            activity).text(
                            getString(R.string.tutorial_message_fab),
                            TutorialTooltipView.Gravity.BOTTOM)
                            .anchor(fab)
                            .attachToWindow()
                            .customIndicator(waveIndicatorView)
//                            .onClickListener(onTutorialTooltipClickedListener)
                            .build());

                    tutorialId3 = TutorialTooltip.show(tutorialTooltipView);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TutorialTooltip.removeAll(activity);
            }
        });

        buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragmentTest dialogFragmentTest = DialogFragmentTest.newInstance();
                dialogFragmentTest.show(getSupportFragmentManager(), null);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            // When user touches the screen
            case MotionEvent.ACTION_DOWN:
                // Getting X coordinate
                float x = event.getX();
                // Getting Y Coordinate
                float y = event.getY();

                createTutorialTooltip(x, y);
                return true;
        }

        return super.onTouchEvent(event);
    }

    private void createTutorialTooltip(float x, float y) {
        TutorialTooltip.show(new TutorialTooltip.Builder(this).anchor(new Point((int) x, (int) y))
                .text("Test Tutorial Message", TutorialTooltipView.Gravity.TOP)
                .onClickListener(onTutorialTooltipClickedListener).build());
    }
}

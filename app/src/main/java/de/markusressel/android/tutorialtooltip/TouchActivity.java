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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import de.markusressel.android.library.tutorialtooltip.CircleWaveAlertView;
import de.markusressel.android.library.tutorialtooltip.TutorialTooltip;
import de.markusressel.android.library.tutorialtooltip.TutorialTooltipView;

public class TouchActivity extends AppCompatActivity {

    private CircleWaveAlertView circleWaveAlertView;

    private BroadcastReceiver broadcastReceiver;
    private int tutorialId1;
    private Button button1;
    private Button button2;
    private FloatingActionButton fab;
    private Button button3;

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

        final Activity activity = this;
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TutorialTooltip.exists(activity, tutorialId1)) {
                    TutorialTooltip.remove(activity, tutorialId1);
                } else {
                    CircleWaveAlertView circleWaveAlertView = new CircleWaveAlertView(activity);
                    circleWaveAlertView.setStartColor(Color.argb(255, 255, 0, 0));
                    circleWaveAlertView.setEndColor(Color.argb(0, 255, 0, 0));
                    circleWaveAlertView.setStrokeWidth(pxFromDp(activity, 5));
                    circleWaveAlertView.setTargetDiameter(pxFromDp(activity, 50));

                    tutorialId1 = TutorialTooltip.show(activity,
                            new TutorialTooltip.Builder().text(getString(R.string.tutorial_message_1),
                                    TutorialTooltipView.Gravity.TOP)
                                    .anchor(button1,
                                            TutorialTooltipView.Gravity.TOP
                                    )
                                    .customIndicator(circleWaveAlertView)
                                    .build());
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TutorialTooltip.removeAll(activity);
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
        TutorialTooltip.show(this,
                new TutorialTooltip.Builder().anchor(new Point((int) x, (int) y))
                        .text("Test Tutorial Message", TutorialTooltipView.Gravity.TOP).build());
    }
}

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

    private static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        Button button = (Button) findViewById(R.id.button);

        CircleWaveAlertView circleWaveAlertView = new CircleWaveAlertView(this);
        circleWaveAlertView.setStartColor(Color.argb(255, 255, 0, 0));
        circleWaveAlertView.setEndColor(Color.argb(0, 255, 0, 0));
        circleWaveAlertView.setStrokeWidth(pxFromDp(this, 5));
        circleWaveAlertView.setTargetDiameter(pxFromDp(this, 200));

        final int tutorialId = TutorialTooltip.show(this,
                new TutorialTooltip.Builder().text(getString(R.string.tutorial_message_1))
                        .anchor(button,
                                TutorialTooltipView.Gravity.CENTER)
                        .indicator(circleWaveAlertView)
                        .build());

        final Activity activity = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TutorialTooltip.remove(activity, tutorialId);
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
                new TutorialTooltip.Builder().anchor(new Point((int) x, (int) y),
                        TutorialTooltipView.Gravity.CENTER)
                        .text("Test Tutorial Message"));
    }
}

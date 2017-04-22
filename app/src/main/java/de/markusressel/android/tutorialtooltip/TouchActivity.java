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

import android.animation.ValueAnimator;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import de.markusressel.android.library.tutorialtooltip.TutorialTooltip;
import de.markusressel.android.library.tutorialtooltip.builder.IndicatorBuilder;
import de.markusressel.android.library.tutorialtooltip.builder.MessageBuilder;
import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipBuilder;
import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipChainBuilder;
import de.markusressel.android.library.tutorialtooltip.interfaces.OnIndicatorClickedListener;
import de.markusressel.android.library.tutorialtooltip.interfaces.OnMessageClickedListener;
import de.markusressel.android.library.tutorialtooltip.interfaces.OnTutorialTooltipClickedListener;
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipIndicator;
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage;
import de.markusressel.android.library.tutorialtooltip.view.CardMessageView;
import de.markusressel.android.library.tutorialtooltip.view.TooltipId;
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView;
import de.markusressel.android.library.tutorialtooltip.view.WaveIndicatorView;

public class TouchActivity extends AppCompatActivity {

    private TooltipId tutorialId1;
    private TooltipId tutorialId2;
    private TooltipId tutorialId3;

    private Button buttonCount;
    private Button buttonTop;
    private Button buttonBottom;
    private FloatingActionButton buttonFab;
    private Button buttonClear;
    private Button buttonCenter;

    private Button buttonShowLayout;
    private Button buttonHideLayout;

    private TutorialTooltipView tutorialTooltipView;
    private TooltipId tutorialId4;
    private Button buttonDialog;
    private OnTutorialTooltipClickedListener onTutorialTooltipClickedListener;
    private Button buttonChain;

    private static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        buttonCount = (Button) findViewById(R.id.button_count);
        buttonTop = (Button) findViewById(R.id.button_top);
        buttonBottom = (Button) findViewById(R.id.button_bottom);
        buttonFab = (FloatingActionButton) findViewById(R.id.button_fab);
        buttonCenter = (Button) findViewById(R.id.button_center);
        buttonDialog = (Button) findViewById(R.id.button_dialog);
        buttonChain = (Button) findViewById(R.id.button_chain);
        buttonClear = (Button) findViewById(R.id.button_clear_all);


        final ValueAnimator animator = ValueAnimator.ofFloat(0, 200);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                buttonHideLayout.setX((float) animation.getAnimatedValue());
            }
        });

        final LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout_right_bottom);
        buttonShowLayout = (Button) findViewById(R.id.button_show_layout);
        buttonShowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
                animator.start();
            }
        });
        buttonHideLayout = (Button) findViewById(R.id.button_hide_layout);
        buttonHideLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.INVISIBLE);
                animator.end();
            }
        });
        animator.start();

        TutorialTooltip.show(new TutorialTooltipBuilder(this).anchor(buttonHideLayout).build());

        final Activity activity = this;

        onTutorialTooltipClickedListener = new OnTutorialTooltipClickedListener() {
            @Override
            public void onTutorialTooltipClicked(TooltipId id,
                    TutorialTooltipView tutorialTooltipView) {
                TutorialTooltip.remove(activity, id, true);
            }
        };

        buttonCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TutorialTooltip.show(
                        new TutorialTooltipBuilder(activity)
                                .anchor(buttonCount, TutorialTooltipView.Gravity.TOP)
                                .onClick(onTutorialTooltipClickedListener)
                                .indicator(new IndicatorBuilder()
                                        .onClick(new OnIndicatorClickedListener() {
                                            @Override
                                            public void onIndicatorClicked(TooltipId id,
                                                    TutorialTooltipView tutorialTooltipView,
                                                    TutorialTooltipIndicator indicator,
                                                    View indicatorView) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Indicator " + id + " " + indicatorView.getWidth() + " clicked!",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .build())
                                .showCount("buttonCount", 3)
                                .build());
            }
        });

        buttonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TutorialTooltip.exists(activity, tutorialId1)) {
                    TutorialTooltip.remove(activity, tutorialId1, true);
                } else {
                    WaveIndicatorView waveIndicatorView = new WaveIndicatorView(activity);
                    waveIndicatorView.setStartColor(Color.argb(255, 255, 0, 0));
                    waveIndicatorView.setEndColor(Color.argb(0, 255, 0, 0));
                    waveIndicatorView.setStrokeWidth(pxFromDp(activity, 5));
//                    waveIndicatorView.setTargetDiameter(pxFromDp(activity, 50));

                    tutorialId1 = TutorialTooltip.show(
                            new TutorialTooltipBuilder(activity)
                                    .anchor(buttonTop, TutorialTooltipView.Gravity.TOP)
                                    .indicator(new IndicatorBuilder()
                                            .customView(waveIndicatorView)
                                            .offset(50, 50)
                                            .size(300, 300)
                                            .onClick(new OnIndicatorClickedListener() {
                                                @Override
                                                public void onIndicatorClicked(TooltipId id,
                                                        TutorialTooltipView tutorialTooltipView,
                                                        TutorialTooltipIndicator indicator,
                                                        View indicatorView) {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Indicator " + id + " " + indicatorView.getWidth() + " clicked!",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .build())
                                    .message(new MessageBuilder(TouchActivity.this)
                                            .text(getString(R.string.tutorial_message_1))
                                            .gravity(TutorialTooltipView.Gravity.TOP)
                                            .onClick(new OnMessageClickedListener() {
                                                @Override
                                                public void onMessageClicked(TooltipId id,
                                                        TutorialTooltipView tutorialTooltipView,
                                                        TutorialTooltipMessage message,
                                                        View messageView) {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Message " + id + " " + messageView.getWidth() + " clicked!",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .build())
                                    .onClick(onTutorialTooltipClickedListener)
                                    .build());
                }
            }
        });

        buttonCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TutorialTooltip.exists(activity, tutorialId4)) {
                    TutorialTooltip.remove(activity, tutorialId4, true);
                } else {
                    tutorialId4 = TutorialTooltip.show(
                            new TutorialTooltipBuilder(activity)
                                    .message(new MessageBuilder(TouchActivity.this)
                                            .text(getString(R.string.tutorial_message_3))
                                            .gravity(TutorialTooltipView.Gravity.RIGHT)
                                            .size((int) pxFromDp(getApplicationContext(), 150),
                                                    MessageBuilder.WRAP_CONTENT)
                                            .build())
                                    .anchor(buttonCenter)
                                    .onClick(onTutorialTooltipClickedListener)
                                    .build());
                }
            }
        });

        buttonBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TutorialTooltip.exists(activity, tutorialId2)) {
                    TutorialTooltip.remove(activity, tutorialId2, true);
                } else {
                    tutorialId2 = TutorialTooltip.show(
                            new TutorialTooltipBuilder(activity)
                                    .indicator(new IndicatorBuilder().color(Color.WHITE).build())
                                    .message(new MessageBuilder(TouchActivity.this)
                                            .text(getString(R.string.tutorial_message_2))
//                                            .anchor(new Point(300, 500))
                                            .anchor(buttonDialog)
                                            .gravity(TutorialTooltipView.Gravity.BOTTOM)
                                            .build())
                                    .anchor(buttonBottom, TutorialTooltipView.Gravity.BOTTOM)
                                    .onClick(onTutorialTooltipClickedListener)
                                    .build());
                }
            }
        });

        buttonFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tutorialTooltipView != null && tutorialTooltipView.isShown()) {
                    tutorialTooltipView.remove(true);
                } else {
                    WaveIndicatorView waveIndicatorView = new WaveIndicatorView(activity);
                    waveIndicatorView.setStartColor(Color.argb(255, 255, 255, 255));
                    waveIndicatorView.setEndColor(Color.argb(0, 255, 255, 255));
                    waveIndicatorView.setStrokeWidth(pxFromDp(activity, 5));
                    waveIndicatorView.setTargetDiameter(pxFromDp(activity, 50));

                    tutorialTooltipView = TutorialTooltip.make(
                            new TutorialTooltipBuilder(activity)
                                    .indicator(new IndicatorBuilder()
                                            .customView(waveIndicatorView)
                                            .onClick(new OnIndicatorClickedListener() {
                                                @Override
                                                public void onIndicatorClicked(TooltipId id,
                                                        TutorialTooltipView tutorialTooltipView,
                                                        TutorialTooltipIndicator indicator,
                                                        View indicatorView) {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Indicator " + id + " " + indicatorView.getWidth() + " clicked!",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .build())
                                    .message(new MessageBuilder(TouchActivity.this)
                                            .customView(new CardMessageView(activity))
                                            .text(getString(R.string.tutorial_message_fab))
                                            .gravity(TutorialTooltipView.Gravity.BOTTOM)
                                            .backgroundColor(Color.BLACK)
                                            .textColor(Color.WHITE)
                                            .build())
                                    .anchor(buttonFab)
                                    .attachToWindow()
                                    .onClick(new OnTutorialTooltipClickedListener() {
                                        @Override
                                        public void onTutorialTooltipClicked(TooltipId id,
                                                TutorialTooltipView tutorialTooltipView) {
                                            tutorialTooltipView.remove(true);
                                        }
                                    })
                                    .build());

                    tutorialId3 = TutorialTooltip.show(tutorialTooltipView);
                }
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TutorialTooltip.removeAll(activity, true);
                TutorialTooltip.resetAllShowCount(getApplicationContext());
            }
        });

        buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragmentTest dialogFragmentTest = DialogFragmentTest.newInstance();
                dialogFragmentTest.show(getSupportFragmentManager(), null);
            }
        });

        buttonChain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TutorialTooltipChainBuilder tutorialTooltipChainBuilder = new TutorialTooltipChainBuilder();

                View[] anchorViews = new View[]{
                        buttonTop,
                        buttonCenter,
                        buttonBottom,
                        buttonFab,
                        buttonDialog,
                        buttonChain,
                        buttonClear
                };

                for (int i = 0; i < anchorViews.length; i++) {
                    tutorialTooltipChainBuilder.addItem(new TutorialTooltipBuilder(activity)
                            .anchor(anchorViews[i])
                            .message(new MessageBuilder(TouchActivity.this).text((i + 1) + "/" + anchorViews.length + ": Message")
                                    .build())
                            .onClick(onTutorialTooltipClickedListener)
                            .build());
                }

                tutorialTooltipChainBuilder.execute();
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

    /**
     * Creates a TutorialTooltip where the user taps (if no other view consumes the touch
     *
     * @param x x coordinate of touch
     * @param y y coordinate of touch
     */
    private void createTutorialTooltip(float x, float y) {
        TutorialTooltip.show(
                new TutorialTooltipBuilder(this)
                        .anchor(new Point((int) x, (int) y))
                        .onClick(onTutorialTooltipClickedListener)
                        .message(new MessageBuilder(TouchActivity.this)
                                .text("You touched right here!")
                                .backgroundColor(Color.parseColor("#FFF3E0"))
                                .size((int) getResources().getDimension(R.dimen.messageWidth),
                                        MessageBuilder.WRAP_CONTENT)
//                                .sizeDimen(this, R.dimen.messageWidth, MessageBuilder.WRAP_CONTENT)
                                .build())
                        .build());
    }
}

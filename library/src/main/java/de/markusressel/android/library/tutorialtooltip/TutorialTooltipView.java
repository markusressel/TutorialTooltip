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

package de.markusressel.android.library.tutorialtooltip;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * TutorialTooltip View class
 * <p>
 * Created by Markus on 17.11.2016.
 */
public class TutorialTooltipView extends RelativeLayout {

    private static final String TAG = "TutorialTooltipView";

    private int tooltipId;
    private CharSequence text;
    private Gravity anchorGravity = Gravity.CENTER;
    private WeakReference<View> anchorView;
    private Point anchorPoint;

    private TextView mTextView;
    private CircleWaveAlertView circleWaveAlertView;
    private View customIndicatorView;

    private Gravity messageGravity = Gravity.BOTTOM;
    private FrameLayout indicatorLayout;
    private FrameLayout messageLayout;

    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    updatePositions();
                }
            };

    public enum Gravity {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        CENTER
    }

    public TutorialTooltipView(Context context) {
        super(context);
    }

    public TutorialTooltipView(Context context, TutorialTooltip.Builder builder) {
        this(context);

        getBuilderValues(builder);

        initializeViews();

        updateValues();

        getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    //    public TutorialTooltipView(Context context, AttributeSet attrs) {
    //        this(context, attrs, 0);
    //    }
    //
    //    public TutorialTooltipView(Context context, AttributeSet attrs, int defStyleAttr) {
    //        super(context, attrs, defStyleAttr);
    //    }
    //
    //    @TargetApi(21)
    //    public TutorialTooltipView(Context context, AttributeSet attrs, int defStyleAttr,
    //            int defStyleRes) {
    //        super(context, attrs, defStyleAttr, defStyleRes);
    //    }

    private void getBuilderValues(TutorialTooltip.Builder builder) {
        tooltipId = builder.id;
        text = builder.text;
        anchorGravity = builder.anchorGravity;
        if (builder.anchorView != null) {
            anchorView = new WeakReference<>(builder.anchorView);
        }
        anchorPoint = builder.anchorPoint;
        customIndicatorView = builder.indicatorView;
        messageGravity = builder.messageGravity;
    }

    private void initializeViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        messageLayout = (FrameLayout) inflater.inflate(R.layout.layout_tutorial_text, this, false);
        mTextView = (TextView) messageLayout.findViewById(R.id.textView);

        indicatorLayout = (FrameLayout) inflater.inflate(R.layout.layout_indicator,
                this,
                false);
        if (customIndicatorView != null) {
            indicatorLayout.removeAllViews();
            indicatorLayout.addView(customIndicatorView, WRAP_CONTENT, WRAP_CONTENT);
        } else {
            circleWaveAlertView = (CircleWaveAlertView) indicatorLayout.findViewById(R.id.indicator);
        }

        addView(indicatorLayout);
        addView(messageLayout);

        if (anchorView != null && anchorView.get() != null) {
            anchorView.get().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        } else if (anchorPoint != null) {

        } else {
            Log.e(TAG,
                    "Invalid anchorView and no anchorPoint either! You have to specify at least one!");
        }
    }

    private void updateValues() {
        setTutorialMessage(Html.fromHtml((String) text));

        //        float targetDiameter = 200;
        //        circleWaveAlertView.setTargetDiameter(targetDiameter);
    }

    private void updatePositions() {
        float x = 0;
        float y = 0;

        if (anchorView != null) {
            View view = anchorView.get();

            if (view != null) {
                int[] position = new int[2];
                view.getLocationInWindow(position);

                switch (anchorGravity) {
                    case TOP:
                        x = position[0] + view.getWidth() / 2 - indicatorLayout.getWidth() / 2;
                        y = position[1] - indicatorLayout.getHeight() / 2;

                        break;
                    case BOTTOM:
                        x = position[0] + view.getWidth() / 2 - indicatorLayout.getWidth() / 2;
                        y = position[1] + view.getHeight() - indicatorLayout.getHeight() / 2;


                        break;
                    case LEFT:
                        x = position[0] - indicatorLayout.getWidth() / 2;
                        y = position[1] + view.getHeight() / 2 - indicatorLayout.getHeight() / 2;


                        break;
                    case RIGHT:
                        x = position[0] + view.getWidth() - indicatorLayout.getWidth() / 2;
                        y = position[1] + view.getHeight() / 2 - indicatorLayout.getHeight() / 2;


                        break;
                    case CENTER:
                        x = position[0] + view.getWidth() / 2 - indicatorLayout.getWidth() / 2;
                        y = position[1] + view.getHeight() / 2 - indicatorLayout.getHeight() / 2;


                        break;
                }

                indicatorLayout.setX(x);
                indicatorLayout.setY(y);

                float messageX = 0;
                float messageY = 0;

                switch (messageGravity) {
                    case TOP:
                        messageX = position[0] + view.getWidth() / 2 - messageLayout.getWidth() / 2;
                        messageY = y - messageLayout.getHeight();
                        break;
                    case BOTTOM:
                        messageX = position[0] + view.getWidth() / 2 - messageLayout.getWidth() / 2;
                        messageY = y + indicatorLayout.getHeight() / 2;
                        break;
                    case LEFT:
                        messageX = position[0] - indicatorLayout.getWidth() - messageLayout.getWidth() / 2;
                        messageY = y + indicatorLayout.getHeight() / 2;
                        break;
                    case RIGHT:
                        messageX = position[0] + view.getWidth() - messageLayout.getWidth() / 2;
                        messageY = y + indicatorLayout.getHeight();
                        break;
                    case CENTER:
                        messageX = position[0] + view.getWidth() / 2 - messageLayout.getWidth() / 2;
                        messageY = y + indicatorLayout.getHeight();
                        break;
                }

                messageLayout.setX(messageX);
                messageLayout.setY(messageY);
            }
        } else if (anchorPoint != null) {
            x = anchorPoint.x - indicatorLayout.getWidth() / 2;
            y = anchorPoint.y - indicatorLayout.getHeight() / 2;

            //            indicatorLayout.setMinimumWidth(20);
            //            indicatorLayout.setMinimumHeight(20);
            indicatorLayout.setX(x);
            indicatorLayout.setY(y);

            float messageX = 0;
            float messageY = 0;

            switch (messageGravity) {
                case TOP:
                case BOTTOM:
                case LEFT:
                case RIGHT:
                case CENTER:
                default:
                    messageX = anchorPoint.x - messageLayout.getWidth() / 2;
                    messageY = anchorPoint.y + indicatorLayout.getHeight() / 2;
                    break;
            }

            messageLayout.setX(messageX);
            messageLayout.setY(messageY);
        } else {
            Log.e(TAG,
                    "Invalid anchorView and no anchorPoint either! You have to specify at least one!");
        }
    }

    private void setTutorialMessage(CharSequence charSequence) {
        mTextView.setText(charSequence);
    }

    /**
     * Get the TutorialTooltip identifier for this TutorialTooltipView
     *
     * @return id
     */
    public int getTutorialTooltipId() {
        return tooltipId;
    }

    /**
     * Show this view
     */
    public void show() {
        if (getParent() == null) {
            final Activity activity = ViewHelper.getActivity(getContext());
            LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
            if (activity != null) {
                ViewGroup rootView;
                rootView = (ViewGroup) (activity.getWindow().getDecorView());
                rootView.addView(this, params);
            }
        }
    }

    /**
     * Remove this view
     */
    public void remove() {
        ViewParent parent = getParent();

        if (null != parent) {
            ((ViewGroup) parent).removeView(TutorialTooltipView.this);
        }
    }
}

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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * TutorialTooltip View class
 * <p>
 * Created by Markus on 17.11.2016.
 */
public class TutorialTooltipView extends RelativeLayout {

    private int tooltipId;
    private CharSequence text;
    private Gravity gravity = Gravity.CENTER;
    private WeakReference<View> anchorView;
    private Point anchorPoint;

    private TextView mTextView;
    private CircleWaveAlertView circleWaveAlertView;
    private View indicatorView;
    private View messageView;

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

        updatePositions();
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
        gravity = builder.gravity;
        anchorView = new WeakReference<>(builder.anchorView);
        anchorPoint = builder.anchorPoint;
    }

    private void initializeViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        messageView = inflater.inflate(R.layout.layout_tutorial_text, this, false);
        mTextView = (TextView) messageView.findViewById(R.id.textView);

        indicatorView = inflater.inflate(R.layout.layout_indicator, this, false);
        circleWaveAlertView = (CircleWaveAlertView) indicatorView.findViewById(R.id.indicator);

        // center views in layout
        //        setGravity(android.anchorView.Gravity.CENTER);
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));

        LayoutParams paramsIndicator = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        LayoutParams paramsText = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        paramsText.addRule(RelativeLayout.BELOW, R.id.indicator);

        addView(indicatorView, paramsIndicator);
        addView(messageView, paramsText);

        if (anchorView != null) {
            anchorView.get().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
            //            float size = Math.max(anchorView.getWidth(), anchorView.getHeight());
            //            circleWaveAlertView.setTargetDiameter(size);
        } else {
            //            circleWaveAlertView.setTargetDiameter(ViewHelper.pxFromDp(getContext(), 50));
        }
    }

    private void updateValues() {
        setTutorialMessage(Html.fromHtml((String) text));

        float targetDiameter = 200;
        circleWaveAlertView.setTargetDiameter(targetDiameter);
    }

    private void updatePositions() {
        // TODO: Update view positions when layout changed
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        float targetDiameter = circleWaveAlertView.getTargetDiameter();

        if (anchorView != null) {
            View view = anchorView.get();

            if (view != null) {
                //                circleWaveAlertView.setTargetDiameter(Math.max(view.getWidth(), view.getHeight()));

                float x = 0;
                float y = 0;

                switch (gravity) {
                    case TOP:
                        break;
                    case BOTTOM:
                        break;
                    case LEFT:
                        break;
                    case RIGHT:
                        break;
                    case CENTER:
                        x = view.getX() + view.getWidth() / 2 - indicatorView.getWidth() / 2;
                        y = view.getY() + view.getHeight() - indicatorView.getHeight() / 2;
                        break;
                }

                indicatorView.setX(x);
                indicatorView.setY(y);


                float messageX = view.getX() + view.getWidth() / 2 - messageView.getWidth() / 2;
                float messageY = y + indicatorView.getHeight();

                messageView.setX(messageX);
                messageView.setY(messageY);
            }
        }

        //        setX(display.getWidth() / 2 - (targetDiameter / 2));
        //        setY(display.getHeight() / 2 - (targetDiameter / 2));

        //        float messagePositionX = indicatorView.getX() - indicatorView.getWidth() / 2;
        //        float messagePositionY = indicatorView.getY() + indicatorView.getHeight() / 2;
        //        messageView.setX(messagePositionX);
        //        messageView.setY(messagePositionY);

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
        // TODO:
        ViewParent parent = getParent();

        if (null != parent) {
            ((ViewGroup) parent).removeView(TutorialTooltipView.this);
            //            if (null != mShowAnimation && mShowAnimation.isStarted()) {
            //                mShowAnimation.cancel();
            //            }
        }
    }
}

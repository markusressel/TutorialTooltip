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
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private TextView mTextView;
    private CircleWaveAlertView circleWaveAlertView;

    enum Gravity {
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
        text = getContext().getString(builder.textRes);
        gravity = builder.gravity;
    }

    private void initializeViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        mTextView = (TextView) inflater.inflate(R.layout.layout_tutorial_text, this, false);

        circleWaveAlertView = (CircleWaveAlertView) inflater.inflate(R.layout.layout_indicator,
                this,
                false);

        // center views in layout
        //        setGravity(android.view.Gravity.CENTER);
        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams paramsIndicator = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsText.addRule(RelativeLayout.BELOW, R.id.indicator);

        addView(circleWaveAlertView, paramsIndicator);
        addView(mTextView, paramsText);
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

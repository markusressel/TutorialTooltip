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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * TutorialTooltip View class
 * <p>
 * Created by Markus on 17.11.2016.
 */
public class TutorialTooltipView extends LinearLayout {

    private int tooltipId;
    private CharSequence text;

    public TutorialTooltipView(Context context) {
        super(context);
    }

    public TutorialTooltipView(Context context, TutorialTooltip.Builder builder) {
        this(context);

        tooltipId = builder.id;
        text = context.getString(builder.textRes);

        LayoutParams params = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        View messageView = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_tutorial_text, this, false);
        messageView.setLayoutParams(params);

        TextView mTextView = (TextView) messageView.findViewById(R.id.textView);
        mTextView.setText(Html.fromHtml((String) this.text));


        View indicatorView = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_indicator, this, false);
        indicatorView.setLayoutParams(params);

        CircleWaveAlertView circleWaveAlertView = (CircleWaveAlertView) indicatorView.findViewById(R.id.indicator);
        float targetDiameter = 200;
        circleWaveAlertView.setTargetDiameter(targetDiameter);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        indicatorView.setX(display.getWidth() / 2 - (targetDiameter / 2));
        indicatorView.setY(display.getHeight() / 2 - (targetDiameter / 2));

        addView(messageView);
        addView(indicatorView);

        setOrientation(VERTICAL);
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

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

package de.markusressel.android.library.tutorialtooltip.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage;

/**
 * Basic Message view implementation
 * <p>
 * Created by Markus on 24.11.2016.
 */
public class CardMessageView extends FrameLayout implements TutorialTooltipMessage {

    private int backgroundColor = Color.parseColor("#FFFFFFFF");
    private int borderColor = Color.parseColor("#FFFFFFFF");
    private int borderThickness = 3;
    private float cornerRadius;
    private int defaultPadding;

    private LinearLayout linearLayout;
    private TextView textView;
    private GradientDrawable cardShape;

    public CardMessageView(Context context) {
        this(context, null);
    }

    public CardMessageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        linearLayout = new LinearLayout(context, attrs, defStyleAttr);
        textView = new TextView(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    @SuppressWarnings("unused")
    public CardMessageView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        linearLayout = new LinearLayout(context, attrs, defStyleAttr, defStyleRes);
        textView = new TextView(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        cornerRadius = (int) ViewHelper.pxFromDp(getContext(), 12);
        defaultPadding = (int) ViewHelper.pxFromDp(getContext(), 8);

        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(Color.argb(0, 0, 0, 0));

        linearLayout.addView(textView);
        addView(linearLayout);


        cardShape = new GradientDrawable();
        cardShape.mutate();
        cardShape.setShape(GradientDrawable.RECTANGLE);
        cardShape.setCornerRadii(new float[]{cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius});
        cardShape.setColor(backgroundColor);
        cardShape.setStroke(borderThickness, borderColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            linearLayout.setBackground(cardShape);
        } else {
            linearLayout.setBackgroundDrawable(cardShape);
        }

        linearLayout.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float elevation = ViewHelper.pxFromDp(getContext(), 6);
            int padding = (int) ViewHelper.pxFromDp(getContext(), 6);

            linearLayout.setElevation(elevation);
            linearLayout.setClipToPadding(false);

            setPadding(padding, padding, padding, padding);
            setClipToPadding(false);
        }
    }

    @Override
    public void setText(CharSequence text) {
        textView.setText(text);
    }

    @Override
    public void setTextColor(@ColorInt int color) {
        textView.setTextColor(color);
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.borderColor = backgroundColor;
        cardShape.setColor(this.backgroundColor);
        cardShape.setStroke(borderThickness, borderColor);

        invalidate();
    }

    /**
     * Set the card border color
     *
     * @param color color as int
     */
    public void setBorderColor(@ColorInt int color) {
        this.borderColor = color;
        cardShape.setStroke(borderThickness, borderColor);

        invalidate();
    }

    /**
     * Set the card border thickness
     *
     * @param thickness width in pixel
     */
    @SuppressWarnings("unused")
    public void setBorderThickness(int thickness) {
        this.borderThickness = thickness;
        cardShape.setStroke(borderThickness, borderColor);

        invalidate();
    }

    /**
     * Set the card corner radius
     *
     * @param radius radius in pixel
     */
    @SuppressWarnings("unused")
    public void setCornerRadius(float radius) {
        cornerRadius = radius;
        cardShape.setCornerRadii(new float[]{
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius,
                cornerRadius}
        );

        invalidate();
    }
}

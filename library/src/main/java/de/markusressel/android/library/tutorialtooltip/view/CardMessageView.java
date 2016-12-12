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
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage;

/**
 * Basic Message view implementation
 * <p>
 * Created by Markus on 24.11.2016.
 */
public class CardMessageView extends LinearLayout implements TutorialTooltipMessage {

    private final int backgroundColor = Color.parseColor("#FFFFFFFF");
    private final int borderColor = Color.parseColor("#FFFFFFFF");

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

        textView = new TextView(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    @SuppressWarnings("unused")
    public CardMessageView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        textView = new TextView(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        addView(textView);

        float cornerRadius = 16;

        cardShape = new GradientDrawable();
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
        cardShape.setStroke(3, borderColor);
        setBackgroundDrawable(cardShape);

        int cornerRadiusInt = (int) cornerRadius;
        setPadding(cornerRadiusInt, cornerRadiusInt, cornerRadiusInt, cornerRadiusInt);
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
        cardShape.setColor(backgroundColor);

        invalidate();
    }
}

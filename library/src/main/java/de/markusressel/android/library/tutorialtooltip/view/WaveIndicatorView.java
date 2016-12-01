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
import android.support.annotation.ColorInt;
import android.util.AttributeSet;

import de.markusressel.android.library.circlewaveview.CircleWaveAlertView;
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipIndicator;

/**
 * Created by Markus on 24.11.2016.
 */
public class WaveIndicatorView extends CircleWaveAlertView implements TutorialTooltipIndicator {

    public WaveIndicatorView(Context context) {
        super(context);
    }

    public WaveIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaveIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public WaveIndicatorView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setColor(@ColorInt int color) {
        setStartColor(color);
        setEndColor(Color.argb(0, Color.red(color), Color.green(color), Color.blue(color)));
    }

}

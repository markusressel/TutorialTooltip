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

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Markus on 17.11.2016.
 */
class TutorialTooltipView extends LinearLayout {


    private int tooltipId;
    private CharSequence text;

    public TutorialTooltipView(Context context) {
        super(context);
        //        this(context, null);
    }

    public TutorialTooltipView(Context context, TutorialTooltip.Builder builder) {
        this(context);

        tooltipId = builder.id;
        text = context.getString(builder.textRes);
    }

    public TutorialTooltipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TutorialTooltipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public TutorialTooltipView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
     * Remove this view
     */
    public void remove() {
        // TODO:
    }
}

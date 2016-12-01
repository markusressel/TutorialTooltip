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

package de.markusressel.android.library.tutorialtooltip.builder;

import android.content.Context;
import android.view.View;

import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipIndicator;

/**
 * Use this Builder to create and customize any of the included indicators
 * <p>
 * Created by Markus on 01.12.2016.
 */
public class IndicatorBuilder extends Builder<IndicatorBuilder> {

    public enum Type {
        Default,
        Custom
    }

    private Context context;

    private Type type = Type.Default;

    /**
     * Custom indicator view
     */
    private View customView;

    /**
     * Indicator x axis offset from anchor position
     */
    private int offsetX;

    /**
     * Indicator y axis offset from anchor position
     */
    private int offsetY;

    /**
     * Constructor for the builder.
     * Chain methods and call ".build()" as your last step to make this object immutable.
     *
     * @param context activity context
     */
    public IndicatorBuilder(Context context) {
        this.context = context;
    }

    /**
     * Set a custom view that should be used as the indicator
     * <p>
     * To build your own indicator view, you have to create a new class extending <code>View</code> and implement <code>TutorialTooltipIndicator</code>
     *
     * @param indicatorView custom indicator view
     * @return IndicatorBuilder
     */
    public <T extends View & TutorialTooltipIndicator> IndicatorBuilder customView(
            T indicatorView) {
        throwIfCompleted();
        this.type = Type.Custom;
        this.customView = indicatorView;
        return this;
    }

    /**
     * Set an offset for the indicator view
     *
     * @param offsetX x-axis offset in pixel (positive is right, negative is left)
     * @param offsetY y-axis offset in pixel (positive is down, negative is up)
     * @return IndicatorBuilder
     */
    public IndicatorBuilder offset(int offsetX, int offsetY) {
        throwIfCompleted();
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        return this;
    }


    public Context getContext() {
        return context;
    }

    public Type getType() {
        return type;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public View getCustomView() {
        return customView;
    }
}

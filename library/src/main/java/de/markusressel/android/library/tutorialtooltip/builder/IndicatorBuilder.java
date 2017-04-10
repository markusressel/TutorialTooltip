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

import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import de.markusressel.android.library.tutorialtooltip.interfaces.OnIndicatorClickedListener;
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipIndicator;

/**
 * Use this Builder to create and customize any of the included indicators
 * <p>
 * Created by Markus on 01.12.2016.
 */
public final class IndicatorBuilder extends Builder<IndicatorBuilder> {

    /**
     * Constant size value to wrap the views content
     */
    @SuppressWarnings("WeakerAccess,unused")
    public static final int WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT;

    /**
     * Constant size value to match the parents size
     */
    @SuppressWarnings("WeakerAccess,unused")
    public static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

    /**
     * Constant size value to match the anchors view size
     */
    @SuppressWarnings("WeakerAccess,unused")
    public static final int MATCH_ANCHOR = -5;
    /**
     * Specifies if a custom view is used
     */
    private Type type = Type.Default;
    /**
     * Custom indicator view
     */
    private View customView;
    /**
     * Indicator x axis offset from anchor position
     */
    private int offsetX = 0;
    /**
     * Indicator y axis offset from anchor position
     */
    private int offsetY = 0;
    /**
     * Indicator x axis size (width)
     */
    private Integer width;
    /**
     * Indicator y axis size (height)
     */
    private Integer height;
    /**
     * Main indicator color
     */
    @ColorInt
    private Integer color = null;
    /**
     * OnClick listener
     */
    private OnIndicatorClickedListener onIndicatorClickedListener;

    /**
     * Constructor for the builder.
     * Chain methods and call ".build()" as your last step to make this object immutable.
     */
    public IndicatorBuilder() {
    }

    /**
     * Set a custom view that should be used as the indicator
     * <p>
     * To build your own indicator view, you have to create a new class extending <code>View</code>
     * and implement <code>TutorialTooltipIndicator</code>
     *
     * @param indicatorView custom indicator view
     * @return IndicatorBuilder
     */
    @SuppressWarnings("unused")
    public <T extends View & TutorialTooltipIndicator> IndicatorBuilder customView(
            T indicatorView) {
        throwIfCompleted();
        this.type = Type.Custom;
        this.customView = indicatorView;
        return this;
    }

    /**
     * Set a custom size for the indicator
     * This does NOT change the actual view size. If the size specified here is smaller
     * than the view, the view will be cropped. Otherwise the view will have a padding around it.
     * <p>
     * If no value is specified, the view itself will be used for size measuring.
     *
     * @param width  x-axis size in pixel
     * @param height y-axis size in pixel
     * @return IndicatorBuilder
     */
    @SuppressWarnings("unused")
    public IndicatorBuilder size(@Dimension(unit = Dimension.PX) int width,
            @Dimension(unit = Dimension.PX) int height) {
        throwIfCompleted();
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Set an offset for the indicator view
     *
     * @param offsetX x-axis offset in pixel (positive is right, negative is left)
     * @param offsetY y-axis offset in pixel (positive is down, negative is up)
     * @return IndicatorBuilder
     */
    @SuppressWarnings("unused")
    public IndicatorBuilder offset(@Dimension(unit = Dimension.PX) int offsetX,
            @Dimension(unit = Dimension.PX) int offsetY) {
        throwIfCompleted();
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        return this;
    }

    /**
     * Set the main color of this indicator
     *
     * @param color color as int
     * @return
     */
    @SuppressWarnings("unused")
    public IndicatorBuilder color(@ColorInt int color) {
        throwIfCompleted();
        this.color = color;
        return this;
    }

    /**
     * Set an onClick listener for the indicator
     *
     * @param onIndicatorClickedListener onClick listener
     * @return IndicatorBuilder
     */
    @SuppressWarnings("unused")
    public IndicatorBuilder onClick(OnIndicatorClickedListener onIndicatorClickedListener) {
        throwIfCompleted();
        this.onIndicatorClickedListener = onIndicatorClickedListener;
        return this;
    }

    public Type getType() {
        return type;
    }

    @Nullable
    public Integer getWidth() {
        return width;
    }

    @Nullable
    public Integer getHeight() {
        return height;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    @ColorInt
    public Integer getColor() {
        return color;
    }

    public View getCustomView() {
        return customView;
    }

    public OnIndicatorClickedListener getOnIndicatorClickedListener() {
        return onIndicatorClickedListener;
    }

    @SuppressWarnings("WeakerAccess")
    public enum Type {
        Default,
        Custom
    }
}

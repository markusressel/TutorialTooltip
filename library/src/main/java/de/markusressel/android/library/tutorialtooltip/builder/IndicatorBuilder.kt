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

package de.markusressel.android.library.tutorialtooltip.builder

import android.support.annotation.ColorInt
import android.support.annotation.Dimension
import android.view.View
import android.widget.LinearLayout
import de.markusressel.android.library.tutorialtooltip.interfaces.OnIndicatorClickedListener
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipIndicator

/**
 * Use this Builder to create and customize any of the included indicators
 *
 *
 * Created by Markus on 01.12.2016.
 */
/**
 * Constructor for the builder.
 * Chain methods and call ".build()" as your last step to make this object immutable.
 */
class IndicatorBuilder : Builder<IndicatorBuilder>() {
    /**
     * Specifies if a custom view is used
     */
    var type = Type.Default
        private set
    /**
     * Custom indicator view
     */
    var customView: View? = null
        private set
    /**
     * Indicator x axis offset from anchor position
     */
    var offsetX = 0
        private set
    /**
     * Indicator y axis offset from anchor position
     */
    var offsetY = 0
        private set
    /**
     * Indicator x axis size (width)
     */
    var width: Int? = null
        private set
    /**
     * Indicator y axis size (height)
     */
    var height: Int? = null
        private set
    /**
     * Main indicator color
     */
    var color: Int? = null
        private set
    /**
     * OnClick listener
     */
    var onIndicatorClickedListener: OnIndicatorClickedListener? = null
        private set

    /**
     * Set a custom view that should be used as the indicator
     *
     *
     * To build your own indicator view, you have to create a new class extending `View`
     * and implement `TutorialTooltipIndicator`

     * @param indicatorView custom indicator view
     * *
     * @return IndicatorBuilder
     */
    fun <T> customView(
            indicatorView: T): IndicatorBuilder where T : View, T : TutorialTooltipIndicator {
        throwIfCompleted()
        this.type = Type.Custom
        this.customView = indicatorView
        return this
    }

    /**
     * Set a custom size for the indicator
     * This does NOT change the actual view size. If the size specified here is smaller
     * than the view, the view will be cropped. Otherwise the view will have a padding around it.
     *
     *
     * If no value is specified, the view itself will be used for size measuring.

     * @param width  x-axis size in pixel
     * *
     * @param height y-axis size in pixel
     * *
     * @return IndicatorBuilder
     */
    fun size(@Dimension(unit = Dimension.PX) width: Int,
             @Dimension(unit = Dimension.PX) height: Int): IndicatorBuilder {
        throwIfCompleted()
        this.width = width
        this.height = height
        return this
    }

    /**
     * Set an offset for the indicator view

     * @param offsetX x-axis offset in pixel (positive is right, negative is left)
     * *
     * @param offsetY y-axis offset in pixel (positive is down, negative is up)
     * *
     * @return IndicatorBuilder
     */
    fun offset(@Dimension(unit = Dimension.PX) offsetX: Int,
               @Dimension(unit = Dimension.PX) offsetY: Int): IndicatorBuilder {
        throwIfCompleted()
        this.offsetX = offsetX
        this.offsetY = offsetY
        return this
    }

    /**
     * Set the main color of this indicator

     * @param color color as int
     * *
     * @return
     */
    fun color(@ColorInt color: Int): IndicatorBuilder {
        throwIfCompleted()
        this.color = color
        return this
    }

    /**
     * Set an onClick listener for the indicator

     * @param onIndicatorClickedListener onClick listener
     * *
     * @return IndicatorBuilder
     */
    fun onClick(onIndicatorClickedListener: OnIndicatorClickedListener): IndicatorBuilder {
        throwIfCompleted()
        this.onIndicatorClickedListener = onIndicatorClickedListener
        return this
    }

    enum class Type {
        Default,
        Custom
    }

    companion object {

        /**
         * Constant size value to wrap the views content
         */
        val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT

        /**
         * Constant size value to match the parents size
         */
        val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

        /**
         * Constant size value to match the anchors view size
         */
        val MATCH_ANCHOR = -5
    }
}

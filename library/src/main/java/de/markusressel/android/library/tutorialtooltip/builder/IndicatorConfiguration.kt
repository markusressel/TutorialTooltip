/*
 * Copyright (c) 2017 Markus Ressel
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
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipIndicator
import de.markusressel.android.library.tutorialtooltip.view.TooltipId
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView

/**
 * Configuration object for an indicator
 *
 * Created by Markus on 18.12.2017.
 */
class IndicatorConfiguration(
        /**
         * Custom indicator view
         */
        internal val customView: View? = null,

        /**
         * Indicator x axis offset from anchor position
         * x-axis offset in pixel (positive is right, negative is left)
         */
        @Dimension(unit = Dimension.PX)
        internal val offsetX: Int = 0,

        /**
         * Indicator y axis offset from anchor position
         * y-axis offset in pixel (positive is down, negative is up)
         */
        @Dimension(unit = Dimension.PX)
        internal val offsetY: Int = 0,

        /**
         * Indicator x axis size (width)
         *
         * This does NOT change the actual view size. If the size specified here is smaller
         * than the view, the view will be cropped. Otherwise the view will have a padding around it.
         *
         * If no value is specified, the view itself will be used for size measuring.
         */
        @Dimension(unit = Dimension.PX)
        internal val width: Int? = null,

        /**
         * Indicator y axis size (height)
         *
         * This does NOT change the actual view size. If the size specified here is smaller
         * than the view, the view will be cropped. Otherwise the view will have a padding around it.
         *
         * If no value is specified, the view itself will be used for size measuring.
         */
        @Dimension(unit = Dimension.PX)
        internal val height: Int? = null,

        /**
         * Main indicator color
         */
        @ColorInt
        internal val color: Int? = null,

        /**
         * OnClick listener
         */
        internal val onClick: ((id: TooltipId, tutorialTooltipView: TutorialTooltipView,
                                indicator: TutorialTooltipIndicator, indicatorView: View) -> Unit)? = null

) {

    init {
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
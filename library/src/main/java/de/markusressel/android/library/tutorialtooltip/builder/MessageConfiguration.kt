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

import android.graphics.Point
import android.support.annotation.ColorInt
import android.support.annotation.Dimension
import android.view.View
import android.widget.LinearLayout
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage
import de.markusressel.android.library.tutorialtooltip.view.TooltipId
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView

/**
 * Created by Markus on 18.12.2017.
 */
class MessageConfiguration(
        /**
         * Message text
         */
        val text: String = "Your Tutorial Message is shown right here.",

        /**
         * Anchor point if no view is used
         *
         * If no anchor view or point is specified
         * the message will be positioned relative to the indicator view.
         */
        val anchorPoint: Point? = null,

        /**
         * Anchor view
         *
         * If no anchor view or point is specified
         * the message will be positioned relative to the indicator view.
         */
        val anchorView: View? = null,

        /**
         * Custom message view
         */
        val customView: View? = null,

        /**
         * Message gravity
         */
        val gravity: TutorialTooltipView.Gravity = TutorialTooltipView.Gravity.TOP,

        /**
         * Message x axis offset from anchor position
         */
        @Dimension(unit = Dimension.PX)
        val offsetX: Int = 0,

        /**
         * Message y axis offset from anchor position
         */
        @Dimension(unit = Dimension.PX)
        val offsetY: Int = 0,

        /**
         * Message x axis size (width)
         */
        @Dimension(unit = Dimension.PX)
        val width: Int = WRAP_CONTENT,

        /**
         * Message y axis size (height)
         */
        @Dimension(unit = Dimension.PX)
        val height: Int = WRAP_CONTENT,

        /**
         * Text color
         */
        @ColorInt
        val textColor: Int? = null,

        /**
         * Background color
         */
        @ColorInt
        val backgroundColor: Int? = null,

        /**
         * OnClick listener
         */
        val onMessageClicked: ((id: TooltipId, tutorialTooltipView: TutorialTooltipView,
                                message: TutorialTooltipMessage, messageView: View) -> Unit)? = null

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
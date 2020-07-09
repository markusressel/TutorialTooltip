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
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
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
        internal val text: String = "Your Tutorial Message is shown right here.",

        /**
         * Anchor point if no view is used
         *
         * If no anchor view or point is specified
         * the message will be positioned relative to the indicator view.
         */
        internal val anchorPoint: Point? = null,

        /**
         * Anchor view
         *
         * If no anchor view or point is specified
         * the message will be positioned relative to the indicator view.
         */
        internal val anchorView: View? = null,

        /**
         * Custom message view
         */
        internal val customView: View? = null,

        /**
         * Message gravity
         */
        internal val gravity: TutorialTooltipView.Gravity = TutorialTooltipView.Gravity.TOP,

        /**
         * Message x axis offset from anchor position
         */
        @Dimension(unit = Dimension.PX)
        internal val offsetX: Int = 0,

        /**
         * Message y axis offset from anchor position
         */
        @Dimension(unit = Dimension.PX)
        internal val offsetY: Int = 0,

        /**
         * Message x axis size (width)
         */
        @Dimension(unit = Dimension.PX)
        internal val width: Int = WRAP_CONTENT,

        /**
         * Message y axis size (height)
         */
        @Dimension(unit = Dimension.PX)
        internal val height: Int = WRAP_CONTENT,

        /**
         * Text color
         */
        @ColorInt
        internal val textColor: Int? = null,

        /**
         * Background color
         */
        @ColorInt
        internal val backgroundColor: Int? = null,

        /**
         * OnClick listener
         */
        internal val onClick: ((id: TooltipId, tutorialTooltipView: TutorialTooltipView,
                                message: TutorialTooltipMessage, messageView: View) -> Unit)? = null

) {

    companion object {

        /**
         * Constant size value to wrap the views content
         */
        const val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT

        /**
         * Constant size value to match the parents size
         */
        const val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

        /**
         * Constant size value to match the anchors view size
         */
        const val MATCH_ANCHOR = -5
    }
}
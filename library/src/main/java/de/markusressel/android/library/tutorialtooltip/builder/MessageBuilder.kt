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

import android.content.Context
import android.graphics.Point
import android.support.annotation.ColorInt
import android.support.annotation.Dimension
import android.support.annotation.StringRes
import android.view.View
import android.widget.LinearLayout

import de.markusressel.android.library.tutorialtooltip.interfaces.OnMessageClickedListener
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView

/**
 * Builder to create the message view
 *
 *
 * Created by Markus on 01.12.2016.
 */
class MessageBuilder
/**
 * Constructor

 * @param context application context
 */
(private val context: Context) : Builder<MessageBuilder>() {

    enum class Type {
        Default,
        Custom
    }

    /**
     * Specifies if a custom view is used
     */
    var type = Type.Default
        private set

    /**
     * Message text
     */
    var text = "Your Tutorial Message is shown right here."
        private set

    /**
     * Anchor point if no view is used
     */
    var anchorPoint: Point? = null
        private set

    /**
     * Anchor view
     */
    var anchorView: View? = null
        private set

    /**
     * Custom message view
     */
    var customView: View? = null
        private set

    /**
     * Message gravity
     */
    var gravity: TutorialTooltipView.Gravity = TutorialTooltipView.Gravity.TOP
        private set

    /**
     * Message x axis offset from anchor position
     */
    var offsetX = 0
        private set

    /**
     * Message y axis offset from anchor position
     */
    var offsetY = 0
        private set

    /**
     * Message x axis size (width)
     */
    var width = WRAP_CONTENT
        private set

    /**
     * Message y axis size (height)
     */
    var height = WRAP_CONTENT
        private set

    /**
     * Text color
     */
    var textColor: Int? = null
        private set

    /**
     * Background color
     */
    var backgroundColor: Int? = null
        private set

    /**
     * OnClick listener
     */
    var onMessageClickedListener: OnMessageClickedListener? = null
        private set

    /**
     * Set a custom anchor point for the message view

     * @param anchorPoint anchor point
     * *
     * @return MessageBuilder
     */
    fun anchor(anchorPoint: Point): MessageBuilder {
        throwIfCompleted()
        this.anchorPoint = anchorPoint
        return this
    }

    /**
     * Set a custom anchor view for the message view
     *
     *
     * If no anchor view or point is specified
     * the message will be positioned relative to the indicator view.

     * @param anchorView anchor view
     * *
     * @return MessageBuilder
     */
    fun anchor(anchorView: View): MessageBuilder {
        throwIfCompleted()
        this.anchorView = anchorView
        return this
    }

    /**
     * Set the gravity of the message view
     *
     *
     * The message view will be positioned relative to the indicator view

     * @param gravity message view gravity
     * *
     * @return MessageBuilder
     */
    fun gravity(gravity: TutorialTooltipView.Gravity): MessageBuilder {
        throwIfCompleted()
        this.gravity = gravity
        return this
    }

    /**
     * Set the tutorial text

     * @param text message
     * *
     * @return MessageBuilder
     */
    fun text(text: String): MessageBuilder {
        throwIfCompleted()
        this.text = text
        return this
    }

    /**
     * Set the tutorial text

     * @param textRes message resource
     * *
     * @return MessageBuilder
     */
    fun text(@StringRes textRes: Int): MessageBuilder {
        throwIfCompleted()
        this.text = context.getString(textRes)
        return this
    }

    /**
     * Set a custom size for the message
     *
     *

     * @param width  x-axis size in pixel
     * *
     * @param height y-axis size in pixel
     * *
     * @return MessageBuilder
     */
    fun size(@Dimension(unit = Dimension.PX) width: Int,
             @Dimension(unit = Dimension.PX) height: Int): MessageBuilder {
        throwIfCompleted()
        this.width = width
        this.height = height
        return this
    }

    /**
     * Set an offset for the message view

     * @param offsetX x-axis offset in pixel (positive is right, negative is left)
     * *
     * @param offsetY y-axis offset in pixel (positive is down, negative is up)
     * *
     * @return MessageBuilder
     */
    fun offset(@Dimension(unit = Dimension.PX) offsetX: Int,
               @Dimension(unit = Dimension.PX) offsetY: Int): MessageBuilder {
        throwIfCompleted()
        this.offsetX = offsetX
        this.offsetY = offsetY
        return this
    }

    /**
     * Set the text color for the message

     * @param textColor text color
     * *
     * @return MessageBuilder
     */
    fun textColor(@ColorInt textColor: Int): MessageBuilder {
        throwIfCompleted()
        this.textColor = textColor
        return this
    }

    /**
     * Set the background color for the message

     * @param backgroundColor background color
     * *
     * @return MessageBuilder
     */
    fun backgroundColor(@ColorInt backgroundColor: Int): MessageBuilder {
        throwIfCompleted()
        this.backgroundColor = backgroundColor
        return this
    }

    /**
     * Set a custom view that should be used as the indicator
     *
     *
     * To build your own indicator view, you have to create a new class extending `View`
     * and implement `TutorialTooltipIndicator`

     * @param messageView custom indicator view
     * *
     * @return MessageBuilder
     */
    fun <T> customView(messageView: T): MessageBuilder where T : View, T : TutorialTooltipMessage {
        throwIfCompleted()
        this.type = Type.Custom
        this.customView = messageView
        return this
    }

    /**
     * Set an onClick listener for the message

     * @param onMessageClickedListener onClick listener
     * *
     * @return MessageBuilder
     */
    fun onClick(onMessageClickedListener: OnMessageClickedListener): MessageBuilder {
        throwIfCompleted()
        this.onMessageClickedListener = onMessageClickedListener
        return this
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

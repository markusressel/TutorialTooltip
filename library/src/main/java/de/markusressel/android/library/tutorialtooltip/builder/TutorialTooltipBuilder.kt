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

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.support.annotation.StringRes
import android.util.Log
import android.view.View

import de.markusressel.android.library.tutorialtooltip.interfaces.OnTutorialTooltipClickedListener
import de.markusressel.android.library.tutorialtooltip.interfaces.OnTutorialTooltipRemovedListener
import de.markusressel.android.library.tutorialtooltip.view.TooltipId
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView

/**
 * Use this Builder to create a TutorialTooltip
 *
 *
 * Created by Markus on 01.12.2016.
 */
class TutorialTooltipBuilder
/**
 * Constructor for the builder.
 * Chain methods and call ".build()" as your last step to make this object immutable.

 * @param context activity context that the TutorialTooltip will be added to.
 * *                Application context will not suffice!
 */
(val context: Context) : Builder<TutorialTooltipBuilder>() {

    /**
     * AttachMode used to distinguish between activity, dialog and window scope
     */
    var attachMode: AttachMode
        private set

    /**
     * Dialog the TutorialTooltip will be attached to (if AttachMode is Dialog)
     */
    var dialog: Dialog? = null
        private set
    /**
     * ID the TutorialTooltip will get
     */
    var tooltipId: TooltipId
        private set
    /**
     * The amount of times to show this TutorialTooltip
     */
    var showCount: Int? = null
        private set
    /**
     * Anchor view
     * This view is used to position the indicator view
     */
    var anchorView: View? = null
        private set
    /**
     * Anchor gravity used to position the indicator using the anchorView borders (or center)
     */
    var anchorGravity: TutorialTooltipView.Gravity? = null
        private set
    /**
     * Exact coordinates the indicator should be positioned
     */
    var anchorPoint: Point? = null
        private set
    /**
     * IndicatorBuilder
     */
    var indicatorBuilder: IndicatorBuilder
        private set
    /**
     * MessageBuilder
     */
    var messageBuilder: MessageBuilder
        private set
    /**
     * OnClick listener for the whole TutorialTooltipView
     */
    var onTutorialTooltipClickedListener: OnTutorialTooltipClickedListener? = null
        private set
    /**
     * Listener for TutorialTooltip remove() events
     */
    var onTutorialTooltipRemovedListener: OnTutorialTooltipRemovedListener? = null
        private set

    init {

        // set default values
        this.attachMode = AttachMode.Activity
        this.indicatorBuilder = IndicatorBuilder().build()
        this.messageBuilder = MessageBuilder(context).build()

        this.tooltipId = TooltipId()
    }

    /**
     * Specify whether the TutorialTooltip should be attached to the Window or the activity.
     *
     *
     * This can be handy if you want to show TutorialTooltips above all other content, like in FragmentDialogs.

     * @return TutorialTooltipBuilder
     */
    fun attachToWindow(): TutorialTooltipBuilder {
        throwIfCompleted()
        attachMode = AttachMode.Window
        return this
    }

    /**
     * Specify the activity this TutorialTooltip should be attached to.
     *
     *

     * @param dialog dialog to attach this view to
     * *
     * @return TutorialTooltipBuilder
     */
    fun attachToDialog(dialog: Dialog): TutorialTooltipBuilder {
        throwIfCompleted()
        attachMode = AttachMode.Dialog
        this.dialog = dialog
        return this
    }

    /**
     * Set the anchor for the TutorialTooltip

     * @param view    view which will be used as an anchor
     * *
     * @param gravity position relative to the anchor view which the indicator will point to
     * *
     * @return TutorialTooltipBuilder
     */
    @JvmOverloads
    fun anchor(view: View,
               gravity: TutorialTooltipView.Gravity = TutorialTooltipView.Gravity.CENTER): TutorialTooltipBuilder {
        throwIfCompleted()
        this.anchorPoint = null
        this.anchorView = view
        this.anchorGravity = gravity
        return this
    }

    /**
     * Set the anchor point for the TutorialTooltip

     * @param point position where the indicator will be located at
     * *
     * @return TutorialTooltipBuilder
     */
    fun anchor(point: Point): TutorialTooltipBuilder {
        throwIfCompleted()
        this.anchorView = null
        this.anchorPoint = point
        return this
    }

    /**
     * Set the indicator using an `IndicatorBuilder`

     * @param indicatorBuilder IndicatorBuilder
     * *
     * @return TutorialTooltipBuilder
     */
    fun indicator(indicatorBuilder: IndicatorBuilder): TutorialTooltipBuilder {
        throwIfCompleted()
        this.indicatorBuilder = indicatorBuilder
        return this
    }

    /**
     * Set the message using a `MessageBuilder`

     * @param messageBuilder MessageBuilder
     * *
     * @return TutorialTooltipBuilder
     */
    fun message(messageBuilder: MessageBuilder): TutorialTooltipBuilder {
        throwIfCompleted()
        this.messageBuilder = messageBuilder
        return this
    }

    /**
     * Call this to show this TutorialTooltip only once.
     *
     *
     * The counter will increase when the TutorialTooltip is **removed** from the sceen.
     * NOT when it is **shown**.

     * @param identifierRes an tooltipId resource for the TutorialTooltip that will be used to save
     * *                      how often it was shown
     * *
     * @return TutorialTooltipBuilder
     */
    fun oneTimeUse(@StringRes identifierRes: Int): TutorialTooltipBuilder {
        return showCount(context.getString(identifierRes), 1)
    }

    /**
     * Set how often this TutorialTooltip should be shown
     * The counter will increase when the TutorialTooltip is **removed** from the sceen.
     * NOT when it is **shown**.

     * @param identifierRes an tooltipId resource for the TutorialTooltip that will be used to save
     * *                      how often it was shown
     * *
     * @param count         the number of times to show this TutorialTooltip, `null` for infinity
     * *
     * @return TutorialTooltipBuilder
     */
    fun showCount(@StringRes identifierRes: Int, count: Int?): TutorialTooltipBuilder {
        return showCount(context.getString(identifierRes), count)
    }

    /**
     * Set how often this TutorialTooltip should be shown
     * The counter will increase when the TutorialTooltip is **removed** from the sceen.
     * NOT when it is **shown**.

     * @param identifier an tooltipId for the TutorialTooltip that will be used to save
     * *                   how often it was shown already
     * *
     * @param count      the number of times to show this TutorialTooltip, `null` for infinity
     * *
     * @return TutorialTooltipBuilder
     */
    fun showCount(identifier: String, count: Int?): TutorialTooltipBuilder {
        throwIfCompleted()
        this.tooltipId = TooltipId(identifier)
        this.showCount = count
        return this
    }

    /**
     * Set an OnClick listener for the TutorialTooltip

     * @param onTutorialTooltipClickedListener onClick listener
     * *
     * @return TutorialTooltipBuilder
     */
    fun onClick(onTutorialTooltipClickedListener: OnTutorialTooltipClickedListener?): TutorialTooltipBuilder {
        throwIfCompleted()
        this.onTutorialTooltipClickedListener = onTutorialTooltipClickedListener
        return this
    }

    fun onRemoved(onTutorialTooltipRemovedListener: OnTutorialTooltipRemovedListener) {
        this.onTutorialTooltipRemovedListener = onTutorialTooltipRemovedListener
    }

    override fun build(): TutorialTooltipBuilder {
        if (!indicatorBuilder.isCompleted) {
            throw IllegalStateException("IndicatorBuilder was not built!")
        }
        if (!messageBuilder.isCompleted) {
            throw IllegalStateException("MessageBuilder was not built!")
        }

        if (anchorView == null && anchorPoint == null) {
            Log.w(TAG,
                    "You did not specify an anchor or anchor position! The view will be positioned at [0,0].")
        }

        return super.build()
    }

    /**
     * Determines how the TutorialTooltip is attached to the parent view
     * This is used only internally
     */
    enum class AttachMode {
        Window,
        Activity,
        Dialog
    }

    companion object {

        private val TAG = "TutorialTooltipBuilder"
    }
}
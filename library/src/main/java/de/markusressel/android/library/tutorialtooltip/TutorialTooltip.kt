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

package de.markusressel.android.library.tutorialtooltip

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipBuilder
import de.markusressel.android.library.tutorialtooltip.preferences.PreferencesHandler
import de.markusressel.android.library.tutorialtooltip.view.TooltipId
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView
import de.markusressel.android.library.tutorialtooltip.view.ViewHelper
import java.util.*

/**
 * Base TutorialTooltip class
 *
 *
 * Contains Builder and basic construction methods
 *
 *
 * Created by Markus on 17.11.2016.
 */
object TutorialTooltip {

    private val TAG = "TutorialTooltip"

    /**
     * Create the TutorialTooltip

     * @param builder TutorialTooltipBuilder
     * *
     * @return TutorialTooltipView
     */
    fun make(builder: TutorialTooltipBuilder): TutorialTooltipView {
        if (!builder.isCompleted) {
            throw IllegalStateException("Builder is not complete! Did you call build()?")
        }
        return TutorialTooltipView(builder)
    }

    /**
     * Create a TutorialTooltip and show it right away

     * @param builder TutorialTooltipBuilder
     * *
     * @return ID of TutorialTooltip
     */
    fun show(builder: TutorialTooltipBuilder): TooltipId {
        val tutorialTooltipView = make(builder)
        return show(tutorialTooltipView)
    }

    /**
     * Show a TutorialTooltip

     * @param tutorialTooltipView TutorialTooltipView
     * *
     * @return ID of TutorialTooltip
     */
    fun show(tutorialTooltipView: TutorialTooltipView): TooltipId {
        tutorialTooltipView.show()
        return tutorialTooltipView.tutorialTooltipId
    }

    /**
     * Searches through the view tree for instances of TutorialTooltipView
     *
     *
     * WARNING: This only works if the TutorialTooltip was attached to the Activity and NOT to the Window!
     * If you attach the TutorialTooltip to the window you have to keep a reference to its view manually.

     * @param context activity context
     * *
     * @param id      id of TutorialTooltip
     * *
     * @return true if a TutorialTooltip with the given id exists, false otherwise
     */
    fun exists(context: Context, id: TooltipId): Boolean {
        val act = ViewHelper.getActivity(context)
        if (act != null) {
            val rootView = act.window.decorView as ViewGroup
            for (i in 0 until rootView.childCount) {
                val child = rootView.getChildAt(i)
                if (child is TutorialTooltipView && child.tutorialTooltipId == id) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Remove an existing TutorialTooltip via its ID
     *
     *
     * WARNING: This only works if the TutorialTooltip was attached to the Activity and NOT to the Window!
     * If you attach the TutorialTooltip to the window you have to keep a reference to its view manually.

     * @param context  activity context the specified tooltip was added to, application context will not work!
     * *
     * @param id       id of TutorialTooltip
     * *
     * @param animated true fades out, false removes immediately
     * *
     * @return true if a TutorialTooltip was found and removed, false otherwise
     */
    fun remove(context: Context, id: TooltipId, animated: Boolean): Boolean {
        val activity = ViewHelper.getActivity(context)

        activity?.let {
            val rootView = it.window.decorView as ViewGroup
            return removeChild(id, rootView, animated)
        }

        return false
    }

    /**
     * Remove an existing TutorialTooltip via its ID
     *
     *
     * WARNING: This only works if the TutorialTooltip was attached to a Dialog and NOT to the Window or Activity!
     * If you attach the TutorialTooltip to the activity use `remove(Context context, TooltipId id)`
     * If you attach it to the window you have to keep a reference to its view and remove it manually.

     * @param dialog   dialog the specified tooltip was added to
     * *
     * @param id       id of TutorialTooltip
     * *
     * @param animated true fades out, false removes immediately
     * *
     * @return true if a TutorialTooltip was found and removed, false otherwise
     */
    fun remove(dialog: Dialog?, id: TooltipId, animated: Boolean): Boolean {
        dialog?.let {
            val rootView = it.window!!.decorView as ViewGroup
            return removeChild(id, rootView, animated)
        }

        return false
    }

    /**
     * This method traverses through the view tree horizontally
     * and searches for a TutorialTooltipView with the given ID
     * until a matching view is found and removes it.

     * @param id       ID of the TutorialTooltip
     * *
     * @param parent   parent ViewGroup
     * *
     * @param animated true fades out, false removes immediately
     * *
     * @return true if a TutorialTooltip was found and removed, false otherwise
     */
    private fun removeChild(id: TooltipId, parent: ViewGroup, animated: Boolean): Boolean {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is TutorialTooltipView && child.tutorialTooltipId == id) {
                child.remove(animated)
                return true
            }
        }

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is ViewGroup) {
                if (removeChild(id, child, animated)) {
                    return true
                }
            }
        }

        return false
    }

    /**
     * Remove an existing TutorialTooltip

     * @param tutorialTooltipView TutorialTooltipView to remove
     * *
     * @param animated            true fades out, false removes immediately
     */
    fun remove(tutorialTooltipView: TutorialTooltipView, animated: Boolean) {
        tutorialTooltipView.remove(animated)
    }

    /**
     * Remove all existing TutorialTooltips from activity
     *
     *
     * WARNING: This does not remove TutorialTooltips that are attached to the window!
     * If you attach the TutorialTooltip to the window you have to keep a reference to its view manually.

     * @param context  activity context the specified tooltip was added to, application context will not work!
     * *
     * @param animated true fades out, false removes immediately
     */
    fun removeAll(context: Context, animated: Boolean) {
        val activity = ViewHelper.getActivity(context)

        activity?.let {
            val rootView = it.window.decorView as ViewGroup

            val childrenToRemove = LinkedList<Int>()

            for (i in 0 until rootView.childCount) {
                val child = rootView.getChildAt(i)
                if (child is TutorialTooltipView) {
                    val tutorialTooltipView = child
                    childrenToRemove.add(i)
                }
            }

            childrenToRemove.reverse()

            childrenToRemove
                    .map { rootView.getChildAt(it) as TutorialTooltipView }
                    .forEach { it.remove(animated) }
        }
    }

    /**
     * Reset the show count for a TutorialTooltip ID

     * @param applicationContext application context (for access to SharedPreferences)
     * *
     * @param tooltipId          the TutorialTooltip ID to reset the count for
     */
    fun resetShowCount(applicationContext: Context,
                       tooltipId: TooltipId) {
        val preferencesHandler = PreferencesHandler(applicationContext)
        preferencesHandler.resetCount(tooltipId)
    }

    /**
     * Reset the show count for a TutorialTooltip

     * @param tutorialTooltipView the TutorialTooltip to reset the count for
     */
    fun resetShowCount(tutorialTooltipView: TutorialTooltipView) {
        val preferencesHandler = PreferencesHandler(tutorialTooltipView.context)
        preferencesHandler.resetCount(tutorialTooltipView)
    }

    /**
     * Reset the show count for ALL TutorialTooltips

     * @param context application context
     */
    fun resetAllShowCount(context: Context) {
        val preferencesHandler = PreferencesHandler(context)
        preferencesHandler.clearAll()
    }

}

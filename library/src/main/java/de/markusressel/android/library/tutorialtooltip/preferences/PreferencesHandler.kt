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

package de.markusressel.android.library.tutorialtooltip.preferences

import android.content.Context
import android.content.SharedPreferences

import de.markusressel.android.library.tutorialtooltip.R
import de.markusressel.android.library.tutorialtooltip.view.TooltipId
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView

/**
 * Preferences used to remember if a ToolTip has already been shown
 *
 *
 * Created by Markus on 11.04.2017.
 */
class PreferencesHandler

/**
 * Constructor
 * @param context application context
 */
(context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE)

    /**
     * Get a preference value by key

     * @param tutorialTooltipView the TutorialTooltip to get the count for
     *
     * @return the show count
     */
    fun getCount(tutorialTooltipView: TutorialTooltipView): Int {
        return sharedPref.getInt(keyPrefix + tutorialTooltipView.tutorialTooltipId, 0)
    }

    /**
     * Set the count for the specified TutorialTooltip to a specific value

     * @param tooltipId the TutorialTooltip to set the count for
     * *
     * @param count     the count to set
     */
    private fun setCount(tooltipId: TooltipId, count: Int) {
        val editor = sharedPref.edit()
        editor.putInt(keyPrefix + tooltipId, count)
        editor.apply()
    }

    /**
     * Set the count for the specified TutorialTooltip to a specific value

     * @param tutorialTooltipView the TutorialTooltip to set the count for
     * *
     * @param count               the count to set
     */
    private fun setCount(tutorialTooltipView: TutorialTooltipView, count: Int) {
        setCount(tutorialTooltipView.tutorialTooltipId, count)
    }

    /**
     * Increase the count for the specified TutorialTooltip

     * @param tutorialTooltipView the TutorialTooltip to increase the count for
     */
    fun increaseCount(tutorialTooltipView: TutorialTooltipView) {
        setCount(tutorialTooltipView, getCount(tutorialTooltipView) + 1)
    }

    /**
     * Reset the show count of the specified TutorialTooltip to 0.

     * @param tooltipId the TutorialTooltip ID to reset count for
     */
    fun resetCount(tooltipId: TooltipId) {
        setCount(tooltipId, 0)
    }

    /**
     * Reset the show count of the specified TutorialTooltip to 0.

     * @param tutorialTooltipView the TutorialTooltip to reset count for
     */
    fun resetCount(tutorialTooltipView: TutorialTooltipView) {
        setCount(tutorialTooltipView.tutorialTooltipId, 0)
    }

    /**
     * Remove all saved preferences
     */
    fun clearAll() {
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val keyPrefix = "TutorialTooltip_"
    }

}

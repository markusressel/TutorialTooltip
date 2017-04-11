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

package de.markusressel.android.library.tutorialtooltip.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import de.markusressel.android.library.tutorialtooltip.R;
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView;

/**
 * Preferences used to remember if a ToolTip has already been shown
 * <p>
 * Created by Markus on 11.04.2017.
 */
public class PreferencesHandler {

    private static final String keyPrefix = "TutorialTooltip_";
    private final SharedPreferences sharedPref;

    /**
     * Constructor
     *
     * @param context application context
     */
    public PreferencesHandler(@NonNull Context context) {
        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
    }

    /**
     * Get a preference value by key
     *
     * @param tutorialTooltipView the TutorialTooltip to get the count for
     * @return the show count
     */
    public int getCount(@NonNull TutorialTooltipView tutorialTooltipView) {
        return sharedPref.getInt(keyPrefix + tutorialTooltipView.getTutorialTooltipIdentifier(), 0);
    }

    /**
     * Set the count for the specified TutorialTooltip to a specific value
     *
     * @param tutorialTooltipView the TutorialTooltip to set the count for
     * @param count               the count to set
     */
    public void setCount(@NonNull TutorialTooltipView tutorialTooltipView, int count) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(keyPrefix + tutorialTooltipView.getTutorialTooltipIdentifier(), count);
        editor.apply();
    }

    /**
     * Increase the count for the specified TutorialTooltip
     *
     * @param tutorialTooltipView the TutorialTooltip to increase the count for
     */
    public void increaseCount(@NonNull TutorialTooltipView tutorialTooltipView) {
        setCount(tutorialTooltipView, getCount(tutorialTooltipView) + 1);
    }

    /**
     * Reset the show count of the specified TutorialTooltip to 0.
     *
     * @param tutorialTooltipView the TutorialTooltip to reset count for
     */
    public void resetCount(@NonNull TutorialTooltipView tutorialTooltipView) {
        setCount(tutorialTooltipView, 0);
    }

    /**
     * Remove all saved preferences
     */
    public void clearAll() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

}

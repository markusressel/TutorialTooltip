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

package de.markusressel.android.library.tutorialtooltip.interfaces;

import android.view.View;

/**
 * OnClick listener for a TutorialTooltip
 * <p>
 * Created by Markus on 28.11.2016.
 */
public abstract class OnTutorialTooltipClickedListener {

    /**
     * This method is called when the indicator view is clicked
     *
     * @param id            ID of the TutorialTooltip
     * @param indicator     indicator
     * @param indicatorView indicator view
     */
    public void onIndicatorClicked(int id, TutorialTooltipIndicator indicator, View indicatorView) {
        if (indicatorView.isClickable()) {
            // disable listening for click events to pass them to the parent view
            indicatorView.setClickable(false);
            // perform click again on parent
            // doesnt work yet :(
            ((View) indicatorView.getParent()).performClick();
        }
    }

    /**
     * This method is called when the message view is clicked
     *
     * @param id          ID of the TutorialTooltip
     * @param message     message
     * @param messageView message view
     */
    public void onMessageClicked(int id, TutorialTooltipMessage message, View messageView) {
        if (messageView.isClickable()) {
            // disable listening for click events to pass them to the parent view
            messageView.setClickable(false);
            // perform click again on parent
            // doesnt work yet :(
            ((View) messageView.getParent()).performClick();
        }
    }

}

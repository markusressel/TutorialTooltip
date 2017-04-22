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

import de.markusressel.android.library.tutorialtooltip.view.TooltipId;
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView;

/**
 * OnClick listener for a TutorialTooltip
 * <p>
 * If you override any of the on*Clicked methods remember to return <code>true</code>
 * in the corresponding is*Clickable() methods.
 * <p>
 * Created by Markus on 28.11.2016.
 */
public interface OnIndicatorClickedListener {

    /**
     * This method is called when the indicator view is clicked
     *
     * @param id                  ID of the TutorialTooltip
     * @param tutorialTooltipView TutorialTooltipView
     * @param indicator           indicator
     * @param indicatorView       the same object as the indicator, but cast to View
     */
    void onIndicatorClicked(TooltipId id, TutorialTooltipView tutorialTooltipView,
            TutorialTooltipIndicator indicator, View indicatorView);
}

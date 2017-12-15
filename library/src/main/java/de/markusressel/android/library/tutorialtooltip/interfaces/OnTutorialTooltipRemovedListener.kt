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

package de.markusressel.android.library.tutorialtooltip.interfaces

import de.markusressel.android.library.tutorialtooltip.view.TooltipId
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView

/**
 * Created by Markus on 24.12.2016.
 */
interface OnTutorialTooltipRemovedListener {

    /**
     * This method is called when remove() is called on the TutorialTooltipView and the view will begin to close/fade out

     * @param id                  TutorialTooltip id
     * *
     * @param tutorialTooltipView TutorialTooltipView
     */
    fun onRemove(id: TooltipId, tutorialTooltipView: TutorialTooltipView)

    /**
     * This method is called when the TutorialTooltipView is completely removed from the view

     * @param id                  TutorialTooltip id
     * *
     * @param tutorialTooltipView TutorialTooltipView
     */
    fun postRemove(id: TooltipId, tutorialTooltipView: TutorialTooltipView)

}

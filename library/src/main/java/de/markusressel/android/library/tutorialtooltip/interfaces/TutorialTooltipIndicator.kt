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

import android.view.View
import androidx.annotation.ColorInt

/**
 * Interface that defines methods a custom implementation for TutorialTooltipIndicator must implement
 *
 *
 * Created by Markus on 24.11.2016.
 */
interface TutorialTooltipIndicator {

    /**
     * Set the main color for this indicator

     * @param color color as integer
     */
    fun setColor(@ColorInt color: Int)

    /**
     * Set an onClick listener for this indicator

     * @param listener
     */
    fun setOnClickListener(listener: View.OnClickListener?)

}

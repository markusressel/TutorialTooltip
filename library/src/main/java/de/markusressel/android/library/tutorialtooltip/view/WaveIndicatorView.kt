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

package de.markusressel.android.library.tutorialtooltip.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.ColorInt
import de.markusressel.android.library.circlewaveview.CircleWaveView
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipIndicator

/**
 * Basic indicator view
 *
 *
 * Shows rhythmic waves expanding from the center
 *
 *
 * Created by Markus on 24.11.2016.
 */
class WaveIndicatorView : CircleWaveView, TutorialTooltipIndicator {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context,
            attrs,
            defStyleAttr)

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun setColor(@ColorInt color: Int) {
        startColor = color
        endColor = Color.argb(0, Color.red(color), Color.green(color), Color.blue(color))
    }

}

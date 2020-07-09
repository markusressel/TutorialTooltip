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

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage

/**
 * Basic Message view implementation
 *
 *
 * Created by Markus on 24.11.2016.
 */
class BasicMessageView : AppCompatTextView, TutorialTooltipMessage {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context,
            attrs,
            defStyleAttr) {
    }

}

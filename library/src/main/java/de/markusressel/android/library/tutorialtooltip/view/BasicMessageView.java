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

package de.markusressel.android.library.tutorialtooltip.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage;

/**
 * Basic Message view implementation
 * <p>
 * Created by Markus on 24.11.2016.
 */
public class BasicMessageView extends AppCompatTextView implements TutorialTooltipMessage {

    public BasicMessageView(Context context) {
        super(context);
    }

    public BasicMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasicMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}

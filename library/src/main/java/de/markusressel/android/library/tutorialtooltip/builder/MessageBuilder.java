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

package de.markusressel.android.library.tutorialtooltip.builder;

import android.content.Context;

import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView;

/**
 * Builder to create the message view
 * <p>
 * Created by Markus on 01.12.2016.
 */
public class MessageBuilder extends Builder<MessageBuilder> {

    private final Context context;

    /**
     * Message text
     */
    private String text = "Your Tutorial Message is shown right here.";

    /**
     * Message gravity
     */
    private TutorialTooltipView.Gravity gravity = TutorialTooltipView.Gravity.TOP;

    public MessageBuilder(Context context) {
        this.context = context;
    }

    /**
     * Set the tutorial text
     *
     * @param text message
     * @return MessageBuilder
     */
    public MessageBuilder text(String text) {
        throwIfCompleted();
        this.text = text;
        return this;
    }

    /**
     * Set the gravity of the message view
     * <p>
     * The message view will be positioned relative to the indicator view
     *
     * @param gravity message view gravity
     * @return MessageBuilder
     */
    public MessageBuilder gravity(TutorialTooltipView.Gravity gravity) {
        throwIfCompleted();
        this.gravity = gravity;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public String getText() {
        return text;
    }

    public TutorialTooltipView.Gravity getGravity() {
        return gravity;
    }
}

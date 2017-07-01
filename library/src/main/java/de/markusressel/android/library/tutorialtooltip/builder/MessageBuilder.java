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
import android.graphics.Point;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.LinearLayout;

import de.markusressel.android.library.tutorialtooltip.interfaces.OnMessageClickedListener;
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage;
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView;
import lombok.Getter;

/**
 * Builder to create the message view
 * <p>
 * Created by Markus on 01.12.2016.
 */
public final class MessageBuilder extends Builder<MessageBuilder> {

    /**
     * Constant size value to wrap the views content
     */
    @SuppressWarnings("WeakerAccess,unused")
    public static final int WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT;

    /**
     * Constant size value to match the parents size
     */
    @SuppressWarnings("WeakerAccess,unused")
    public static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

    /**
     * Constant size value to match the anchors view size
     */
    @SuppressWarnings("WeakerAccess,unused")
    public static final int MATCH_ANCHOR = -5;
    private final Context context;
    /**
     * Specifies if a custom view is used
     */
    @Getter
    private Type type = Type.Default;
    /**
     * Message text
     */
    @Getter
    private String text = "Your Tutorial Message is shown right here.";
    /**
     * Anchor point if no view is used
     */
    @Getter
    private Point anchorPoint;
    /**
     * Anchor view
     */
    @Getter
    private View anchorView;
    /**
     * Custom message view
     */
    @Getter
    private View customView;
    /**
     * Message gravity
     */
    @Getter
    private TutorialTooltipView.Gravity gravity = TutorialTooltipView.Gravity.TOP;
    /**
     * Message x axis offset from anchor position
     */
    @Getter
    private int offsetX = 0;
    /**
     * Message y axis offset from anchor position
     */
    @Getter
    private int offsetY = 0;
    /**
     * Message x axis size (width)
     */
    @Getter
    private int width = WRAP_CONTENT;
    /**
     * Message y axis size (height)
     */
    @Getter
    private int height = WRAP_CONTENT;
    /**
     * Text color
     */
    @Getter
    @ColorInt
    private Integer textColor;
    /**
     * Background color
     */
    @Getter
    @ColorInt
    private Integer backgroundColor;
    /**
     * OnClick listener
     */
    @Getter
    private OnMessageClickedListener onMessageClickedListener;

    /**
     * Constructor
     *
     * @param context application context
     */
    public MessageBuilder(Context context) {
        this.context = context;
    }

    /**
     * Set a custom anchor point for the message view
     *
     * @param anchorPoint anchor point
     * @return MessageBuilder
     */
    @SuppressWarnings("unused")
    public MessageBuilder anchor(Point anchorPoint) {
        throwIfCompleted();
        this.anchorPoint = anchorPoint;
        return this;
    }

    /**
     * Set a custom anchor view for the message view
     * <p>
     * If no anchor view or point is specified
     * the message will be positioned relative to the indicator view.
     *
     * @param anchorView anchor view
     * @return MessageBuilder
     */
    @SuppressWarnings("unused")
    public MessageBuilder anchor(View anchorView) {
        throwIfCompleted();
        this.anchorView = anchorView;
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
    @SuppressWarnings("unused")
    public MessageBuilder gravity(TutorialTooltipView.Gravity gravity) {
        throwIfCompleted();
        this.gravity = gravity;
        return this;
    }

    /**
     * Set the tutorial text
     *
     * @param text message
     * @return MessageBuilder
     */
    @SuppressWarnings("unused")
    public MessageBuilder text(String text) {
        throwIfCompleted();
        this.text = text;
        return this;
    }

    /**
     * Set the tutorial text
     *
     * @param textRes message resource
     * @return MessageBuilder
     */
    @SuppressWarnings("unused")
    public MessageBuilder text(@StringRes int textRes) {
        throwIfCompleted();
        this.text = context.getString(textRes);
        return this;
    }

    /**
     * Set a custom size for the message
     * <p>
     *
     * @param width  x-axis size in pixel
     * @param height y-axis size in pixel
     * @return MessageBuilder
     */
    @SuppressWarnings("unused")
    public MessageBuilder size(@Dimension(unit = Dimension.PX) int width,
            @Dimension(unit = Dimension.PX) int height) {
        throwIfCompleted();
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Set an offset for the message view
     *
     * @param offsetX x-axis offset in pixel (positive is right, negative is left)
     * @param offsetY y-axis offset in pixel (positive is down, negative is up)
     * @return MessageBuilder
     */
    @SuppressWarnings("unused")
    public MessageBuilder offset(@Dimension(unit = Dimension.PX) int offsetX,
            @Dimension(unit = Dimension.PX) int offsetY) {
        throwIfCompleted();
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        return this;
    }

    /**
     * Set the text color for the message
     *
     * @param textColor text color
     * @return MessageBuilder
     */
    @SuppressWarnings("unused")
    public MessageBuilder textColor(@ColorInt int textColor) {
        throwIfCompleted();
        this.textColor = textColor;
        return this;
    }

    /**
     * Set the background color for the message
     *
     * @param backgroundColor background color
     * @return MessageBuilder
     */
    @SuppressWarnings("unused")
    public MessageBuilder backgroundColor(@ColorInt int backgroundColor) {
        throwIfCompleted();
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Set a custom view that should be used as the indicator
     * <p>
     * To build your own indicator view, you have to create a new class extending <code>View</code>
     * and implement <code>TutorialTooltipIndicator</code>
     *
     * @param messageView custom indicator view
     * @return MessageBuilder
     */
    @SuppressWarnings("unused")
    public <T extends View & TutorialTooltipMessage> MessageBuilder customView(T messageView) {
        throwIfCompleted();
        this.type = Type.Custom;
        this.customView = messageView;
        return this;
    }

    /**
     * Set an onClick listener for the message
     *
     * @param onMessageClickedListener onClick listener
     * @return MessageBuilder
     */
    @SuppressWarnings("unused")
    public MessageBuilder onClick(OnMessageClickedListener onMessageClickedListener) {
        throwIfCompleted();
        this.onMessageClickedListener = onMessageClickedListener;
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public enum Type {
        Default,
        Custom
    }

}

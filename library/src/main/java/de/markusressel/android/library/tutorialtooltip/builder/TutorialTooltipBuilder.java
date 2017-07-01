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

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;

import de.markusressel.android.library.tutorialtooltip.interfaces.OnTutorialTooltipClickedListener;
import de.markusressel.android.library.tutorialtooltip.interfaces.OnTutorialTooltipRemovedListener;
import de.markusressel.android.library.tutorialtooltip.view.TooltipId;
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView;
import lombok.Getter;

/**
 * Use this Builder to create a TutorialTooltip
 * <p>
 * Created by Markus on 01.12.2016.
 */
public final class TutorialTooltipBuilder extends Builder<TutorialTooltipBuilder> {

    private static final String TAG = "TutorialTooltipBuilder";

    /**
     * Activity context
     */
    @Getter
    private Context context;

    /**
     * AttachMode used to distinguish between activity, dialog and window scope
     */
    @Getter
    private AttachMode attachMode;


    /**
     * Dialog the TutorialTooltip will be attached to (if AttachMode is Dialog)
     */
    @Getter
    private Dialog dialog;

    /**
     * ID the TutorialTooltip will get
     */
    @Getter
    private TooltipId tooltipId;

    /**
     * The amount of times to show this TutorialTooltip
     */
    @Getter
    private Integer showCount;

    /**
     * Anchor view
     * This view is used to position the indicator view
     */
    @Getter
    private View anchorView;

    /**
     * Anchor gravity used to position the indicator using the anchorView borders (or center)
     */
    @Getter
    private TutorialTooltipView.Gravity anchorGravity;

    /**
     * Exact coordinates the indicator should be positioned
     */
    @Getter
    private Point anchorPoint;

    /**
     * IndicatorBuilder
     */
    @Getter
    private IndicatorBuilder indicatorBuilder;

    /**
     * MessageBuilder
     */
    @Getter
    private MessageBuilder messageBuilder;

    /**
     * OnClick listener for the whole TutorialTooltipView
     */
    @Getter
    private OnTutorialTooltipClickedListener onTutorialTooltipClickedListener;

    /**
     * Listener for TutorialTooltip remove() events
     */
    @Getter
    private OnTutorialTooltipRemovedListener onTutorialTooltipRemovedListener;

    /**
     * Constructor for the builder.
     * Chain methods and call ".build()" as your last step to make this object immutable.
     *
     * @param context activity context that the TutorialTooltip will be added to.
     *                Application context will not suffice!
     */
    public TutorialTooltipBuilder(@NonNull Context context) {
        this.context = context;

        // set default values
        this.attachMode = AttachMode.Activity;
        this.indicatorBuilder = new IndicatorBuilder().build();
        this.messageBuilder = new MessageBuilder(context).build();

        this.tooltipId = new TooltipId();
    }

    /**
     * Specify whether the TutorialTooltip should be attached to the Window or the activity.
     * <p>
     * This can be handy if you want to show TutorialTooltips above all other content, like in FragmentDialogs.
     *
     * @return TutorialTooltipBuilder
     */
    @SuppressWarnings("unused")
    public TutorialTooltipBuilder attachToWindow() {
        throwIfCompleted();
        attachMode = AttachMode.Window;
        return this;
    }

    /**
     * Specify the activity this TutorialTooltip should be attached to.
     * <p>
     *
     * @param dialog dialog to attach this view to
     * @return TutorialTooltipBuilder
     */
    @SuppressWarnings("unused")
    public TutorialTooltipBuilder attachToDialog(@NonNull Dialog dialog) {
        throwIfCompleted();
        attachMode = AttachMode.Dialog;
        this.dialog = dialog;
        return this;
    }

    /**
     * Set the anchor for the TutorialTooltip
     *
     * @param view view which will be used as an anchor
     * @return TutorialTooltipBuilder
     */
    @SuppressWarnings("unused")
    public TutorialTooltipBuilder anchor(@NonNull View view) {
        return anchor(view, TutorialTooltipView.Gravity.CENTER);
    }

    /**
     * Set the anchor for the TutorialTooltip
     *
     * @param view    view which will be used as an anchor
     * @param gravity position relative to the anchor view which the indicator will point to
     * @return TutorialTooltipBuilder
     */
    @SuppressWarnings("unused")
    public TutorialTooltipBuilder anchor(@NonNull View view,
            @NonNull TutorialTooltipView.Gravity gravity) {
        throwIfCompleted();
        this.anchorPoint = null;
        this.anchorView = view;
        this.anchorGravity = gravity;
        return this;
    }

    /**
     * Set the anchor point for the TutorialTooltip
     *
     * @param point position where the indicator will be located at
     * @return TutorialTooltipBuilder
     */
    @SuppressWarnings("unused")
    public TutorialTooltipBuilder anchor(@NonNull final Point point) {
        throwIfCompleted();
        this.anchorView = null;
        this.anchorPoint = point;
        return this;
    }

    /**
     * Set the indicator using an <code>IndicatorBuilder</code>
     *
     * @param indicatorBuilder IndicatorBuilder
     * @return TutorialTooltipBuilder
     */
    @SuppressWarnings("unused")
    public TutorialTooltipBuilder indicator(@NonNull IndicatorBuilder indicatorBuilder) {
        throwIfCompleted();
        this.indicatorBuilder = indicatorBuilder;
        return this;
    }

    /**
     * Set the message using a <code>MessageBuilder</code>
     *
     * @param messageBuilder MessageBuilder
     * @return TutorialTooltipBuilder
     */
    @SuppressWarnings("unused")
    public TutorialTooltipBuilder message(@NonNull MessageBuilder messageBuilder) {
        throwIfCompleted();
        this.messageBuilder = messageBuilder;
        return this;
    }

    /**
     * Call this to show this TutorialTooltip only once.
     * <p>
     * The counter will increase when the TutorialTooltip is <b>removed</b> from the sceen.
     * NOT when it is <b>shown</b>.
     *
     * @param identifierRes an tooltipId resource for the TutorialTooltip that will be used to save
     *                      how often it was shown
     * @return TutorialTooltipBuilder
     */
    @SuppressWarnings("unused")
    public TutorialTooltipBuilder oneTimeUse(@StringRes int identifierRes) {
        return showCount(context.getString(identifierRes), 1);
    }

    /**
     * Set how often this TutorialTooltip should be shown
     * The counter will increase when the TutorialTooltip is <b>removed</b> from the sceen.
     * NOT when it is <b>shown</b>.
     *
     * @param identifierRes an tooltipId resource for the TutorialTooltip that will be used to save
     *                      how often it was shown
     * @param count         the number of times to show this TutorialTooltip, <code>null</code> for infinity
     * @return TutorialTooltipBuilder
     */
    @SuppressWarnings("unused")
    public TutorialTooltipBuilder showCount(@StringRes int identifierRes, @Nullable Integer count) {
        return showCount(context.getString(identifierRes), count);
    }

    /**
     * Set how often this TutorialTooltip should be shown
     * The counter will increase when the TutorialTooltip is <b>removed</b> from the sceen.
     * NOT when it is <b>shown</b>.
     *
     * @param identifier an tooltipId for the TutorialTooltip that will be used to save
     *                   how often it was shown already
     * @param count      the number of times to show this TutorialTooltip, <code>null</code> for infinity
     * @return TutorialTooltipBuilder
     */
    @SuppressWarnings("unused")
    public TutorialTooltipBuilder showCount(@NonNull String identifier, @Nullable Integer count) {
        throwIfCompleted();
        this.tooltipId = new TooltipId(identifier);
        this.showCount = count;
        return this;
    }

    /**
     * Set an OnClick listener for the TutorialTooltip
     *
     * @param onTutorialTooltipClickedListener onClick listener
     * @return TutorialTooltipBuilder
     */
    @SuppressWarnings("unused")
    public TutorialTooltipBuilder onClick(@Nullable
            OnTutorialTooltipClickedListener onTutorialTooltipClickedListener) {
        throwIfCompleted();
        this.onTutorialTooltipClickedListener = onTutorialTooltipClickedListener;
        return this;
    }

    void onRemoved(OnTutorialTooltipRemovedListener onTutorialTooltipRemovedListener) {
        this.onTutorialTooltipRemovedListener = onTutorialTooltipRemovedListener;
    }

    @Override
    public TutorialTooltipBuilder build() {
        if (!indicatorBuilder.isCompleted()) {
            throw new IllegalStateException("IndicatorBuilder was not built!");
        }
        if (!messageBuilder.isCompleted()) {
            throw new IllegalStateException("MessageBuilder was not built!");
        }

        if (anchorView == null && anchorPoint == null) {
            Log.w(TAG,
                    "You did not specify an anchor or anchor position! The view will be positioned at [0,0].");
        }

        return super.build();
    }

    /**
     * Determines how the TutorialTooltip is attached to the parent view
     * This is used only internally
     */
    public enum AttachMode {
        Window,
        Activity,
        Dialog
    }
}

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
import android.view.View;

import de.markusressel.android.library.tutorialtooltip.interfaces.OnTutorialTooltipClickedListener;
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView;

/**
 * Use this Builder to create a TutorialTooltip
 * <p>
 * Created by Markus on 01.12.2016.
 */
public final class TutorialTooltipBuilder extends Builder<TutorialTooltipBuilder> {

    /**
     * Determines how the TutorialTooltip is attached to the parent view
     * This is used only internally
     */
    public enum AttachMode {
        Window,
        Activity,
        Dialog
    }

    /**
     * Activity context
     */
    private Context context;

    /**
     * AttachMode used to distinguish between activity, dialog and window scope
     */
    private AttachMode attachMode;

    /**
     * Dialog the TutorialTooltip will be attached to (if AttachMode is Dialog)
     */
    private Dialog dialog;

    /**
     * last used ID value, used to determine the next valid and unused ID
     */
    private static int lastId = 0;

    /**
     * ID the TutorialTooltip will get
     */
    private int id;

    /**
     * Anchor view
     * This view is used to position the indicator view
     */
    private View anchorView;

    /**
     * Anchor gravity used to position the indicator using the anchorView borders (or center)
     */
    private TutorialTooltipView.Gravity anchorGravity;

    /**
     * Exact coordinates the indicator should be positioned
     */
    private Point anchorPoint;

    /**
     * IndicatorBuilder
     */
    private IndicatorBuilder indicatorBuilder;

    /**
     * MessageBuilder
     */
    private MessageBuilder messageBuilder;

    /**
     * OnClick listener for the indicator and message view
     */
    private OnTutorialTooltipClickedListener onTutorialTooltipClickedListener;

    /**
     * Constructor for the builder.
     * Chain methods and call ".build()" as your last step to make this object immutable.
     *
     * @param context activity context that the TutorialTooltip will be added to.
     *                Application context will not suffice!
     */
    public TutorialTooltipBuilder(Context context) {
        this.context = context;

        // set default values
        this.attachMode = AttachMode.Activity;
        this.indicatorBuilder = new IndicatorBuilder(context).build();
        this.messageBuilder = new MessageBuilder(context).build();

        this.id = ++lastId;
    }

    /**
     * Specify whether the TutorialTooltip should be attached to the Window or the activity.
     * <p>
     * This can be handy if you want to show TutorialTooltips above all other content, like in FragmentDialogs.
     *
     * @return TutorialTooltipBuilder
     */
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
    public TutorialTooltipBuilder attachToDialog(Dialog dialog) {
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
    public TutorialTooltipBuilder anchor(View view) {
        return anchor(view, TutorialTooltipView.Gravity.CENTER);
    }

    /**
     * Set the anchor for the TutorialTooltip
     *
     * @param view    view which will be used as an anchor
     * @param gravity position relative to the anchor view which the indicator will point to
     * @return TutorialTooltipBuilder
     */
    public TutorialTooltipBuilder anchor(View view, TutorialTooltipView.Gravity gravity) {
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
    public TutorialTooltipBuilder anchor(final Point point) {
        throwIfCompleted();
        this.anchorView = null;
        this.anchorPoint = point;
        return this;
    }

    /**
     * Set the indicator using an <code>IndicatorBuilder</code>
     *
     * @param indicatorBuilder Indicator builder
     * @return TutorialTooltipBuilder
     */
    public TutorialTooltipBuilder indicator(IndicatorBuilder indicatorBuilder) {
        throwIfCompleted();
        this.indicatorBuilder = indicatorBuilder;
        return this;
    }

    /**
     * Set the message using a <code>MessageBuilder</code>
     */
    public TutorialTooltipBuilder message(MessageBuilder messageBuilder) {
        throwIfCompleted();
        this.messageBuilder = messageBuilder;
        return this;
    }

    /**
     * Set an OnClick listener for the TutorialTooltip
     *
     * @param onTutorialTooltipClickedListener onClick listener
     * @return TutorialTooltipBuilder
     */
    public TutorialTooltipBuilder onClickListener(
            OnTutorialTooltipClickedListener onTutorialTooltipClickedListener) {
        throwIfCompleted();
        this.onTutorialTooltipClickedListener = onTutorialTooltipClickedListener;
        return this;
    }

    public AttachMode getAttachMode() {
        return attachMode;
    }

    public Context getContext() {
        return context;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public int getId() {
        return id;
    }

    public Point getAnchorPoint() {
        return anchorPoint;
    }

    public View getAnchorView() {
        return anchorView;
    }

    public TutorialTooltipView.Gravity getAnchorGravity() {
        return anchorGravity;
    }

    public IndicatorBuilder getIndicatorBuilder() {
        return indicatorBuilder;
    }

    public MessageBuilder getMessageBuilder() {
        return messageBuilder;
    }

    public OnTutorialTooltipClickedListener getOnTutorialTooltipClickedListener() {
        return onTutorialTooltipClickedListener;
    }

    @Override
    public TutorialTooltipBuilder build() {
        if (!indicatorBuilder.isCompleted()) {
            throw new IllegalStateException("IndicatorBuilder was not built!");
        }
        if (!messageBuilder.isCompleted()) {
            throw new IllegalStateException("MessageBuilder was not built!");
        }

        return super.build();
    }
}

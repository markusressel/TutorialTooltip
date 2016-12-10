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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

import de.markusressel.android.library.tutorialtooltip.R;
import de.markusressel.android.library.tutorialtooltip.builder.IndicatorBuilder;
import de.markusressel.android.library.tutorialtooltip.builder.MessageBuilder;
import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipBuilder;
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipIndicator;
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * TutorialTooltip View class
 * <p>
 * Created by Markus on 17.11.2016.
 */
public class TutorialTooltipView extends LinearLayout {

    private static final String TAG = "TutorialTooltipView";

    private int tooltipId;

    private Dialog dialog;

    private TutorialTooltipBuilder tutorialTooltipBuilder;
    private IndicatorBuilder indicatorBuilder;
    private MessageBuilder messageBuilder;

    private Gravity anchorGravity = Gravity.CENTER;
    private WeakReference<View> anchorView;
    private Point anchorPoint;

    private FrameLayout indicatorLayout;
    private FrameLayout messageLayout;

    private TutorialTooltipIndicator indicatorView;
    private TutorialTooltipMessage messageView;

    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    updatePositions();
                }
            };

    private TutorialTooltipBuilder.AttachMode attachMode;

    public enum Gravity {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        CENTER
    }

    protected TutorialTooltipView(Context context) {
        super(context);
    }

    public TutorialTooltipView(TutorialTooltipBuilder tutorialTooltipBuilder) {
        this(tutorialTooltipBuilder.getContext());

        getBuilderValues(tutorialTooltipBuilder);

        initializeViews();

        updateValues();

        getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    private void getBuilderValues(TutorialTooltipBuilder tutorialTooltipBuilder) {
        this.tutorialTooltipBuilder = tutorialTooltipBuilder;

        tooltipId = tutorialTooltipBuilder.getId();

        attachMode = tutorialTooltipBuilder.getAttachMode();
        dialog = tutorialTooltipBuilder.getDialog();

        anchorGravity = tutorialTooltipBuilder.getAnchorGravity();
        if (tutorialTooltipBuilder.getAnchorView() != null) {
            anchorView = new WeakReference<>(tutorialTooltipBuilder.getAnchorView());
        }
        anchorPoint = tutorialTooltipBuilder.getAnchorPoint();

        indicatorBuilder = tutorialTooltipBuilder.getIndicatorBuilder();
        switch (indicatorBuilder.getType()) {
            case Custom:
                indicatorView = (TutorialTooltipIndicator) indicatorBuilder.getCustomView();
                break;
            case Default:
            default:
                break;
        }

        messageBuilder = tutorialTooltipBuilder.getMessageBuilder();
        switch (messageBuilder.getType()) {
            case Custom:
                messageView = (TutorialTooltipMessage) messageBuilder.getCustomView();
                break;
            case Default:
            default:
                break;
        }

    }

    private void initializeViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (tutorialTooltipBuilder.getOnTutorialTooltipClickedListener() != null) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tutorialTooltipBuilder.getOnTutorialTooltipClickedListener() != null) {
                        tutorialTooltipBuilder.getOnTutorialTooltipClickedListener()
                                .onTutorialTooltipClicked(getTutorialTooltipId(),
                                        getTutorialTooltipView());
                    }
                }
            });
        }

        indicatorLayout = (FrameLayout) inflater.inflate(R.layout.layout_indicator,
                this,
                false);
        if (indicatorView == null) {
            indicatorView = (WaveIndicatorView) indicatorLayout.findViewById(R.id.indicator);
        } else {
            indicatorLayout.removeAllViews();
            indicatorLayout.addView((View) indicatorView, WRAP_CONTENT, WRAP_CONTENT);
        }

        // Set onClick listeners
        if (indicatorBuilder.getOnIndicatorClickedListener() != null) {

            indicatorLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (indicatorBuilder.getOnIndicatorClickedListener() != null) {
                        indicatorBuilder.getOnIndicatorClickedListener()
                                .onIndicatorClicked(getTutorialTooltipId(),
                                        indicatorView, (View) indicatorView);
                    }
                }
            });
        }

        messageLayout = (FrameLayout) inflater.inflate(R.layout.layout_tutorial_text, this, false);

        if (messageView == null) {
            messageView = (BasicMessageView) messageLayout.findViewById(R.id.messageView);
        } else {
            messageLayout.removeAllViews();
            messageLayout.addView((View) messageView, WRAP_CONTENT, WRAP_CONTENT);
        }

        if (messageBuilder.getOnMessageClickedListener() != null) {
            messageLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (messageBuilder.getOnMessageClickedListener() != null) {
                        messageBuilder.getOnMessageClickedListener()
                                .onMessageClicked(getTutorialTooltipId(),
                                        messageView, (View) messageView);
                    }
                }
            });
        }

        int indicatorWidth;
        if (indicatorBuilder.getWidth() == IndicatorBuilder.DEFAULT) {
            indicatorWidth = (int) ViewHelper.pxFromDp(getContext(), 50);
        } else {
            indicatorWidth = indicatorBuilder.getWidth();
        }

        int indicatorHeight;
        if (indicatorBuilder.getHeight() == IndicatorBuilder.DEFAULT) {
            indicatorHeight = (int) ViewHelper.pxFromDp(getContext(), 50);
        } else {
            indicatorHeight = indicatorBuilder.getHeight();
        }

        addView(indicatorLayout, indicatorWidth, indicatorHeight);
        addView(messageLayout, messageBuilder.getWidth(), messageBuilder.getHeight());

        if (anchorView != null && anchorView.get() != null) {
            anchorView.get().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        } else if (anchorPoint != null) {
            anchorView = null;
        } else {
            Log.e(TAG,
                    "Invalid anchorView and no anchorPoint either! You have to specify at least one!");
        }
    }

    private void updateValues() {
        setTutorialMessage(Html.fromHtml(messageBuilder.getText()));

        //        float targetDiameter = 200;
        //        circleWaveAlertView.setTargetDiameter(targetDiameter);
    }

    private void updatePositions() {
        if (anchorView != null) {
            updateIndicatorPosition(anchorView);
        } else if (anchorPoint != null) {
            updateIndicatorPosition(anchorPoint);
        } else {
            Log.e(TAG,
                    "Invalid anchorView and no anchorPoint either! You have to specify at least one!");
            return;
        }

        if (messageBuilder.getAnchorPoint() != null) {
            updateMessagePosition(messageBuilder.getAnchorPoint());
        } else if (messageBuilder.getAnchorView() != null) {
            updateMessagePosition(new WeakReference<View>(messageBuilder.getAnchorView()));
        } else {
            updateMessagePosition(new WeakReference<View>(indicatorLayout));
        }
    }

    private void updateIndicatorPosition(WeakReference<View> anchorView) {
        float x;
        float y;

        View view = anchorView.get();

        if (view == null) {
            return;
        }

        int[] position = new int[2];
        view.getLocationInWindow(position);

        View rootView = view.getRootView();
        position[0] -= rootView.getPaddingLeft();
        position[1] -= rootView.getPaddingTop();

        switch (anchorGravity) {
            case TOP:
                x = position[0] + view.getWidth() / 2 - indicatorLayout.getWidth() / 2;
                y = position[1] - indicatorLayout.getHeight() / 2;
                break;
            case BOTTOM:
                x = position[0] + view.getWidth() / 2 - indicatorLayout.getWidth() / 2;
                y = position[1] + view.getHeight() - indicatorLayout.getHeight() / 2;
                break;
            case LEFT:
                x = position[0] - indicatorLayout.getWidth() / 2;
                y = position[1] + view.getHeight() / 2 - indicatorLayout.getHeight() / 2;
                break;
            case RIGHT:
                x = position[0] + view.getWidth() - indicatorLayout.getWidth() / 2;
                y = position[1] + view.getHeight() / 2 - indicatorLayout.getHeight() / 2;
                break;
            case CENTER:
            default:
                x = position[0] + view.getWidth() / 2 - indicatorLayout.getWidth() / 2;
                y = position[1] + view.getHeight() / 2 - indicatorLayout.getHeight() / 2;
                break;
        }

        x += indicatorBuilder.getOffsetX();
        y += indicatorBuilder.getOffsetY();

        indicatorLayout.setX(x);
        indicatorLayout.setY(y);
    }

    private void updateIndicatorPosition(Point anchorPoint) {
        float x;
        float y;

        x = anchorPoint.x - indicatorLayout.getWidth() / 2 + indicatorBuilder.getOffsetX();
        y = anchorPoint.y - indicatorLayout.getHeight() / 2 + indicatorBuilder.getOffsetY();

        indicatorLayout.setX(x);
        indicatorLayout.setY(y);
    }

    private void updateMessagePosition(WeakReference<View> anchorView) {
        View view = anchorView.get();

        if (view == null) {
            return;
        }

        int[] position = new int[2];
        view.getLocationInWindow(position);

        View rootView = view.getRootView();
        position[0] -= rootView.getPaddingLeft();
        position[1] -= rootView.getPaddingTop();

        float anchorX = position[0];
        float anchorY = position[1];

        float messageX;
        float messageY;

        switch (messageBuilder.getGravity()) {
            case TOP:
                messageX = anchorX + view.getWidth() / 2 - messageLayout.getWidth() / 2;
                messageY = anchorY - messageLayout.getHeight();
                break;
            case LEFT:
                messageX = anchorX - messageLayout.getWidth();
                messageY = anchorY + view.getHeight() / 2 - messageLayout.getHeight() / 2;
                break;
            case RIGHT:
                messageX = anchorX + view.getWidth();
                messageY = anchorY + view.getHeight() / 2 - messageLayout.getHeight() / 2;
                break;
            case CENTER:
                messageX = anchorX + view.getWidth() / 2 - messageLayout.getWidth() / 2;
                messageY = anchorY + view.getHeight() / 2 - messageLayout.getHeight() / 2;
                break;
            case BOTTOM:
            default:
                messageX = anchorX + view.getWidth() / 2 - messageLayout.getWidth() / 2;
                messageY = anchorY + view.getHeight();
                break;
        }

        messageX += messageBuilder.getOffsetX();
        messageY += messageBuilder.getOffsetY();

        messageLayout.setX(messageX);
        messageLayout.setY(messageY);
    }

    private void updateMessagePosition(Point anchorPoint) {
        float messageX;
        float messageY;

        switch (messageBuilder.getGravity()) {
            case TOP:
                messageX = anchorPoint.x - messageLayout.getWidth() / 2;
                messageY = anchorPoint.y - messageLayout.getHeight();
                break;
            case LEFT:
                messageX = anchorPoint.x - messageLayout.getWidth();
                messageY = anchorPoint.y - messageLayout.getHeight() / 2;
                break;
            case RIGHT:
                messageX = anchorPoint.x;
                messageY = anchorPoint.y - messageLayout.getHeight() / 2;
                break;
            case CENTER:
                messageX = anchorPoint.x - messageLayout.getWidth() / 2;
                messageY = anchorPoint.y - messageLayout.getHeight() / 2;
                break;
            case BOTTOM:
            default:
                messageX = anchorPoint.x - messageLayout.getWidth() / 2;
                messageY = anchorPoint.y;
                break;
        }

        messageX += messageBuilder.getOffsetX();
        messageY += messageBuilder.getOffsetY();

        messageLayout.setX(messageX);
        messageLayout.setY(messageY);

//        updateMessageSize(messageX, messageY);
    }

    private void updateMessageSize(float messageX, float messageY) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        if (messageX + messageLayout.getWidth() > metrics.widthPixels) {
            messageLayout.getLayoutParams().width = metrics.widthPixels - (int) messageX;
        } else if (messageX < 0) {
            messageLayout.setX(0);
            messageLayout.getLayoutParams().width = messageLayout.getWidth() + (int) messageX;
        }

        messageLayout.requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean onTouchEvent = super.onTouchEvent(event);

//        Toast.makeText(getContext(),
//                "onTouchEvent: " + event.getX() + "," + event.getY(), Toast.LENGTH_SHORT)
//                .show();

        Log.d(TAG, "onTouchEvent: " + event.getX() + "," + event.getY());

        return onTouchEvent;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
//        Toast.makeText(getContext(),
//                "onInterceptTouchEvent: " + event.getX() + "," + event.getY(), Toast.LENGTH_SHORT)
//                .show();

        Log.d(TAG, "onInterceptTouchEvent: " + event.getX() + "," + event.getY());

        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(event);
        return onInterceptTouchEvent;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            remove();
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    private void setTutorialMessage(CharSequence charSequence) {
        messageView.setText(charSequence);
    }

    private TutorialTooltipView getTutorialTooltipView() {
        return this;
    }

    /**
     * Get the TutorialTooltip identifier for this TutorialTooltipView
     *
     * @return id
     */
    public int getTutorialTooltipId() {
        return tooltipId;
    }

    /**
     * Show this view
     */
    public void show() {
        if (getParent() == null) {
            final Activity activity = ViewHelper.getActivity(getContext());
            LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);

            switch (attachMode) {
                case Window:
                    final WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                    final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT);
                    mParams.format = PixelFormat.TRANSLUCENT;
                    mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
                    mParams.packageName = getContext().getPackageName();
                    mParams.setTitle("TutorialTooltip");
                    mParams.flags =
                            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    ;

                    if (tutorialTooltipBuilder.getOnTutorialTooltipClickedListener() == null) {
                        mParams.flags = mParams.flags
                                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
                    }
                    wm.addView(this, mParams);
                    break;
                case Dialog:
                    ViewGroup dialogRootView;
                    dialogRootView = (ViewGroup) (dialog.getWindow().getDecorView());
                    dialogRootView.addView(this, params);
                    break;
                case Activity:
                default:
                    ViewGroup rootView;
                    rootView = (ViewGroup) (activity.getWindow().getDecorView());
                    rootView.addView(this, params);
                    break;
            }
        }
    }

    /**
     * Remove this view
     */
    public void remove() {
        ViewParent parent = getParent();

        switch (attachMode) {
            case Window:
                WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                if (null != parent) {
                    wm.removeView(this);
                }
                break;
            case Activity:
            case Dialog:
            default:
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(TutorialTooltipView.this);
                }
                break;
        }
    }
}

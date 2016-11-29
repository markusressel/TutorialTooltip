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

package de.markusressel.android.library.tutorialtooltip;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

import de.markusressel.android.library.tutorialtooltip.interfaces.OnTutorialTooltipClickedListener;
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipIndicator;
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * TutorialTooltip View class
 * <p>
 * Created by Markus on 17.11.2016.
 */
public class TutorialTooltipView extends RelativeLayout {

    private static final String TAG = "TutorialTooltipView";

    private int tooltipId;

    private Dialog dialog;

    private CharSequence text;
    private Gravity anchorGravity = Gravity.CENTER;
    private WeakReference<View> anchorView;
    private Point anchorPoint;
    private int offsetX;
    private int offsetY;

    private TutorialTooltipIndicator indicatorView;
    private TutorialTooltipMessage messageView;

    private Gravity messageGravity = Gravity.BOTTOM;
    private FrameLayout indicatorLayout;
    private FrameLayout messageLayout;

    private OnTutorialTooltipClickedListener onTutorialTooltipClickedListener;

    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    updatePositions();
                }
            };

    private TutorialTooltip.Builder.AttachMode attachMode;

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

    public TutorialTooltipView(TutorialTooltip.Builder builder) {
        this(builder.context);

        getBuilderValues(builder);

        initializeViews();

        updateValues();

        getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    private void getBuilderValues(TutorialTooltip.Builder builder) {
        tooltipId = builder.id;

        attachMode = builder.attachMode;
        dialog = builder.dialog;

        text = builder.text;
        anchorGravity = builder.anchorGravity;
        if (builder.anchorView != null) {
            anchorView = new WeakReference<>(builder.anchorView);
        }
        offsetX = builder.offsetX;
        offsetY = builder.offsetY;
        anchorPoint = builder.anchorPoint;
        indicatorView = (TutorialTooltipIndicator) builder.indicatorView;
        messageGravity = builder.messageGravity;

        onTutorialTooltipClickedListener = builder.onTutorialTooltipClickedListener;
    }

    private void initializeViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        messageLayout = (FrameLayout) inflater.inflate(R.layout.layout_tutorial_text, this, false);

        if (messageView != null) {
            messageLayout.removeAllViews();
            messageLayout.addView((View) messageView, WRAP_CONTENT, WRAP_CONTENT);
        } else {
            messageView = (BasicMessageView) messageLayout.findViewById(R.id.messageView);
        }

        indicatorLayout = (FrameLayout) inflater.inflate(R.layout.layout_indicator,
                this,
                false);
        if (indicatorView != null) {
            indicatorLayout.removeAllViews();
            indicatorLayout.addView((View) indicatorView, WRAP_CONTENT, WRAP_CONTENT);
        } else {
            indicatorView = (WaveIndicatorView) indicatorLayout.findViewById(R.id.indicator);
        }

        // Set onClick listeners
        if (onTutorialTooltipClickedListener != null) {
            if (onTutorialTooltipClickedListener.indicatorIsClickable()) {
                indicatorView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onTutorialTooltipClickedListener != null) {
                            onTutorialTooltipClickedListener.onIndicatorClicked(getTutorialTooltipId(),
                                    indicatorView, (View) indicatorView);
                        }
                    }
                });
            }

            if (onTutorialTooltipClickedListener.messageIsClickable()) {
                messageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onTutorialTooltipClickedListener != null) {
                            onTutorialTooltipClickedListener.onMessageClicked(getTutorialTooltipId(),
                                    messageView, (View) messageView);
                        }
                    }
                });
            }
        }

        addView(indicatorLayout);
        addView(messageLayout, WRAP_CONTENT, WRAP_CONTENT);

        if (anchorView != null && anchorView.get() != null) {
            anchorView.get().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        } else if (anchorPoint != null) {

        } else {
            Log.e(TAG,
                    "Invalid anchorView and no anchorPoint either! You have to specify at least one!");
        }
    }

    private void updateValues() {
        setTutorialMessage(Html.fromHtml((String) text));

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
        }
    }

    private void updateIndicatorPosition(WeakReference<View> anchorView) {
        float x;
        float y;

        View view = anchorView.get();

        if (view != null) {
            int[] position = new int[2];
            view.getLocationInWindow(position);

            //rootView.setBackgroundColor(Color.parseColor("#ff0000"));
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

            x += offsetX;
            y += offsetY;

            indicatorLayout.setX(x);
            indicatorLayout.setY(y);

            updateMessagePosition(x, y);
        }
    }

    private void updateIndicatorPosition(Point anchorPoint) {
        float x;
        float y;

        x = anchorPoint.x - indicatorLayout.getWidth() / 2;
        y = anchorPoint.y - indicatorLayout.getHeight() / 2;

        indicatorLayout.setX(x);
        indicatorLayout.setY(y);

        updateMessagePosition(x, y);
    }

    private void updateMessagePosition(float indicatorX, float indicatorY) {
        float messageX;
        float messageY;

        switch (messageGravity) {
            case TOP:
                messageX = indicatorX + indicatorLayout.getWidth() / 2 - messageLayout.getWidth() / 2;
                messageY = indicatorY - messageLayout.getHeight();
                break;
            case LEFT:
                messageX = indicatorX - messageLayout.getWidth();
                messageY = indicatorY + indicatorLayout.getHeight() / 2 - messageLayout.getHeight() / 2;
                break;
            case RIGHT:
                messageX = indicatorX + indicatorLayout.getWidth();
                messageY = indicatorY + indicatorLayout.getHeight() / 2 - messageLayout.getHeight() / 2;
                break;
            case CENTER:
                messageX = indicatorX + indicatorLayout.getWidth() / 2 - messageLayout.getWidth() / 2;
                messageY = indicatorY + indicatorLayout.getHeight() / 2 - messageLayout.getHeight() / 2;
                break;
            case BOTTOM:
            default:
                messageX = indicatorX + indicatorLayout.getWidth() / 2 - messageLayout.getWidth() / 2;
                messageY = indicatorY + indicatorLayout.getHeight();
                break;
        }

        messageLayout.setX(messageX);
        messageLayout.setY(messageY);

        updateMessageSize(messageX, messageY);
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        onTouchEvent(ev);

        // ignore touch outside of indicator or message view
        return false;
    }

    private void setTutorialMessage(CharSequence charSequence) {
        messageView.setText(charSequence);
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

                    if (onTutorialTooltipClickedListener == null) {
                        mParams.flags = mParams.flags | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
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

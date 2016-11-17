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

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Simple "wave" like circle indicator view
 * <p>
 * Created by Markus on 08.11.2016.
 */
public class CircleWaveAlertView extends View {

    private static final String TAG = "CircleWaveAlertView";

    @ColorInt
    private static final int DEFAULT_COLOR = Color.BLUE;
    private static final int DEFAULT_STROKE_WIDTH = 3;
    private static final int DEFAULT_DURATION_MILLISECONDS = 3000;
    private static final int DEFAULT_WAVE_COUNT = 3;

    private float startDiameter;
    private float targetDiameter;

    @ColorInt
    private int startColor;
    @ColorInt
    private int endColor;

    private float strokeWidth;
    private int duration;
    private int delayBetweenWaves;
    private int waveCount;

    private Interpolator customInterpolator = new FastOutSlowInInterpolator();

    private float[] currentDiameters;
    private Paint[] paints;

    private ValueAnimator[] colorAnimators;
    private ValueAnimator[] sizeAnimators;
    private boolean isInitialized;


    public CircleWaveAlertView(Context context) {
        this(context, null);
    }

    public CircleWaveAlertView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleWaveAlertView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        readArguments(context, attrs);
    }

    @TargetApi(21)
    public CircleWaveAlertView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        readArguments(context, attrs);
    }

    private void readArguments(Context context, AttributeSet attrs) {
        // read XML attributes
        TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CircleWaveAlertView, 0, 0);

        try {
            int defaultColor = getAccentColor(context);

            startDiameter = a.getDimensionPixelSize(R.styleable.CircleWaveAlertView_cwav_startDiameter,
                    0);
            targetDiameter = a.getDimensionPixelSize(R.styleable.CircleWaveAlertView_cwav_targetDiameter,
                    -1);
            startColor = a.getColor(R.styleable.CircleWaveAlertView_cwav_startColor, defaultColor);

            @ColorInt
            int defaultEndColor = Color.argb(0,
                    Color.red(startColor),
                    Color.green(startColor),
                    Color.blue(startColor));
            endColor = a.getColor(R.styleable.CircleWaveAlertView_cwav_endColor, defaultEndColor);

            strokeWidth = a.getColor(R.styleable.CircleWaveAlertView_cwav_strokeWidth,
                    DEFAULT_STROKE_WIDTH);
            duration = a.getInt(R.styleable.CircleWaveAlertView_cwav_durationMilliseconds,
                    DEFAULT_DURATION_MILLISECONDS);
            delayBetweenWaves = a.getInt(R.styleable.CircleWaveAlertView_cwav_delayMillisecondsBetweenWaves,
                    -1);
            waveCount = a.getInt(R.styleable.CircleWaveAlertView_cwav_waveCount,
                    DEFAULT_WAVE_COUNT);
        } finally {
            a.recycle();
        }
    }

    private int getAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data,
                new int[]{R.attr.colorAccent});
        int color = a.getColor(0, DEFAULT_COLOR);

        a.recycle();

        return color;
    }

    private void init() {
        currentDiameters = new float[waveCount];
        paints = new Paint[waveCount];
        colorAnimators = new ValueAnimator[waveCount];
        sizeAnimators = new ValueAnimator[waveCount];

        for (int i = 0; i < waveCount; i++) {
            paints[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
            paints[i].setColor(startColor);
            paints[i].setStyle(Paint.Style.STROKE);
            paints[i].setStrokeWidth(strokeWidth);

            sizeAnimators[i] = ValueAnimator.ofFloat(startDiameter, targetDiameter);
            sizeAnimators[i].setDuration(duration);
            sizeAnimators[i].setRepeatCount(ValueAnimator.INFINITE);
            sizeAnimators[i].setRepeatMode(ValueAnimator.RESTART);
            sizeAnimators[i].setInterpolator(customInterpolator);
            final int index = i;
            sizeAnimators[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentDiameters[index] = (float) animation.getAnimatedValue();

                    // we only need to rerender the view if the first animator updates, as all animators update at the same speed
                    if (index == 0) {
                        invalidate();
                    }
                }
            });

            colorAnimators[i] = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
            colorAnimators[i].setDuration(duration);
            colorAnimators[i].setRepeatCount(ObjectAnimator.INFINITE);
            colorAnimators[i].setRepeatMode(ValueAnimator.RESTART);
            colorAnimators[i].setInterpolator(customInterpolator);
            colorAnimators[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    paints[index].setColor((int) animation.getAnimatedValue());
                }
            });
        }

        post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < waveCount; i++) {
                    int delay;
                    if (delayBetweenWaves == -1) {
                        delay = i * (duration / waveCount);
                    } else {
                        delay = i * delayBetweenWaves;
                    }
                    sizeAnimators[i].setStartDelay(delay);
                    colorAnimators[i].setStartDelay(delay);

                    sizeAnimators[i].start();
                    colorAnimators[i].start();
                }
            }
        });

        isInitialized = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int viewWidth = (int) targetDiameter + (int) (strokeWidth / 2) + this.getPaddingLeft() + this
                .getPaddingRight();
        int viewHeight = (int) targetDiameter + (int) (strokeWidth / 2) + this.getPaddingTop() + this
                .getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(viewWidth, widthSize);
        } else {
            //Be whatever you want
            width = viewWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(viewHeight, heightSize);
        } else {
            //Be whatever you want
            height = viewHeight;
        }

        setMeasuredDimension(width, height);

        if (targetDiameter == -1) {
            targetDiameter = Math.min(width, height);
        }

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float x = getWidth() / 2;
        float y = getHeight() / 2;

        for (int i = 0; i < waveCount; i++) {
            canvas.drawCircle(x, y, currentDiameters[i] / 2, paints[i]);
        }
    }


    /**
     * Get the current wave start color
     *
     * @return color as int
     */
    @ColorInt
    public int getStartColor() {
        return startColor;
    }

    /**
     * Set the start color of the waves
     *
     * @param newStartColor new color value
     */
    public void setStartColor(int newStartColor) {
        this.startColor = newStartColor;

        if (isInitialized) {
            for (int i = 0; i < waveCount; i++) {
                colorAnimators[i].setObjectValues(startColor, endColor);
            }
        }
    }

    /**
     * Get the current wave end color
     *
     * @return color as int
     */
    @ColorInt
    public int getEndColor() {
        return endColor;
    }

    /**
     * Set the end color of the waves
     *
     * @param newEndColor new color value
     */
    public void setEndColor(int newEndColor) {
        this.endColor = newEndColor;

        if (isInitialized) {
            for (int i = 0; i < waveCount; i++) {
                colorAnimators[i].setObjectValues(startColor, endColor);
            }
        }
    }

    /**
     * Get the current wave start diameter
     *
     * @return start diameter
     */
    @Dimension
    public float getStartDiameter() {
        return startDiameter;
    }

    /**
     * Set the start diameter at wich waves will spawn
     *
     * @param newStartDiameter start diameter
     */
    public void setStartDiameter(@Dimension float newStartDiameter) {
        this.startDiameter = newStartDiameter;

        if (isInitialized) {
            for (int i = 0; i < waveCount; i++) {
                sizeAnimators[i].setFloatValues(startDiameter, targetDiameter);
            }
        }
    }

    /**
     * Get the current wave target diameter
     *
     * @return target diameter
     */
    @Dimension
    public float getTargetDiameter() {
        return targetDiameter;
    }

    /**
     * Set the target diameter for the waves
     *
     * @param newTargetDiameter target diameter
     */
    public void setTargetDiameter(@Dimension float newTargetDiameter) {
        this.targetDiameter = newTargetDiameter;

        if (isInitialized) {
            for (int i = 0; i < waveCount; i++) {
                sizeAnimators[i].setFloatValues(startDiameter, targetDiameter);
            }
        }
    }

    /**
     * Get the current wave stroke width
     *
     * @return stroke width
     */
    public float getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * Set the width of the wave lines
     *
     * @param newStrokeWidth width
     */
    public void setStrokeWidth(float newStrokeWidth) {
        this.strokeWidth = newStrokeWidth;

        if (isInitialized) {
            for (int i = 0; i < waveCount; i++) {
                paints[i].setStrokeWidth(strokeWidth);
            }
        }
    }

    /**
     * Get the current duration for a wave
     *
     * @return duration in milliseconds
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Set the duration for one wave from spawn till complete fadeout
     *
     * @param newDuration duration in milliseconds
     */
    public void setDuration(int newDuration) {
        this.duration = newDuration;

        if (isInitialized) {
            for (int i = 0; i < waveCount; i++) {
                colorAnimators[i].setDuration(duration);
                sizeAnimators[i].setDuration(duration);
            }
        }
    }

    /**
     * Get the current delay between spawning new waves
     *
     * @return delay in milliseconds
     */
    public int getDelayBetweenWaves() {
        return delayBetweenWaves;
    }

    /**
     * Set the delay between spawning new waves
     * <p>
     * Calling this method will reset the view to its initial state,
     * because it has to be reinitialized completely
     *
     * @param delayBetweenWaves delay in milliseconds
     */
    public void setDelayBetweenWaves(int delayBetweenWaves) {
        this.delayBetweenWaves = delayBetweenWaves;

        init();
    }

    /**
     * Get the current amount of waves to be spawned
     *
     * @return wave amount
     */
    public int getWaveCount() {
        return waveCount;
    }

    /**
     * Set amount of waves to render
     * <p>
     * Calling this method will reset the view to its initial state,
     * because it has to be reinitialized completely
     *
     * @param waveCount amount of waves
     */
    public void setWaveCount(int waveCount) {
        this.waveCount = waveCount;

        init();
    }

    /**
     * Get the currently set interpolator
     *
     * @return interpolator
     */
    public Interpolator getCustomInterpolator() {
        return customInterpolator;
    }

    /**
     * Set a custom interpolator
     *
     * @param customInterpolator interpolator
     */
    public void setCustomInterpolator(Interpolator customInterpolator) {
        this.customInterpolator = customInterpolator;

        if (isInitialized) {
            // reinitialize
            init();
        }
    }
}

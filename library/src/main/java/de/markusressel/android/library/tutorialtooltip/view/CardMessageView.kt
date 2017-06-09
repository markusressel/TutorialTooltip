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
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView

import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage

/**
 * Basic Message view implementation
 *
 *
 * Created by Markus on 24.11.2016.
 */
class CardMessageView : FrameLayout, TutorialTooltipMessage {

    private var backgroundColor = Color.parseColor("#FFFFFFFF")
    private var borderColor = Color.parseColor("#FFFFFFFF")
    private var borderThickness = 3
    private var cornerRadius: Float = 0.toFloat()
    private var defaultPadding: Int = 0

    private var linearLayout: LinearLayout? = null
    private var textView: TextView? = null
    private var cardShape: GradientDrawable? = null

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {

        linearLayout = LinearLayout(context, attrs, defStyleAttr)
        textView = TextView(context, attrs, defStyleAttr)
        init()
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {

        linearLayout = LinearLayout(context, attrs, defStyleAttr, defStyleRes)
        textView = TextView(context, attrs, defStyleAttr, defStyleRes)

        init()
    }

    private fun init() {
        cornerRadius = ViewHelper.pxFromDp(context, 12f).toInt().toFloat()
        defaultPadding = ViewHelper.pxFromDp(context, 8f).toInt()

        textView!!.gravity = Gravity.CENTER
        textView!!.setBackgroundColor(Color.argb(0, 0, 0, 0))

        linearLayout!!.addView(textView)
        addView(linearLayout)


        cardShape = GradientDrawable()
        cardShape!!.mutate()
        cardShape!!.shape = GradientDrawable.RECTANGLE
        cardShape!!.cornerRadii = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius)
        cardShape!!.setColor(backgroundColor)
        cardShape!!.setStroke(borderThickness, borderColor)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            linearLayout!!.background = cardShape
        } else {
            linearLayout!!.setBackgroundDrawable(cardShape)
        }

        linearLayout!!.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val elevation = ViewHelper.pxFromDp(context, 6f)
            val padding = ViewHelper.pxFromDp(context, 6f).toInt()

            linearLayout!!.elevation = elevation
            linearLayout!!.clipToPadding = false

            setPadding(padding, padding, padding, padding)
            clipToPadding = false
        }
    }

    override fun setText(text: CharSequence) {
        textView!!.text = text
    }

    override fun setTextColor(@ColorInt color: Int) {
        textView!!.setTextColor(color)
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
        this.borderColor = backgroundColor
        cardShape!!.setColor(this.backgroundColor)
        cardShape!!.setStroke(borderThickness, borderColor)

        invalidate()
    }

    /**
     * Set the card border color

     * @param color color as int
     */
    fun setBorderColor(@ColorInt color: Int) {
        this.borderColor = color
        cardShape!!.setStroke(borderThickness, borderColor)

        invalidate()
    }

    /**
     * Set the card border thickness

     * @param thickness width in pixel
     */
    fun setBorderThickness(thickness: Int) {
        this.borderThickness = thickness
        cardShape!!.setStroke(borderThickness, borderColor)

        invalidate()
    }

    /**
     * Set the card corner radius

     * @param radius radius in pixel
     */
    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        cardShape!!.cornerRadii = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius)

        invalidate()
    }
}

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
import android.app.Dialog
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.text.Html
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import de.markusressel.android.library.tutorialtooltip.R
import de.markusressel.android.library.tutorialtooltip.builder.IndicatorBuilder
import de.markusressel.android.library.tutorialtooltip.builder.MessageBuilder
import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipBuilder
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipIndicator
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipMessage
import de.markusressel.android.library.tutorialtooltip.preferences.PreferencesHandler
import java.lang.ref.WeakReference


/**
 * TutorialTooltip View class
 *
 *
 * Created by Markus on 17.11.2016.
 */
class TutorialTooltipView : LinearLayout {

    /**
     * Get the TutorialTooltip tooltipId for this TutorialTooltipView

     * @return tooltipId
     */
    lateinit var tutorialTooltipId: TooltipId
        private set

    /**
     * Get the show count for this TutorialTooltipView

     * @return showCount or null
     */
    private var showCount: Int? = null

    private var dialog: Dialog? = null

    private lateinit var tutorialTooltipBuilder: TutorialTooltipBuilder
    private lateinit var indicatorBuilder: IndicatorBuilder
    private lateinit var messageBuilder: MessageBuilder

    private var anchorGravity: Gravity? = Gravity.CENTER
    private var anchorView: WeakReference<View?>? = null
    private var anchorPoint: Point? = null

    private lateinit var indicatorLayout: FrameLayout
    private lateinit var messageLayout: FrameLayout

    private var indicatorView: TutorialTooltipIndicator? = null
    private var messageView: TutorialTooltipMessage? = null

    private lateinit var attachMode: TutorialTooltipBuilder.AttachMode

    @JvmOverloads protected constructor(context: Context,
                                        attrs: AttributeSet? = null,
                                        defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    @TargetApi(21)
    protected constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int,
                          defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(tutorialTooltipBuilder: TutorialTooltipBuilder) : this(tutorialTooltipBuilder.context) {

        getBuilderValues(tutorialTooltipBuilder)

        initializeViews()

        updateValues()

        viewTreeObserver.addOnPreDrawListener {
            updateVisibility()
            if (visibility == View.VISIBLE) {
                updateSizes()
                updatePositions()
            }
            true
        }
    }

    private fun getBuilderValues(tutorialTooltipBuilder: TutorialTooltipBuilder) {
        this.tutorialTooltipBuilder = tutorialTooltipBuilder

        tutorialTooltipId = tutorialTooltipBuilder.tooltipId

        showCount = tutorialTooltipBuilder.showCount

        attachMode = tutorialTooltipBuilder.attachMode
        dialog = tutorialTooltipBuilder.dialog

        anchorGravity = tutorialTooltipBuilder.anchorGravity
        if (tutorialTooltipBuilder.anchorView != null) {
            anchorView = WeakReference(tutorialTooltipBuilder.anchorView)
        }
        anchorPoint = tutorialTooltipBuilder.anchorPoint

        indicatorBuilder = tutorialTooltipBuilder.indicatorBuilder
        when (indicatorBuilder.type) {
            IndicatorBuilder.Type.Custom -> indicatorView = indicatorBuilder.customView as TutorialTooltipIndicator
            else -> {
            }
        }

        messageBuilder = tutorialTooltipBuilder.messageBuilder
        when (messageBuilder.type) {
            MessageBuilder.Type.Custom -> messageView = messageBuilder.customView as TutorialTooltipMessage
            else -> {
            }
        }

    }

    private fun initializeViews() {
        val inflater = LayoutInflater.from(context)

        tutorialTooltipBuilder.onTutorialTooltipClickedListener?.let {
            setOnClickListener {
                tutorialTooltipBuilder.onTutorialTooltipClickedListener?.onTutorialTooltipClicked(
                        tutorialTooltipId,
                        tutorialTooltipView)
            }
        }

        // Create indicator and message views based on builder values
        initializeIndicatorView(inflater)
        initializeMessageView(inflater)

        if ((anchorView == null || anchorView!!.get() == null) && anchorPoint == null) {
            Log.e(TAG,
                    "Invalid anchorView and no anchorPoint either! You have to specify at least one!")
        }

        // fade in view after successful creation
        post { fadeIn() }
    }

    private fun initializeIndicatorView(inflater: LayoutInflater) {
        indicatorLayout = inflater.inflate(R.layout.layout_indicator,
                this,
                false) as FrameLayout

        if (indicatorView == null) {
            val indicatorViewTemp: View? = indicatorLayout.findViewById(R.id.indicator)
            indicatorView = indicatorViewTemp as TutorialTooltipIndicator
        } else {
            indicatorLayout.removeAllViews()
            indicatorLayout.addView(indicatorView as View?,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
        }

        indicatorBuilder.color?.let {
            indicatorView!!.setColor(it)
        }

        // Set onClick listeners
        indicatorBuilder.onIndicatorClickedListener?.let {
            indicatorLayout.setOnClickListener {
                indicatorBuilder.onIndicatorClickedListener?.onIndicatorClicked(tutorialTooltipId,
                        tutorialTooltipView,
                        indicatorView!!,
                        indicatorView as View)
            }
        }

        val indicatorWidth: Int = indicatorBuilder.width ?: ViewHelper.pxFromDp(context,
                50f).toInt()
        val indicatorHeight: Int = indicatorBuilder.height ?: ViewHelper.pxFromDp(context,
                50f).toInt()

        // remove reference to anchorView if an anchorPoint is specified
        anchorPoint?.let {
            anchorView = null
        }

        addView(indicatorLayout, indicatorWidth, indicatorHeight)

        // hide at the beginning to prevent flickering upon fadein
        indicatorLayout.visibility = View.INVISIBLE
    }

    private fun initializeMessageView(inflater: LayoutInflater) {
        messageLayout = inflater.inflate(R.layout.layout_tutorial_text, this, false) as FrameLayout

        messageView?.let {
            messageLayout.removeAllViews()
            messageLayout.addView(messageView as View?,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)
        } ?: let {
            val messageViewTemp: View? = messageLayout.findViewById(R.id.messageView)
            messageView = messageViewTemp as TutorialTooltipMessage
        }

        messageBuilder.textColor?.let {
            messageView!!.setTextColor(it)
        }

        messageBuilder.backgroundColor?.let {
            messageView!!.setBackgroundColor(it)
        }

        messageBuilder.onMessageClickedListener?.let {
            messageLayout.setOnClickListener {
                messageBuilder.onMessageClickedListener?.onMessageClicked(tutorialTooltipId,
                        tutorialTooltipView,
                        messageView!!,
                        messageView as View)
            }
        }

        addView(messageLayout, messageBuilder.width, messageBuilder.height)

        // hide at the beginning to prevent flickering upon fadein
        messageLayout.visibility = View.INVISIBLE
    }

    private fun updateValues() {
        setTutorialMessage(Html.fromHtml(messageBuilder.text))

        //        float targetDiameter = 200;
        //        circleWaveAlertView.setTargetDiameter(targetDiameter);
    }

    private fun updateVisibility() {
        // if an anchorView is set, update visibility based on the anchor view
        if (anchorView != null && anchorView!!.get() != null) {
            if (anchorView!!.get()!!.isShown && visibility != View.VISIBLE) {
                visibility = View.VISIBLE
                indicatorLayout.visibility = View.VISIBLE
                messageLayout.visibility = View.VISIBLE
            } else if (!anchorView!!.get()!!.isShown && visibility != View.GONE) {
                visibility = View.GONE
                indicatorLayout.visibility = View.GONE
                messageLayout.visibility = View.GONE
            }
        }
    }

    private fun updateSizes() {
        updateIndicatorSize()
    }

    private fun updateIndicatorSize() {
        val indicatorParams = indicatorLayout.layoutParams as LinearLayout.LayoutParams

        indicatorBuilder.width?.let {
            if (it == IndicatorBuilder.MATCH_ANCHOR && anchorView != null) {
                val view = anchorView!!.get()
                if (view != null) {
                    indicatorParams.width = view.width
                }
            }
        }

        indicatorBuilder.height?.let {
            if (it == IndicatorBuilder.MATCH_ANCHOR && anchorView != null) {
                val view = anchorView!!.get()
                if (view != null) {
                    indicatorParams.height = view.height
                }
            }
        }

        indicatorLayout.layoutParams = indicatorParams
    }

    private fun updatePositions() {
        if (anchorView == null && anchorPoint == null) {
            Log.e(TAG,
                    "Invalid anchorView and no anchorPoint either! You have to specify at least one!")
            return
        }

        anchorView?.let {
            updateIndicatorPosition(it)
        }

        anchorPoint?.let {
            updateIndicatorPosition(it)
        }

        updateMessagePosition(WeakReference<View?>(indicatorLayout))

        messageBuilder.anchorView?.let {
            updateMessagePosition(WeakReference(it))
        }

        messageBuilder.anchorPoint?.let {
            updateMessagePosition(it)
        }
    }

    private fun updateIndicatorPosition(anchorView: WeakReference<View?>) {
        var x = 0f
        var y = 0f

        val view = anchorView.get() ?: return

        val position = IntArray(2)
        view.getLocationInWindow(position)

        val rootView = view.rootView
        position[0] -= rootView.paddingLeft
        position[1] -= rootView.paddingTop

        when (anchorGravity) {
            TutorialTooltipView.Gravity.TOP -> {
                x = (position[0] + view.width / 2 - indicatorLayout.width / 2).toFloat()
                y = (position[1] - indicatorLayout.height / 2).toFloat()
            }
            TutorialTooltipView.Gravity.BOTTOM -> {
                x = (position[0] + view.width / 2 - indicatorLayout.width / 2).toFloat()
                y = (position[1] + view.height - indicatorLayout.height / 2).toFloat()
            }
            TutorialTooltipView.Gravity.LEFT -> {
                x = (position[0] - indicatorLayout.width / 2).toFloat()
                y = (position[1] + view.height / 2 - indicatorLayout.height / 2).toFloat()
            }
            TutorialTooltipView.Gravity.RIGHT -> {
                x = (position[0] + view.width - indicatorLayout.width / 2).toFloat()
                y = (position[1] + view.height / 2 - indicatorLayout.height / 2).toFloat()
            }
            TutorialTooltipView.Gravity.CENTER -> {
                x = (position[0] + view.width / 2 - indicatorLayout.width / 2).toFloat()
                y = (position[1] + view.height / 2 - indicatorLayout.height / 2).toFloat()
            }
        }

        x += indicatorBuilder.offsetX.toFloat()
        y += indicatorBuilder.offsetY.toFloat()

        indicatorLayout.x = x
        indicatorLayout.y = y
    }

    private fun updateIndicatorPosition(anchorPoint: Point) {
        val x: Float = (anchorPoint.x - indicatorLayout.width / 2 + indicatorBuilder.offsetX - rootView.paddingLeft).toFloat()
        val y: Float = (anchorPoint.y - indicatorLayout.height / 2 + indicatorBuilder.offsetY - rootView.paddingTop).toFloat()

        indicatorLayout.x = x
        indicatorLayout.y = y
    }

    private fun updateMessagePosition(anchorView: WeakReference<View?>?) {
        val view = anchorView!!.get() ?: return

        val position = IntArray(2)
        view.getLocationInWindow(position)

        val anchorX = position[0].toFloat()
        val anchorY = position[1].toFloat()

        var messageX: Float
        var messageY: Float

        when (messageBuilder.gravity) {
            TutorialTooltipView.Gravity.TOP -> {
                messageX = anchorX + view.width / 2 - messageLayout.width / 2
                messageY = anchorY - messageLayout.height
            }
            TutorialTooltipView.Gravity.LEFT -> {
                messageX = anchorX - messageLayout.width
                messageY = anchorY + view.height / 2 - messageLayout.height / 2
            }
            TutorialTooltipView.Gravity.RIGHT -> {
                messageX = anchorX + view.width
                messageY = anchorY + view.height / 2 - messageLayout.height / 2
            }
            TutorialTooltipView.Gravity.CENTER -> {
                messageX = anchorX + view.width / 2 - messageLayout.width / 2
                messageY = anchorY + view.height / 2 - messageLayout.height / 2
            }
            TutorialTooltipView.Gravity.BOTTOM -> {
                messageX = anchorX + view.width / 2 - messageLayout.width / 2
                messageY = anchorY + view.height
            }
        }

        messageX -= rootView.paddingLeft.toFloat()
        messageY -= rootView.paddingTop.toFloat()

        messageX += messageBuilder.offsetX.toFloat()
        messageY += messageBuilder.offsetY.toFloat()

        messageLayout.x = messageX
        messageLayout.y = messageY
    }

    private fun updateMessagePosition(anchorPoint: Point) {
        var messageX: Float
        var messageY: Float

        when (messageBuilder.gravity) {
            TutorialTooltipView.Gravity.TOP -> {
                messageX = (anchorPoint.x - messageLayout.width / 2).toFloat()
                messageY = (anchorPoint.y - messageLayout.height).toFloat()
            }
            TutorialTooltipView.Gravity.LEFT -> {
                messageX = (anchorPoint.x - messageLayout.width).toFloat()
                messageY = (anchorPoint.y - messageLayout.height / 2).toFloat()
            }
            TutorialTooltipView.Gravity.RIGHT -> {
                messageX = anchorPoint.x.toFloat()
                messageY = (anchorPoint.y - messageLayout.height / 2).toFloat()
            }
            TutorialTooltipView.Gravity.CENTER -> {
                messageX = (anchorPoint.x - messageLayout.width / 2).toFloat()
                messageY = (anchorPoint.y - messageLayout.height / 2).toFloat()
            }
            TutorialTooltipView.Gravity.BOTTOM -> {
                messageX = (anchorPoint.x - messageLayout.width / 2).toFloat()
                messageY = anchorPoint.y.toFloat()
            }
        }

        // ignore padding for calculations
        messageX -= rootView.paddingLeft.toFloat()
        messageY -= rootView.paddingTop.toFloat()

        messageX += messageBuilder.offsetX.toFloat()
        messageY += messageBuilder.offsetY.toFloat()

        messageLayout.x = messageX
        messageLayout.y = messageY

        //        updateMessageSize(messageX, messageY);
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val onTouchEvent = super.onTouchEvent(event)

        //        Toast.makeText(getContext(),
        //                "onTouchEvent: " + event.getX() + "," + event.getY(), Toast.LENGTH_SHORT)
        //                .show();

        Log.d(TAG, "onTouchEvent: " + event.x + "," + event.y)

        return onTouchEvent
    }

    //    private void updateMessageSize(float messageX, float messageY) {
    //        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    //        DisplayMetrics metrics = new DisplayMetrics();
    //        wm.getDefaultDisplay().getMetrics(metrics);
    //
    //        if (messageX + messageLayout.getWidth() > metrics.widthPixels) {
    //            messageLayout.getLayoutParams().width = metrics.widthPixels - (int) messageX;
    //        } else if (messageX < 0) {
    //            messageLayout.setX(0);
    //            messageLayout.getLayoutParams().width = messageLayout.getWidth() + (int) messageX;
    //        }
    //
    //        messageLayout.requestLayout();
    //    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        //        Toast.makeText(getContext(),
        //                "onInterceptTouchEvent: " + event.getX() + "," + event.getY(), Toast.LENGTH_SHORT)
        //                .show();

        Log.d(TAG, "onInterceptTouchEvent: " + event.x + "," + event.y)

        val onInterceptTouchEvent = super.onInterceptTouchEvent(event)
        return onInterceptTouchEvent
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            remove(true)
            return true
        }

        return super.dispatchKeyEvent(event)
    }

    private fun setTutorialMessage(text: CharSequence) {
        messageView!!.setText(text)
    }

    private val tutorialTooltipView: TutorialTooltipView
        get() = this

    /**
     * Show this view
     */
    fun show() {
        if (showCount != null) {
            val preferencesHandler = PreferencesHandler(context)
            if (preferencesHandler.getCount(this) >= showCount!!) {
                Log.w(TAG, "showCount reached, TutorialTooltip will not be shown")
                return
            }
        }

        if (parent == null) {
            val activity = ViewHelper.getActivity(context)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT)

            when (attachMode) {
                TutorialTooltipBuilder.AttachMode.Window -> {
                    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    val mParams = WindowManager.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT)
                    mParams.format = PixelFormat.TRANSLUCENT
                    mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION
                    mParams.packageName = context.packageName
                    mParams.title = "TutorialTooltip"
                    mParams.flags = activity!!.window.attributes.flags or
                            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH

                    if (tutorialTooltipBuilder.onTutorialTooltipClickedListener == null) {
                        mParams.flags = mParams.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    }
                    wm.addView(this, mParams)
                }
                TutorialTooltipBuilder.AttachMode.Dialog -> {
                    val dialogRootView: ViewGroup = dialog!!.window!!.decorView as ViewGroup
                    dialogRootView.addView(this, params)
                }
                TutorialTooltipBuilder.AttachMode.Activity -> {
                    val rootView: ViewGroup = activity!!.window.decorView as ViewGroup
                    rootView.addView(this, params)
                }
            }
        }
    }

    /**
     * Remove this view

     * @param animated true fades out, false removes immediately
     */
    fun remove(animated: Boolean) {
        // prevent calling onClick when fading out
        setOnClickListener(null)

        tutorialTooltipBuilder.onTutorialTooltipRemovedListener?.onRemove(tutorialTooltipId, this)


        if (animated) {
            fadeOut()
        } else {
            removeFromParent()
        }

        val preferencesHandler = PreferencesHandler(context)
        if (showCount != null && preferencesHandler.getCount(this) < showCount!!) {
            preferencesHandler.increaseCount(this)
        }

        tutorialTooltipBuilder.onTutorialTooltipRemovedListener?.postRemove(tutorialTooltipId, this)
    }

    private fun fadeIn() {
        // fade out animation duration
        val animationDuration = 225

        val animationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                indicatorLayout.visibility = View.VISIBLE
                messageLayout.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}
        }

        // animation set for the indicator
        val indicatorAnimationSet = AnimationSet(true) // true means shared interpolators
        indicatorAnimationSet.interpolator = DecelerateInterpolator()
        indicatorAnimationSet.duration = animationDuration.toLong()
        indicatorAnimationSet.fillAfter = true
        indicatorAnimationSet.setAnimationListener(animationListener)

        val alphaAnimation = AlphaAnimation(0f, 1f)

        indicatorAnimationSet.addAnimation(alphaAnimation)

        // animation set for the message view
        val messageAnimationSet = AnimationSet(true)
        messageAnimationSet.interpolator = DecelerateInterpolator()
        messageAnimationSet.duration = animationDuration.toLong()
        messageAnimationSet.fillAfter = true

        // move message view slightly to the bottom in addition to the fade out
        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                -0.05f,
                Animation.RELATIVE_TO_SELF,
                0.00f)

        messageAnimationSet.addAnimation(alphaAnimation)
        messageAnimationSet.addAnimation(translateAnimation)

        // start animations
        indicatorLayout.startAnimation(indicatorAnimationSet)
        messageLayout.startAnimation(messageAnimationSet)
    }

    private fun fadeOut() {
        // fade out animation duration
        val animationDuration = 195

        val animationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // workaround to prevent flickering at animation end
                // afaik setFillAfter(true) should fix this, but sadly it doesn't
                indicatorLayout.visibility = ViewGroup.INVISIBLE
                messageLayout.visibility = ViewGroup.INVISIBLE

                post { removeFromParent() }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        }

        // animation set for the indicator
        val indicatorAnimationSet = AnimationSet(true) // true means shared interpolators
        indicatorAnimationSet.interpolator = DecelerateInterpolator()
        indicatorAnimationSet.duration = animationDuration.toLong()
        indicatorAnimationSet.fillAfter = true
        indicatorAnimationSet.setAnimationListener(animationListener)

        val alphaAnimation = AlphaAnimation(1f, 0f)

        indicatorAnimationSet.addAnimation(alphaAnimation)

        // animation set for the message view
        val messageAnimationSet = AnimationSet(true)
        messageAnimationSet.interpolator = DecelerateInterpolator()
        messageAnimationSet.duration = animationDuration.toLong()
        messageAnimationSet.fillAfter = true

        // move message view slightly to the bottom in addition to the fade out
        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.05f)

        messageAnimationSet.addAnimation(alphaAnimation)
        messageAnimationSet.addAnimation(translateAnimation)

        // start animations
        indicatorLayout.startAnimation(indicatorAnimationSet)
        messageLayout.startAnimation(messageAnimationSet)
    }

    private fun removeFromParent() {
        val parent = parent

        when (attachMode) {
            TutorialTooltipBuilder.AttachMode.Window -> {
                val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                if (parent != null) {
                    wm.removeView(this)
                }
            }
            TutorialTooltipBuilder.AttachMode.Activity,
            TutorialTooltipBuilder.AttachMode.Dialog ->
                if (parent is ViewGroup) {
                    parent.removeView(this@TutorialTooltipView)
                }
        }
    }

    enum class Gravity {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        CENTER
    }

    companion object {

        private val TAG = "TutorialTooltipView"
    }
}

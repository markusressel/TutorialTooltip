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

package de.markusressel.android.tutorialtooltip

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import de.markusressel.android.library.tutorialtooltip.TutorialTooltip
import de.markusressel.android.library.tutorialtooltip.builder.IndicatorConfiguration
import de.markusressel.android.library.tutorialtooltip.builder.MessageConfiguration
import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipBuilder
import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipChainBuilder
import de.markusressel.android.library.tutorialtooltip.interfaces.OnTutorialTooltipClickedListener
import de.markusressel.android.library.tutorialtooltip.view.CardMessageView
import de.markusressel.android.library.tutorialtooltip.view.TooltipId
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView
import de.markusressel.android.library.tutorialtooltip.view.WaveIndicatorView
import kotlinx.android.synthetic.main.activity_test.*

class TouchActivity : AppCompatActivity() {

    private var tutorialId1: TooltipId = TooltipId("")
    private var tutorialId2: TooltipId = TooltipId("")
    private var tutorialId3: TooltipId = TooltipId("")

    private var tutorialTooltipView: TutorialTooltipView? = null
    private var tutorialId4: TooltipId = TooltipId("")
    private lateinit var onTutorialTooltipClickedListener: OnTutorialTooltipClickedListener

    private fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val animator = ValueAnimator.ofFloat(0f, 200f)
        animator.duration = 2000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.addUpdateListener { animation -> buttonHideLayout.x = animation.animatedValue as Float }

        buttonShowLayout.setOnClickListener {
            linear_layout_right_bottom.visibility = View.VISIBLE
            animator.start()
        }
        buttonHideLayout.setOnClickListener {
            linear_layout_right_bottom.visibility = View.INVISIBLE
            animator.end()
        }
        animator.start()

        TutorialTooltip.show(TutorialTooltipBuilder(this).anchor(buttonHideLayout).build())

        val activity = this

        onTutorialTooltipClickedListener = object : OnTutorialTooltipClickedListener {
            override fun onTutorialTooltipClicked(id: TooltipId,
                                                  tutorialTooltipView: TutorialTooltipView) {
                TutorialTooltip.remove(activity, id, true)
            }
        }

        buttonCount.setOnClickListener {
            TutorialTooltip.show(
                    TutorialTooltipBuilder(activity)
                            .anchor(buttonCount, TutorialTooltipView.Gravity.TOP)
                            .onClick(onTutorialTooltipClickedListener)
                            .indicator(IndicatorConfiguration(
                                    onIndicatorClicked = { id, tutorialTooltipView, indicator, indicatorView ->
                                        Toast.makeText(applicationContext,
                                                "Indicator " + id + " " + indicatorView.width + " clicked!",
                                                Toast.LENGTH_SHORT).show()
                                    }
                            ))
                            .showCount("button_count", 3)
                            .build())
        }

        buttonTop.setOnClickListener {
            if (TutorialTooltip.exists(activity, tutorialId1)) {
                TutorialTooltip.remove(activity, tutorialId1, true)
            } else {
                val waveIndicatorView = WaveIndicatorView(activity)
                waveIndicatorView.startColor = Color.argb(255, 255, 0, 0)
                waveIndicatorView.endColor = Color.argb(0, 255, 0, 0)
                waveIndicatorView.strokeWidth = pxFromDp(activity, 5f)
                //                    waveIndicatorView.setTargetDiameter(pxFromDp(activity, 50));

                tutorialId1 = TutorialTooltip.show(
                        TutorialTooltipBuilder(activity)
                                .anchor(buttonTop, TutorialTooltipView.Gravity.TOP)
                                .indicator(IndicatorConfiguration(
                                        customView = waveIndicatorView,
                                        offsetX = 50,
                                        offsetY = 50,
                                        width = 300,
                                        height = 300,
                                        onIndicatorClicked = { id, tutorialTooltipView, indicator, indicatorView ->
                                            Toast.makeText(applicationContext,
                                                    "Indicator " + id + " " + indicatorView.width + " clicked!",
                                                    Toast.LENGTH_SHORT).show()
                                        }


                                ))
                                .message(MessageConfiguration(
                                        text = getString(R.string.tutorial_message_1),
                                        gravity = TutorialTooltipView.Gravity.TOP,
                                        onMessageClicked = { id, tutorialTooltipView, message, messageView ->
                                            Toast.makeText(applicationContext,
                                                    "Message " + id + " " + messageView.width + " clicked!",
                                                    Toast.LENGTH_SHORT).show()
                                        }))
                                .onClick(onTutorialTooltipClickedListener)
                                .build())
            }
        }

        buttonCenter.setOnClickListener {
            if (TutorialTooltip.exists(activity, tutorialId4)) {
                TutorialTooltip.remove(activity, tutorialId4, true)
            } else {
                tutorialId4 = TutorialTooltip.show(
                        TutorialTooltipBuilder(activity)
                                .message(MessageConfiguration(
                                        text = getString(R.string.tutorial_message_3),
                                        gravity = TutorialTooltipView.Gravity.RIGHT,
                                        width = pxFromDp(applicationContext, 150f).toInt(),
                                        height = MessageConfiguration.WRAP_CONTENT))
                                .anchor(buttonCenter)
                                .onClick(onTutorialTooltipClickedListener)
                                .build())
            }
        }

        buttonBottom.setOnClickListener {
            if (TutorialTooltip.exists(activity, tutorialId2)) {
                TutorialTooltip.remove(activity, tutorialId2, true)
            } else {
                tutorialId2 = TutorialTooltip.show(
                        TutorialTooltipBuilder(activity)
                                .indicator(IndicatorConfiguration(
                                        color = Color.WHITE
                                ))
                                .message(MessageConfiguration(
                                        text = getString(R.string.tutorial_message_2),
                                        gravity = TutorialTooltipView.Gravity.BOTTOM,
                                        anchorView = buttonDialog))
                                .anchor(buttonBottom, TutorialTooltipView.Gravity.BOTTOM)
                                .onClick(onTutorialTooltipClickedListener)
                                .build())
            }
        }

        buttonFab.setOnClickListener {
            if (tutorialTooltipView != null && tutorialTooltipView!!.isShown) {
                tutorialTooltipView?.remove(true)
            } else {
                val waveIndicatorView = WaveIndicatorView(activity)
                waveIndicatorView.startColor = Color.argb(255, 255, 255, 255)
                waveIndicatorView.endColor = Color.argb(0, 255, 255, 255)
                waveIndicatorView.strokeWidth = pxFromDp(activity, 5f)
                waveIndicatorView.targetDiameter = pxFromDp(activity, 50f)

                tutorialTooltipView = TutorialTooltip.make(
                        TutorialTooltipBuilder(activity)
                                .indicator(IndicatorConfiguration(
                                        customView = waveIndicatorView,
                                        onIndicatorClicked = { id, tutorialTooltipView, indicator, indicatorView ->
                                            Toast.makeText(applicationContext,
                                                    "Indicator " + id + " " + indicatorView.width + " clicked!",
                                                    Toast.LENGTH_SHORT).show()
                                        }
                                ))
                                .message(MessageConfiguration(
                                        customView = CardMessageView(activity),
                                        text = getString(R.string.tutorial_message_fab),
                                        gravity = TutorialTooltipView.Gravity.BOTTOM,
                                        backgroundColor = Color.BLACK,
                                        textColor = Color.WHITE))
                                .anchor(buttonFab)
                                .attachToWindow()
                                .onClick(object : OnTutorialTooltipClickedListener {
                                    override fun onTutorialTooltipClicked(id: TooltipId,
                                                                          tutorialTooltipView: TutorialTooltipView) {
                                        tutorialTooltipView.remove(true)
                                    }
                                })
                                .build())

                tutorialId3 = TutorialTooltip.show(tutorialTooltipView!!)
            }
        }

        buttonClearAll.setOnClickListener {
            TutorialTooltip.removeAll(activity, true)
            TutorialTooltip.resetAllShowCount(applicationContext)
        }

        buttonDialog.setOnClickListener {
            val dialogFragmentTest = DialogFragmentTest.newInstance()
            dialogFragmentTest.show(supportFragmentManager, null)
        }

        buttonChain.setOnClickListener {
            val tutorialTooltipChainBuilder = TutorialTooltipChainBuilder()

            val anchorViews = arrayOf<View>(buttonTop,
                    buttonCenter,
                    buttonBottom,
                    buttonFab,
                    buttonDialog,
                    buttonChain,
                    buttonClearAll)

            for (i in anchorViews.indices) {
                tutorialTooltipChainBuilder.addItem(TutorialTooltipBuilder(activity)
                        .anchor(anchorViews[i])
                        .message(MessageConfiguration(
                                text = "${i + 1}/${anchorViews.size}: Message"))
                        .onClick(onTutorialTooltipClickedListener)
                        .build())
            }

            tutorialTooltipChainBuilder.execute()
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
        // When user touches the screen
            MotionEvent.ACTION_UP -> {
                // Getting X coordinate
                val x = event.x
                // Getting Y Coordinate
                val y = event.y

                createTutorialTooltip(x, y)
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    /**
     * Creates a TutorialTooltip where the user taps (if no other view consumes the touch

     * @param x x coordinate of touch
     * *
     * @param y y coordinate of touch
     */
    private fun createTutorialTooltip(x: Float, y: Float) {
        TutorialTooltip.show(
                TutorialTooltipBuilder(this)
                        .anchor(Point(x.toInt(), y.toInt()))
                        .onClick(onTutorialTooltipClickedListener)
                        .message(MessageConfiguration(
                                text = "You touched right here!",
                                backgroundColor = Color.parseColor("#FFF3E0"),
                                width = resources.getDimension(R.dimen.messageWidth).toInt(),
                                height = MessageConfiguration.WRAP_CONTENT))
                        .build())
    }
}

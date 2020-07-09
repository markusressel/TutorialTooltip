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
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.markusressel.android.library.tutorialtooltip.TutorialTooltip
import de.markusressel.android.library.tutorialtooltip.builder.IndicatorConfiguration
import de.markusressel.android.library.tutorialtooltip.builder.MessageConfiguration
import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipBuilder
import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipChainBuilder
import de.markusressel.android.library.tutorialtooltip.view.CardMessageView
import de.markusressel.android.library.tutorialtooltip.view.TooltipId
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView
import de.markusressel.android.library.tutorialtooltip.view.WaveIndicatorView
import kotlinx.android.synthetic.main.activity_test.*

class TouchActivity : AppCompatActivity() {

    private var tutorialId1: TooltipId = TooltipId()
    private var tutorialId2: TooltipId = TooltipId()
    private var tutorialId3: TooltipId = TooltipId()

    private var tutorialTooltipView: TutorialTooltipView? = null
    private var tutorialId4: TooltipId = TooltipId()

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
        animator.addUpdateListener { animation ->
            buttonHideLayout.x = animation.animatedValue as Float
        }

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

        val listener: ((id: TooltipId, tutorialTooltipView: TutorialTooltipView) -> Unit) = { id, view ->
            TutorialTooltip.remove(activity, id, true)
        }

        buttonCount.setOnClickListener {
            TutorialTooltip.show(
                    TutorialTooltipBuilder(
                            context = activity,
                            onClick = listener,
                            indicatorConfiguration = IndicatorConfiguration(
                                    onClick = { id, tutorialTooltipView, indicator, indicatorView ->
                                        Toast.makeText(applicationContext,
                                                "Indicator " + id + " " + indicatorView.width + " clicked!",
                                                Toast.LENGTH_SHORT).show()
                                    }))
                            .anchor(buttonCount, TutorialTooltipView.Gravity.TOP)
                            .showCount("button_count", 3)
                            .build())
        }

        buttonTop.setOnClickListener {
            if (TutorialTooltip.exists(activity, tutorialId1)) {
                TutorialTooltip.remove(activity, tutorialId1, true)
            } else {
                val waveIndicatorView = WaveIndicatorView(activity).apply {
                    startColor = Color.argb(255, 255, 0, 0)
                    endColor = Color.argb(0, 255, 0, 0)
                    strokeWidth = pxFromDp(activity, 5f)
                }

                tutorialId1 = TutorialTooltip.show(
                        TutorialTooltipBuilder(
                                context = activity,
                                indicatorConfiguration = IndicatorConfiguration(
                                        customView = waveIndicatorView,
                                        offsetX = 50,
                                        offsetY = 50,
                                        width = 300,
                                        height = 300,
                                        onClick = { id, tutorialTooltipView, indicator, indicatorView ->
                                            Toast.makeText(applicationContext,
                                                    "Indicator " + id + " " + indicatorView.width + " clicked!",
                                                    Toast.LENGTH_SHORT).show()
                                        }),
                                messageConfiguration = MessageConfiguration(
                                        text = getString(R.string.tutorial_message_1),
                                        gravity = TutorialTooltipView.Gravity.TOP,
                                        onClick = { id, tutorialTooltipView, message, messageView ->
                                            Toast.makeText(applicationContext,
                                                    "Message " + id + " " + messageView.width + " clicked!",
                                                    Toast.LENGTH_SHORT).show()
                                        }),
                                onClick = listener)
                                .anchor(buttonTop, TutorialTooltipView.Gravity.TOP)
                                .build())
            }
        }

        buttonCenter.setOnClickListener {
            if (TutorialTooltip.exists(activity, tutorialId4)) {
                TutorialTooltip.remove(activity, tutorialId4, true)
            } else {
                tutorialId4 = TutorialTooltip.show(
                        TutorialTooltipBuilder(
                                context = activity,
                                messageConfiguration = MessageConfiguration(
                                        text = getString(R.string.tutorial_message_3),
                                        gravity = TutorialTooltipView.Gravity.RIGHT,
                                        width = pxFromDp(applicationContext, 150f).toInt(),
                                        height = MessageConfiguration.WRAP_CONTENT),
                                onClick = listener)
                                .anchor(buttonCenter)
                                .build())
            }
        }

        buttonBottom.setOnClickListener {
            if (TutorialTooltip.exists(activity, tutorialId2)) {
                TutorialTooltip.remove(activity, tutorialId2, true)
            } else {
                tutorialId2 = TutorialTooltip.show(
                        TutorialTooltipBuilder(
                                context = activity,
                                indicatorConfiguration = IndicatorConfiguration(
                                        color = Color.WHITE
                                ),
                                messageConfiguration = MessageConfiguration(
                                        text = getString(R.string.tutorial_message_2),
                                        gravity = TutorialTooltipView.Gravity.BOTTOM,
                                        anchorView = buttonDialog),
                                onClick = listener)
                                .anchor(buttonBottom, TutorialTooltipView.Gravity.BOTTOM)
                                .build()
                )
            }
        }

        buttonFab.setOnClickListener {
            if (tutorialTooltipView != null && tutorialTooltipView!!.isShown) {
                tutorialTooltipView?.remove(true)
            } else {
                val waveIndicatorView = WaveIndicatorView(activity).apply {
                    startColor = Color.argb(255, 255, 255, 255)
                    endColor = Color.argb(0, 255, 255, 255)
                    strokeWidth = pxFromDp(activity, 5f)
                    endDiameter = pxFromDp(activity, 50f)
                }

                tutorialTooltipView = TutorialTooltip.make(
                        TutorialTooltipBuilder(
                                context = activity,
                                indicatorConfiguration = IndicatorConfiguration(
                                        customView = waveIndicatorView,
                                        onClick = { id, tutorialTooltipView, indicator, indicatorView ->
                                            Toast.makeText(applicationContext,
                                                    "Indicator " + id + " " + indicatorView.width + " clicked!",
                                                    Toast.LENGTH_SHORT).show()
                                        }
                                ),
                                messageConfiguration = MessageConfiguration(
                                        customView = CardMessageView(activity),
                                        text = getString(R.string.tutorial_message_fab),
                                        gravity = TutorialTooltipView.Gravity.BOTTOM,
                                        backgroundColor = Color.BLACK,
                                        textColor = Color.WHITE),
                                onClick = { id, view ->
                                    view.remove(true)
                                })
                                .anchor(buttonFab)
                                .attachToWindow()
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
                tutorialTooltipChainBuilder.addItem(
                        TutorialTooltipBuilder(
                                context = activity,
                                messageConfiguration = MessageConfiguration(
                                        text = "${i + 1}/${anchorViews.size}: Message"),
                                onClick = listener)
                                .anchor(anchorViews[i])
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
        val activity = this

        TutorialTooltip.show(
                TutorialTooltipBuilder(
                        context = this,
                        onClick = { id, view ->
                            TutorialTooltip.remove(activity, id, true)
                        },
                        messageConfiguration = MessageConfiguration(
                                text = "You touched right here!",
                                backgroundColor = Color.parseColor("#FFF3E0"),
                                width = resources.getDimension(R.dimen.messageWidth).toInt(),
                                height = MessageConfiguration.WRAP_CONTENT),
                        onPostRemove = { id, view ->
                            Log.d("Test", "Test")
                        })
                        .anchor(Point(x.toInt(), y.toInt()))
                        .build())
    }
}

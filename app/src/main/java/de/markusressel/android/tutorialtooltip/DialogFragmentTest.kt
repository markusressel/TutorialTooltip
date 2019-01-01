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

import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.core.app.DialogFragment
import android.view.*
import de.markusressel.android.library.tutorialtooltip.TutorialTooltip
import de.markusressel.android.library.tutorialtooltip.builder.IndicatorConfiguration
import de.markusressel.android.library.tutorialtooltip.builder.MessageConfiguration
import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipBuilder
import de.markusressel.android.library.tutorialtooltip.view.CardMessageView
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView
import de.markusressel.android.library.tutorialtooltip.view.WaveIndicatorView

/**
 * Dialog for testing purpose
 *
 *
 * Created by Markus on 28.11.2016.
 */
class DialogFragmentTest : DialogFragment() {

    lateinit var testButton: View
    lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.dialog_test, container)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme)

        testButton = rootView.findViewById(R.id.testButton)
        testButton.setOnClickListener { showTutorialTooltip() }

        return rootView
    }

    private fun showTutorialTooltip() {
        // custom message view
        val cardMessageView = CardMessageView(activity!!)
        @ColorInt val transparentWhite = Color.argb(255, 255, 255, 255)
        cardMessageView.setBorderColor(Color.BLACK)

        // custom indicator view
        val waveIndicatorView = WaveIndicatorView(activity!!)
        waveIndicatorView.strokeWidth = 10f // customization that is not included in the IndicatorBuilder

        val tutorialTooltipBuilder = TutorialTooltipBuilder(
                context = activity!!,
                messageConfiguration = MessageConfiguration(
                        customView = cardMessageView,
                        text = "This is a tutorial message!\nNow with two lines!",
                        textColor = Color.BLACK,
                        backgroundColor = Color.WHITE,
                        gravity = TutorialTooltipView.Gravity.TOP,
                        onClick = { id, tutorialTooltipView, message, messageView ->
                            // this will intercept touches only for the message view
                            // if you don't want the main OnTutorialTooltipClickedListener listener
                            // to react to touches here just specify an empty OnMessageClickedListener

                            TutorialTooltip.remove(dialog, id, true)
                        },
                        height = MessageConfiguration.WRAP_CONTENT,
                        width = MessageConfiguration.WRAP_CONTENT),
                indicatorConfiguration = IndicatorConfiguration(
                        customView = waveIndicatorView,
                        height = IndicatorConfiguration.MATCH_ANCHOR,
                        width = IndicatorConfiguration.MATCH_ANCHOR,
                        color = Color.BLUE,
                        onClick = { id, tutorialTooltipView, message, messageView ->
                            // this will intercept touches only for the indicator view
                            // if you don't want the main OnTutorialTooltipClickedListener listener
                            // to react to touches here just specify an empty OnIndicatorClickedListener

                            TutorialTooltip.remove(dialog, id, true)
                        }),
                onClick = { id, view ->
                    // this will intercept touches of the complete window
                    // if you don't specify additional listeners for the indicator or
                    // message view they will be included

                    TutorialTooltip.remove(dialog, id, true)
                })
                .anchor(testButton)
                .attachToDialog(dialog)
                .build()

        TutorialTooltip.show(tutorialTooltipBuilder)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // ask to really close
        val dialog = object : Dialog(activity) {
            override fun onTouchEvent(event: MotionEvent): Boolean {
                when (event.action) {
                // When user touches the screen
                    MotionEvent.ACTION_UP -> {
                        // Getting X coordinate
                        val x = event.x
                        // Getting Y Coordinate
                        val y = event.y

                        // calculate dialog view bounds
                        val r = Rect(rootView.paddingLeft,
                                rootView.paddingTop,
                                rootView.width + rootView.paddingLeft,
                                rootView.height + rootView.paddingTop)

                        // check if the tapped position is within the bounds of the dialog
                        if (r.contains(x.toInt(), y.toInt())) {
                            createTutorialTooltip(x, y)
                        }

                        return true
                    }
                }

                return super.onTouchEvent(event)
            }
        }
        dialog.setTitle("Dialog Test")
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        //        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        //        lp.copyFrom(dialog.getWindow().getAttributes());
        ////        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        ////        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //        dialog.show();
        //        dialog.getWindow().setAttributes(lp);

        dialog.show()

        return dialog
    }

    /**
     * Creates a TutorialTooltip where the user taps (if no other view consumes the touch

     * @param x x coordinate of touch
     * *
     * @param y y coordinate of touch
     */
    private fun createTutorialTooltip(x: Float, y: Float) {
        val messageGravity: TutorialTooltipView.Gravity

        val borderSize = 200

        val leftBorder = borderSize
        val rightBorder = dialog.window!!.decorView.width - borderSize
        val topBorder = borderSize
        val bottomBorder = dialog.window!!.decorView.height - borderSize

        if (x > rightBorder) {
            messageGravity = TutorialTooltipView.Gravity.LEFT
        } else if (x < leftBorder) {
            messageGravity = TutorialTooltipView.Gravity.RIGHT
        } else if (y < topBorder) {
            messageGravity = TutorialTooltipView.Gravity.BOTTOM
        } else if (y > bottomBorder) {
            messageGravity = TutorialTooltipView.Gravity.TOP
        } else {
            messageGravity = TutorialTooltipView.Gravity.CENTER
        }


        TutorialTooltip.show(
                TutorialTooltipBuilder(
                        context = activity!!,
                        onClick = { id, view ->
                            view.remove(true)
                        },
                        messageConfiguration = MessageConfiguration(
                                text = "You touched the dialog right here!",
                                width = 200,
                                height = MessageConfiguration.WRAP_CONTENT,
                                gravity = messageGravity)
                ).anchor(Point(x.toInt(), y.toInt()))
                        .attachToDialog(dialog)
                        .build())
    }

    companion object {

        fun newInstance(): DialogFragmentTest {
            val fragment = DialogFragmentTest()
            fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme)
            //        fragment.setTargetFragment(targetFragment, 0);
            return fragment
        }
    }
}
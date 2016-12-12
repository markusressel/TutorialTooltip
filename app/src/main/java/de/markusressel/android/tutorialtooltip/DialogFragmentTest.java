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

package de.markusressel.android.tutorialtooltip;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import de.markusressel.android.library.tutorialtooltip.TutorialTooltip;
import de.markusressel.android.library.tutorialtooltip.builder.MessageBuilder;
import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipBuilder;
import de.markusressel.android.library.tutorialtooltip.interfaces.OnTutorialTooltipClickedListener;
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView;

/**
 * Dialog for testing purpose
 * <p>
 * Created by Markus on 28.11.2016.
 */
public class DialogFragmentTest extends DialogFragment {

    private Button button;

    public static DialogFragmentTest newInstance() {
        DialogFragmentTest fragment = new DialogFragmentTest();
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
//        fragment.setTargetFragment(targetFragment, 0);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_test, container);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);

        button = (Button) rootView.findViewById(R.id.testbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTutorialTooltip();
            }
        });

        return rootView;
    }

    private void showTutorialTooltip() {
        TutorialTooltip.show(new TutorialTooltipBuilder(getActivity())
                .message(new MessageBuilder()
                        .text("This is a dialog test message!\nhttp://power-switch.eu")
                        .gravity(TutorialTooltipView.Gravity.TOP)
                        .build())
                .anchor(button)
                .attachToDialog(getDialog())
                .onClick(new OnTutorialTooltipClickedListener() {
                    @Override
                    public void onTutorialTooltipClicked(int id,
                            TutorialTooltipView tutorialTooltipView) {
                        tutorialTooltipView.remove();
                    }
                })
                .build()
        );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // ask to really close
        final Dialog dialog = new Dialog(getActivity()) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                switch (event.getAction()) {
                    // When user touches the screen
                    case MotionEvent.ACTION_DOWN:
                        // Getting X coordinate
                        float x = event.getX();
                        // Getting Y Coordinate
                        float y = event.getY();

                        createTutorialTooltip(x, y);
                        return true;
                }

                return super.onTouchEvent(event);
            }
        };
        dialog.setTitle("Dialog Test");
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
////        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
////        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialog.show();
//        dialog.getWindow().setAttributes(lp);

        dialog.show();

        return dialog;
    }

    /**
     * Creates a TutorialTooltip where the user taps (if no other view consumes the touch
     *
     * @param x x coordinate of touch
     * @param y y coordinate of touch
     */
    private void createTutorialTooltip(float x, float y) {
        TutorialTooltipView.Gravity messageGravity;

        int borderSize = 200;

        int leftBorder = borderSize;
        int rightBorder = getDialog().getWindow().getDecorView().getWidth() - borderSize;
        int topBorder = borderSize;
        int bottomBorder = getDialog().getWindow().getDecorView().getHeight() - borderSize;

        if (x > rightBorder) {
            messageGravity = TutorialTooltipView.Gravity.LEFT;
        } else if (x < leftBorder) {
            messageGravity = TutorialTooltipView.Gravity.RIGHT;
        } else if (y < topBorder) {
            messageGravity = TutorialTooltipView.Gravity.BOTTOM;
        } else if (y > bottomBorder) {
            messageGravity = TutorialTooltipView.Gravity.TOP;
        } else {
            messageGravity = TutorialTooltipView.Gravity.CENTER;
        }


        TutorialTooltip.show(
                new TutorialTooltipBuilder(getActivity())
                        .anchor(new Point((int) x, (int) y))
                        .attachToDialog(getDialog())
                        .onClick(new OnTutorialTooltipClickedListener() {
                            @Override
                            public void onTutorialTooltipClicked(int id,
                                    TutorialTooltipView tutorialTooltipView) {
                                tutorialTooltipView.remove();
                            }
                        })
                        .message(new MessageBuilder()
                                .gravity(messageGravity)
                                .size(200, MessageBuilder.WRAP_CONTENT)
                                .text("You touched the dialog right here!")
                                .build())
                        .build());
    }
}
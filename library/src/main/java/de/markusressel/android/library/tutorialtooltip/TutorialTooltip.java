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
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;

import de.markusressel.android.library.tutorialtooltip.TutorialTooltipView.Gravity;
import de.markusressel.android.library.tutorialtooltip.interfaces.OnTutorialTooltipClickedListener;
import de.markusressel.android.library.tutorialtooltip.interfaces.TutorialTooltipIndicator;

/**
 * Base TutorialTooltip class
 * <p>
 * Contains Builder and basic construction methods
 * <p>
 * Created by Markus on 17.11.2016.
 */
public class TutorialTooltip {

    private static final String TAG = "TutorialTooltip";

    /**
     * Create the TutorialTooltip
     *
     * @param builder TutorialTooltip.Builder
     * @return view
     */
    public static TutorialTooltipView make(Builder builder) {
        return new TutorialTooltipView(builder);
    }

    /**
     * Create a TutorialTooltip and show it right away
     *
     * @param builder TutorialTooltip.Builder
     */
    public static int show(Builder builder) {
        TutorialTooltipView tutorialTooltipView = make(builder);
        return show(tutorialTooltipView);
    }

    /**
     * Create a TutorialTooltip and show it right away
     *
     * @param tutorialTooltipView TutorialTooltipView
     */
    public static int show(TutorialTooltipView tutorialTooltipView) {
        tutorialTooltipView.show();
        return tutorialTooltipView.getTutorialTooltipId();
    }

    /**
     * Searches through the view tree for instances of TutorialTooltipView
     * <p>
     * This only works if the TutorialTooltip was attached to the Activity and NOT to the Window!
     *
     * @param context activity context
     * @param id      id of TutorialTooltip
     * @return true if a TutorialTooltip with the given id exists, false otherwise
     */
    public static boolean exists(Context context, int id) {
        final Activity act = ViewHelper.getActivity(context);
        if (act != null) {
            ViewGroup rootView = (ViewGroup) (act.getWindow().getDecorView());
            for (int i = 0; i < rootView.getChildCount(); i++) {
                final View child = rootView.getChildAt(i);
                if (child instanceof TutorialTooltipView) {
                    TutorialTooltipView tutorialTooltipView = (TutorialTooltipView) child;
                    if (tutorialTooltipView.getTutorialTooltipId() == id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Remove an existing TutorialTooltip via its ID
     * <p>
     * This only works if the TutorialTooltip was attached to the Activity and NOT to the Window!
     *
     * @param context activity context the specified tooltip was added to, application context will not work!
     * @param id      id of TutorialTooltip
     * @return true if a TutorialTooltip was found and removed, false otherwise
     */
    public static boolean remove(Context context, int id) {
        final Activity act = ViewHelper.getActivity(context);
        if (act != null) {
            ViewGroup rootView = (ViewGroup) (act.getWindow().getDecorView());
            for (int i = 0; i < rootView.getChildCount(); i++) {
                final View child = rootView.getChildAt(i);
                if (child instanceof TutorialTooltipView) {
                    TutorialTooltipView tutorialTooltipView = (TutorialTooltipView) child;

                    if (tutorialTooltipView.getTutorialTooltipId() == id) {
                        tutorialTooltipView.remove();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Remove an existing TutorialTooltip
     *
     * @param tutorialTooltipView TutorialTooltipView to remove
     */
    public static void remove(TutorialTooltipView tutorialTooltipView) {
        tutorialTooltipView.remove();
    }

    /**
     * Remove all existing TutorialTooltips from activity
     * <p>
     * This does not remove TutorialTooltips that are attached to the window!
     *
     * @param context activity context the specified tooltip was added to, application context will not work!
     */
    public static void removeAll(Context context) {
        final Activity act = ViewHelper.getActivity(context);
        if (act != null) {
            ViewGroup rootView = (ViewGroup) (act.getWindow().getDecorView());
            for (int i = 0; i < rootView.getChildCount(); i++) {
                final View child = rootView.getChildAt(i);
                if (child instanceof TutorialTooltipView) {
                    TutorialTooltipView tutorialTooltipView = (TutorialTooltipView) child;
                    tutorialTooltipView.remove();

                    // a view was removed fron the parent so the child list is now one element smaller
                    // to prevent skipping one element in the list the index is kept the same for the next loop (decremented and incremented)
                    i--;
                }
            }
        }
    }

    /**
     * Use this Builder to create a TutorialTooltip
     */
    public static final class Builder {

        Context context;

        private static int lastId = 0;

        int id;

        String text;

        View anchorView;

        Gravity anchorGravity;

        Gravity messageGravity;

        Point anchorPoint;

        View indicatorView;

        OnTutorialTooltipClickedListener onTutorialTooltipClickedListener;

        boolean attachToWindow;

        private boolean completed;

//        public Builder(int id) {
//            this.id = id;
//        }

        /**
         * Constructor for the builder.
         * Chain methods and call ".build()" as your last step to make this object immutable.
         *
         * @param context activity context that the TutorialTooltip will be added to. Application context will not suffice!
         */
        public Builder(Context context) {
            this.context = context;
            this.id = ++lastId;
        }

        /**
         * Specify whether the TutorialTooltip should be attached to the Window or the activity.
         * <p>
         * This can be handy if you want to show TutorialTooltips above all other content, like in FragmentDialogs.
         *
         * @param attachToWindow true, if attached to the Window,
         *                       false, if attached to the activity
         * @return Builder
         */
        public Builder attachToWindow(boolean attachToWindow) {
            this.attachToWindow = attachToWindow;
            return this;
        }

        /**
         * Set the anchor for the TutorialTooltip
         *
         * @param view    view which will be used as an anchor
         * @param gravity position relative to the anchor view which the indicator will point to
         * @return Builder
         */
        public Builder anchor(View view, Gravity gravity) {
            isCompleted();
            this.anchorPoint = null;
            this.anchorView = view;
            this.anchorGravity = gravity;
            return this;
        }

        /**
         * Set the anchor point for the TutorialTooltip
         *
         * @param point position where the indicator will be located at
         * @return Builder
         */
        public Builder anchor(final Point point) {
            isCompleted();
            this.anchorView = null;
            this.anchorPoint = point;
            return this;
        }

        /**
         * Set the tutorial text
         *
         * @param text    message
         * @param gravity positioning of the text relative to the indicator view
         * @return Builder
         */
        public Builder text(String text, final Gravity gravity) {
            isCompleted();
            this.text = text;
            this.messageGravity = gravity;
            return this;
        }

        /**
         * Set a custom indicator view
         * <p>
         * To build your own indicator view, just create a new class and extend <code>TutorialTooltipIndicator</code>
         *
         * @param view indicator view
         * @return Builder
         */
        public <T extends View & TutorialTooltipIndicator> Builder customIndicator(T view) {
            isCompleted();
            this.indicatorView = view;
            return this;
        }

        /**
         * Set an OnClick listener for the TutorialTooltip
         *
         * @param onTutorialTooltipClickedListener
         * @return Builder
         */
        public Builder onClickListener(
                OnTutorialTooltipClickedListener onTutorialTooltipClickedListener) {
            isCompleted();
            this.onTutorialTooltipClickedListener = onTutorialTooltipClickedListener;
            return this;
        }

        /**
         * Checks if this Builder was already build and therefore cant be modified anymore
         */
        private void isCompleted() {
            if (completed) {
                throw new IllegalStateException("Builder was already build!");
            }
        }

        /**
         * Complete the build process
         *
         * @return Builder
         */
        public Builder build() {
            isCompleted();

            this.completed = true;
            return this;
        }
    }

}

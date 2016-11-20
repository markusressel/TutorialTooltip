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

/**
 * Base TutorialTooltip class
 * <p>
 * Contains Builder and basic construction methods
 * <p>
 * Created by Markus on 17.11.2016.
 */
public class TutorialTooltip {

    /**
     * Create the TutorialTooltip
     *
     * @param context activity context
     * @param builder TutorialTooltip.Builder
     * @return view
     */
    public static TutorialTooltipView make(Context context, Builder builder) {
        return new TutorialTooltipView(context, builder);
    }

    /**
     * Create a TutorialTooltip and show it right away
     *
     * @param context activity context that the TutorialTooltip will be added to. Application context will not suffice!
     * @param builder TutorialTooltip.Builder
     */
    public static int show(Context context, Builder builder) {
        TutorialTooltipView tutorialTooltipView = make(context, builder);
        tutorialTooltipView.show();
        return tutorialTooltipView.getTutorialTooltipId();
    }

    /**
     * Remove an existing TutorialTooltip
     *
     * @param context activity context the specified tooltip was added to, application context will not work!
     * @param id      id of TutorialTooltip
     * @return true if a TutorialTooltip was found and removed, false otherwise
     */
    public static boolean remove(Context context, int id) {
        final Activity act = ViewHelper.getActivity(context);
        if (act != null) {
            ViewGroup rootView;
            rootView = (ViewGroup) (act.getWindow().getDecorView());
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
     * Use this Builder to create a TutorialTooltip
     */
    public static final class Builder {
        private static int lastId = 0;

        int id;

        String text;

        View anchorView;

        Gravity anchorGravity;

        Gravity messageGravity;

        Point anchorPoint;

        View indicatorView;

        private boolean completed;

        public Builder(int id) {
            this.id = id;
        }

        public Builder() {
            this.id = lastId++;
        }

        public Builder anchor(View view, Gravity gravity) {
            isCompleted();
            this.anchorPoint = null;
            this.anchorView = view;
            this.anchorGravity = gravity;
            return this;
        }

        public Builder anchor(final Point point) {
            isCompleted();
            this.anchorView = null;
            this.anchorPoint = point;
            return this;
        }

        /**
         * Set the tutorial text
         *
         * @param text
         * @return
         */
        public Builder text(String text, final Gravity gravity) {
            isCompleted();
            this.text = text;
            this.messageGravity = gravity;
            return this;
        }

        /**
         * Set a custom indicator view
         *
         * @param view indicator view
         * @return
         */
        public Builder indicator(View view) {
            isCompleted();
            this.indicatorView = view;
            return this;
        }

        private void isCompleted() {
            if (completed) {
                throw new IllegalStateException("Builder was already build!");
            }
        }

        /**
         * Complete the build process
         *
         * @return
         */
        public Builder build() {
            isCompleted();

            this.completed = true;
            return this;
        }
    }

}
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
import android.view.View;
import android.view.ViewGroup;

import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipBuilder;
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView;
import de.markusressel.android.library.tutorialtooltip.view.ViewHelper;

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
     * @param builder TutorialTooltipBuilder
     * @return TutorialTooltipView
     */
    public static TutorialTooltipView make(TutorialTooltipBuilder builder) {
        if (!builder.isCompleted()) {
            throw new IllegalStateException("Builder is not complete! Did you call build()?");
        }
        return new TutorialTooltipView(builder);
    }

    /**
     * Create a TutorialTooltip and show it right away
     *
     * @param builder TutorialTooltipBuilder
     * @return ID of TutorialTooltip
     */
    public static int show(TutorialTooltipBuilder builder) {
        TutorialTooltipView tutorialTooltipView = make(builder);
        return show(tutorialTooltipView);
    }

    /**
     * Show a TutorialTooltip
     *
     * @param tutorialTooltipView TutorialTooltipView
     * @return ID of TutorialTooltip
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

}

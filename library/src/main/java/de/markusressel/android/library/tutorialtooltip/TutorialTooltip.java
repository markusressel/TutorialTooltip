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
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.markusressel.android.library.tutorialtooltip.builder.TutorialTooltipBuilder;
import de.markusressel.android.library.tutorialtooltip.preferences.PreferencesHandler;
import de.markusressel.android.library.tutorialtooltip.view.TooltipId;
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public static TooltipId show(TutorialTooltipBuilder builder) {
        TutorialTooltipView tutorialTooltipView = make(builder);
        return show(tutorialTooltipView);
    }

    /**
     * Show a TutorialTooltip
     *
     * @param tutorialTooltipView TutorialTooltipView
     * @return ID of TutorialTooltip
     */
    @SuppressWarnings("unused")
    public static TooltipId show(TutorialTooltipView tutorialTooltipView) {
        tutorialTooltipView.show();
        return tutorialTooltipView.getTutorialTooltipId();
    }

    /**
     * Searches through the view tree for instances of TutorialTooltipView
     * <p>
     * WARNING: This only works if the TutorialTooltip was attached to the Activity and NOT to the Window!
     * If you attach the TutorialTooltip to the window you have to keep a reference to its view manually.
     *
     * @param context activity context
     * @param id      id of TutorialTooltip
     * @return true if a TutorialTooltip with the given id exists, false otherwise
     */
    @SuppressWarnings("unused")
    public static boolean exists(Context context, TooltipId id) {
        final Activity act = ViewHelper.getActivity(context);
        if (act != null) {
            ViewGroup rootView = (ViewGroup) (act.getWindow().getDecorView());
            for (int i = 0; i < rootView.getChildCount(); i++) {
                final View child = rootView.getChildAt(i);
                if (child instanceof TutorialTooltipView) {
                    TutorialTooltipView tutorialTooltipView = (TutorialTooltipView) child;
                    if (tutorialTooltipView.getTutorialTooltipId().equals(id)) {
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
     * WARNING: This only works if the TutorialTooltip was attached to the Activity and NOT to the Window!
     * If you attach the TutorialTooltip to the window you have to keep a reference to its view manually.
     *
     * @param context  activity context the specified tooltip was added to, application context will not work!
     * @param id       id of TutorialTooltip
     * @param animated true fades out, false removes immediately
     * @return true if a TutorialTooltip was found and removed, false otherwise
     */
    @SuppressWarnings("unused")
    public static boolean remove(Context context, TooltipId id, boolean animated) {
        final Activity act = ViewHelper.getActivity(context);
        if (act != null) {
            ViewGroup rootView = (ViewGroup) (act.getWindow().getDecorView());
            return removeChild(id, rootView, animated);
        }

        return false;
    }

    /**
     * Remove an existing TutorialTooltip via its ID
     * <p>
     * WARNING: This only works if the TutorialTooltip was attached to a Dialog and NOT to the Window or Activity!
     * If you attach the TutorialTooltip to the activity use <code>remove(Context context, TooltipId id)</code>
     * If you attach it to the window you have to keep a reference to its view and remove it manually.
     *
     * @param dialog   dialog the specified tooltip was added to
     * @param id       id of TutorialTooltip
     * @param animated true fades out, false removes immediately
     * @return true if a TutorialTooltip was found and removed, false otherwise
     */
    @SuppressWarnings("unused")
    public static boolean remove(Dialog dialog, TooltipId id, boolean animated) {
        if (dialog != null) {
            ViewGroup rootView = (ViewGroup) (dialog.getWindow().getDecorView());
            return removeChild(id, rootView, animated);
        }

        return false;
    }

    /**
     * This method traverses through the view tree horizontally
     * and searches for a TutorialTooltipView with the given ID
     * until a matching view is found and removes it.
     *
     * @param id       ID of the TutorialTooltip
     * @param parent   parent ViewGroup
     * @param animated true fades out, false removes immediately
     * @return true if a TutorialTooltip was found and removed, false otherwise
     */
    private static boolean removeChild(TooltipId id, ViewGroup parent, boolean animated) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (child instanceof TutorialTooltipView) {
                TutorialTooltipView tutorialTooltipView = (TutorialTooltipView) child;

                if (tutorialTooltipView.getTutorialTooltipId().equals(id)) {
                    tutorialTooltipView.remove(animated);
                    return true;
                }
            }
        }

        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                if (removeChild(id, (ViewGroup) child, animated)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Remove an existing TutorialTooltip
     *
     * @param tutorialTooltipView TutorialTooltipView to remove
     * @param animated            true fades out, false removes immediately
     */
    @SuppressWarnings("unused")
    public static void remove(TutorialTooltipView tutorialTooltipView, boolean animated) {
        tutorialTooltipView.remove(animated);
    }

    /**
     * Remove all existing TutorialTooltips from activity
     * <p>
     * WARNING: This does not remove TutorialTooltips that are attached to the window!
     * If you attach the TutorialTooltip to the window you have to keep a reference to its view manually.
     *
     * @param context  activity context the specified tooltip was added to, application context will not work!
     * @param animated true fades out, false removes immediately
     */
    @SuppressWarnings("unused")
    public static void removeAll(Context context, boolean animated) {
        final Activity act = ViewHelper.getActivity(context);
        if (act != null) {
            ViewGroup rootView = (ViewGroup) (act.getWindow().getDecorView());

            List<Integer> childsToRemove = new LinkedList<>();

            for (int i = 0; i < rootView.getChildCount(); i++) {
                final View child = rootView.getChildAt(i);
                if (child instanceof TutorialTooltipView) {
                    TutorialTooltipView tutorialTooltipView = (TutorialTooltipView) child;
                    childsToRemove.add(i);
                }
            }

            Collections.sort(childsToRemove, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o2.compareTo(o1);
                }
            });

            for (Integer childIndex : childsToRemove) {
                TutorialTooltipView tutorialTooltipView =
                        (TutorialTooltipView) rootView.getChildAt(childIndex);
                tutorialTooltipView.remove(animated);
            }
        }
    }

    /**
     * Reset the show count for a TutorialTooltip ID
     *
     * @param applicationContext application context (for access to SharedPreferences)
     * @param tooltipId          the TutorialTooltip ID to reset the count for
     */
    @SuppressWarnings("unused")
    public static void resetShowCount(@NonNull Context applicationContext,
            @NonNull TooltipId tooltipId) {
        PreferencesHandler preferencesHandler = new PreferencesHandler(applicationContext);
        preferencesHandler.resetCount(tooltipId);
    }

    /**
     * Reset the show count for a TutorialTooltip
     *
     * @param tutorialTooltipView the TutorialTooltip to reset the count for
     */
    @SuppressWarnings("unused")
    public static void resetShowCount(@NonNull TutorialTooltipView tutorialTooltipView) {
        PreferencesHandler preferencesHandler = new PreferencesHandler(tutorialTooltipView.getContext());
        preferencesHandler.resetCount(tutorialTooltipView);
    }

    /**
     * Reset the show count for ALL TutorialTooltips
     *
     * @param context application context
     */
    @SuppressWarnings("unused")
    public static void resetAllShowCount(@NonNull Context context) {
        PreferencesHandler preferencesHandler = new PreferencesHandler(context);
        preferencesHandler.clearAll();
    }

}

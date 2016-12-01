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

package de.markusressel.android.library.tutorialtooltip.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;

/**
 * Helper class for easy access to views
 * <p>
 * Created by Markus on 17.11.2016.
 */
public class ViewHelper {

    @Nullable
    public static Activity getActivity(@Nullable Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}

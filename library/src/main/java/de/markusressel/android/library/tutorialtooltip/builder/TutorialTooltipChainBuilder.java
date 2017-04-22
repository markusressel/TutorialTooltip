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

package de.markusressel.android.library.tutorialtooltip.builder;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.markusressel.android.library.tutorialtooltip.TutorialTooltip;
import de.markusressel.android.library.tutorialtooltip.interfaces.OnTutorialTooltipRemovedListener;
import de.markusressel.android.library.tutorialtooltip.view.TooltipId;
import de.markusressel.android.library.tutorialtooltip.view.TutorialTooltipView;

/**
 * Class to easily build a chain of TutorialTooltips
 * <p>
 * Add items to the chain using <code>addItem(TutorialTooltipBuilder)</code>.
 * Start the chain using <code>execute()</code>.
 * <p>
 * To proceed from one item in the chain to the next you have to close (remove) its ancestor TutorialTooltip.
 * The next item will then show up automatically.
 * <p>
 * Created by Markus on 23.12.2016.
 */
public final class TutorialTooltipChainBuilder extends Builder<TutorialTooltipChainBuilder> {

    private static final String TAG = "ChainBuilder";

    private List<TutorialTooltipBuilder> tooltips;

    /**
     * Constructor for the Builder
     * Chain methods and call ".build()" as your last step to make this object immutable.
     */
    public TutorialTooltipChainBuilder() {
        tooltips = new ArrayList<>();
    }

    /**
     * Add a TutorialTooltip to the chain
     *
     * @param tutorialTooltipBuilder TutorialTooltipBuilder
     * @return TutorialTooltipChainBuilder
     */
    public TutorialTooltipChainBuilder addItem(TutorialTooltipBuilder tutorialTooltipBuilder) {
        throwIfCompleted();
        tooltips.add(tutorialTooltipBuilder);
        return this;
    }

    /**
     * Execute the chain and show TutorialItems one after another
     */
    public void execute() {
        if (tooltips.isEmpty()) {
            Log.w(TAG, "Empty chain. Nothing to do.");
            return;
        }

        if (!isCompleted()) {
            build();
        }

        for (int i = 0; i < tooltips.size(); i++) {
            final int currentIndex = i;
            TutorialTooltipBuilder tooltipBuilder = tooltips.get(i);

            tooltipBuilder.onRemoved(new OnTutorialTooltipRemovedListener() {
                @Override
                public void onRemove(TooltipId id, TutorialTooltipView tutorialTooltipView) {

                }

                @Override
                public void postRemove(TooltipId id, TutorialTooltipView tutorialTooltipView) {
                    // open the next TutorialTooltip in the chain (if one exists)
                    if (currentIndex >= 0 && currentIndex < tooltips.size() - 1) {
                        TutorialTooltip.show(tooltips.get(currentIndex + 1));
                    }
                }
            });
        }

        // show the first TutorialTooltip
        TutorialTooltip.show(tooltips.get(0));
    }

}

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

package de.markusressel.android.library.tutorialtooltip.builder

import android.util.Log
import de.markusressel.android.library.tutorialtooltip.TutorialTooltip
import java.util.*

/**
 * Class to easily build a chain of TutorialTooltips
 *
 *
 * Add items to the chain using `addItem(TutorialTooltipBuilder)`.
 * Start the chain using `execute()`.
 *
 *
 * To proceed from one item in the chain to the next you have to close (remove) its ancestor TutorialTooltip.
 * The next item will then show up automatically.
 *
 *
 * Created by Markus on 23.12.2016.
 */
class TutorialTooltipChainBuilder : Builder<TutorialTooltipChainBuilder>() {

    private val tooltips: MutableList<TutorialTooltipBuilder>

    /**
     * Constructor for the Builder
     * Chain methods and call ".build()" as your last step to make this object immutable.
     */
    init {
        tooltips = ArrayList()
    }

    /**
     * Add a TutorialTooltip to the chain

     * @param tutorialTooltipBuilder TutorialTooltipBuilder
     * *
     * @return TutorialTooltipChainBuilder
     */
    fun addItem(tutorialTooltipBuilder: TutorialTooltipBuilder): TutorialTooltipChainBuilder {
        throwIfCompleted()
        tooltips.add(tutorialTooltipBuilder)
        return this
    }

    /**
     * Execute the chain and show TutorialItems one after another
     */
    fun execute() {
        if (tooltips.isEmpty()) {
            Log.w(TAG, "Empty chain. Nothing to do.")
            return
        }

        if (!isCompleted) {
            build()
        }

        for (currentIndex in tooltips.indices) {
            val tooltipBuilder = tooltips[currentIndex]

            val userListener = tooltipBuilder.onPostRemove

            tooltipBuilder.onPostRemove = { id, view ->
                // call user defined listener
                userListener?.invoke(id, view)

                // open the next TutorialTooltip in the chain (if one exists)
                if (currentIndex >= 0 && currentIndex < tooltips.size - 1) {
                    TutorialTooltip.show(tooltips[currentIndex + 1])
                }
            }
        }

        // show the first TutorialTooltip
        TutorialTooltip.show(tooltips[0])
    }

    companion object {
        private const val TAG = "ChainBuilder"
    }

}

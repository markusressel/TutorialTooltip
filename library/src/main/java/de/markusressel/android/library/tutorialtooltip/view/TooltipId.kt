/*
 * Copyright (c) 2017 Markus Ressel
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

package de.markusressel.android.library.tutorialtooltip.view

/**
 * ID of a TutorialTooltip
 *
 *
 * Created by Markus on 22.04.2017.
 */
class TooltipId {

    private var identifier: String? = null

    /**
     * Create a new random ID
     */
    constructor() {
        identifier = "TooltipID-" + lastId++
    }

    /**
     * Create an ID from the given parameter

     * @param identifier the string to use as ID
     */
    constructor(identifier: String) {
        this.identifier = identifier
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is TooltipId -> identifier == other.identifier
            is String -> identifier == other
            else -> super.equals(other)
        }
    }

    override fun toString(): String {
        return identifier.toString()
    }

    override fun hashCode(): Int {
        return identifier?.hashCode() ?: 0
    }

    companion object {
        private var lastId = 1
    }
}

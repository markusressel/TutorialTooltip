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

import lombok.Getter;

/**
 * Base class for builders
 * <p>
 * Use T to pass in the class that extends this Builder.
 * f.ex.: <code>MessageBuilder extends Builder<MessageBuilder></code>
 * <p>
 * Created by Markus on 01.12.2016.
 */
abstract class Builder<T> {

    /**
     * true if the Builder has been built, false otherwise
     */
    @Getter
    private boolean completed;

    /**
     * Checks if this Builder was already build and therefore cant be modified anymore
     */
    void throwIfCompleted() {
        if (completed) {
            throw new IllegalStateException("Builder was already built!");
        }
    }

    /**
     * Complete the build process
     * <p>
     *
     * @return T The extending Builder class
     */
    public T build() {
        throwIfCompleted();
        this.completed = true;

        return (T) this;
    }

}

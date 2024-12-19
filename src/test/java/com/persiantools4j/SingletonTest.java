/*
 * Copyright 2024 Imorate (Amir Mohammad Hl) <dev.imorate@gmail.com> and contributors.
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

package com.persiantools4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class SingletonTest<T> {

    protected abstract T getSingletonInstance();

    @Nested
    @DisplayName("Get instance")
    class GetInstanceTest {

        @Test
        @DisplayName("Non thread-safe")
        void getInstanceTest() {
            T firstInstance = getSingletonInstance();
            assertThat(firstInstance).isNotNull();
            T secondInstance = getSingletonInstance();
            assertThat(secondInstance).isNotNull();
            assertThat(firstInstance).isSameAs(secondInstance);
        }

        @Test
        @DisplayName("Thread-safe")
        @SuppressWarnings("unchecked")
        void getInstanceThreadSafeTest() throws InterruptedException {
            T[] instances = (T[]) new Object[2];
            Thread firstThread = new Thread(() -> instances[0] = getSingletonInstance());
            Thread secondThread = new Thread(() -> instances[1] = getSingletonInstance());
            firstThread.start();
            secondThread.start();
            firstThread.join();
            secondThread.join();
            assertThat(instances[0]).isNotNull();
            assertThat(instances[1]).isNotNull();
            assertThat(instances[0]).isSameAs(instances[1]);
        }

    }

}

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

package com.persiantools4j.module.nationalid;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Hometown collection")
class HometownCollectionTest {

    @Test
    @DisplayName("Get populated hometown list")
    void testPopulatedHometownList() {
        List<Hometown> hometownList = HometownCollection.getInstance().getCollection();
        Pattern hometownCodePattern = Pattern.compile("\\d{3}");
        Hometown expectedContainingHometown = new Hometown("تهران مرکزی", "تهران",
                Arrays.asList("001", "002", "003", "004", "005", "006", "007", "008"));
        assertThat(hometownList)
                .isNotNull()
                .contains(expectedContainingHometown)
                .allSatisfy(hometown -> {
                    assertThat(hometown.getCode())
                            .isNotEmpty()
                            .allMatch(code -> hometownCodePattern.matcher(code).matches());
                    assertThat(hometown.getCity()).isNotBlank();
                    assertThat(hometown.getProvince()).isNotBlank();
                });
    }

    @Nested
    @DisplayName("Get instance")
    class GetInstanceTest {

        @Test
        @DisplayName("Non-thread-safe")
        void testGetInstance() {
            HometownCollection firstHometownCollection = HometownCollection.getInstance();
            assertThat(firstHometownCollection).isNotNull();
            HometownCollection secondHometownCollection = HometownCollection.getInstance();
            assertThat(secondHometownCollection).isNotNull();
            assertThat(firstHometownCollection).isSameAs(secondHometownCollection);
        }

        @Test
        @DisplayName("Thread-safe")
        void testGetInstanceThreadSafe() throws InterruptedException {
            HometownCollection[] hometownCollections = new HometownCollection[2];
            Thread firstThread = new Thread(() -> hometownCollections[0] = HometownCollection.getInstance());
            Thread secondThread = new Thread(() -> hometownCollections[1] = HometownCollection.getInstance());
            firstThread.start();
            secondThread.start();
            firstThread.join();
            secondThread.join();
            assertThat(hometownCollections[0]).isNotNull();
            assertThat(hometownCollections[1]).isNotNull();
            assertThat(hometownCollections[0]).isSameAs(hometownCollections[1]);
        }

    }

}

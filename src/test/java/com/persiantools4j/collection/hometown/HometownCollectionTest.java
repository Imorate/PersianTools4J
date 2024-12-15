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

package com.persiantools4j.collection.hometown;

import com.persiantools4j.collection.Collection;
import com.persiantools4j.collection.CollectionTest;
import com.persiantools4j.enums.RegexCharacterClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Hometown collection")
class HometownCollectionTest extends CollectionTest<Hometown> {

    @Override
    protected Collection<Hometown> getTestInstance() {
        return HometownCollection.getInstance();
    }

    @Test
    @DisplayName("Get populated hometown list")
    void testPopulatedHometownList() {
        Hometown testHometown = new Hometown("تهران مرکزی", "تهران",
                Arrays.asList("001", "002", "003", "004", "005", "006", "007", "008"));
        assertThat(getTestInstance().findAllBy(hometown -> true))
                .isNotNull()
                .isNotEmpty()
                .contains(testHometown)
                .allSatisfy(hometown -> {
                    assertThat(hometown.getCode())
                            .isNotNull()
                            .isNotEmpty()
                            .allMatch(code -> code.matches("\\d{3}"));
                    assertThat(hometown.getCity())
                            .isNotBlank()
                            .matches(persianName -> persianName.matches("[" +
                                    RegexCharacterClass.PERSIAN_ALPHABET.getClassStr() + "()0-9\\s]+"));
                    assertThat(hometown.getProvince())
                            .isNotBlank()
                            .matches(persianName -> persianName.matches("[" +
                                    RegexCharacterClass.PERSIAN_ALPHABET.getClassStr() + "\\s]+"));
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

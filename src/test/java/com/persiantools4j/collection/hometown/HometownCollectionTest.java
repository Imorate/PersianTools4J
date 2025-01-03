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
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Hometown collection")
class HometownCollectionTest extends CollectionTest<HometownCollection, Hometown> {

    @Override
    protected HometownCollection getSingletonInstance() {
        return HometownCollection.getInstance();
    }

    @Override
    protected Collection<Hometown> getTestCollectionInstance() {
        return HometownCollection.getInstance();
    }

    @Test
    @DisplayName("Get populated collection")
    void populatedHometownListTest() {
        Hometown expectedHometown = new Hometown("تهران", "تهران مرکزی",
                Arrays.asList("001", "002", "003", "004", "005", "006", "007", "008"));
        assertThat(getTestCollectionInstance().findAllBy(hometown -> true))
                .isNotNull()
                .isNotEmpty()
                .contains(expectedHometown)
                .allSatisfy(hometown -> {
                    assertThat(hometown.getCodes())
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

}

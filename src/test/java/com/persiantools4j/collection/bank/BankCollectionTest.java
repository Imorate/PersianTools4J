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

package com.persiantools4j.collection.bank;

import com.persiantools4j.collection.Collection;
import com.persiantools4j.collection.CollectionTest;
import com.persiantools4j.enums.RegexCharacterClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Bank collection")
class BankCollectionTest extends CollectionTest<BankCollection, Bank> {

    @Override
    protected BankCollection getSingletonInstance() {
        return BankCollection.getInstance();
    }

    @Override
    protected Collection<Bank> getTestCollectionInstance() {
        return BankCollection.getInstance();
    }

    @Test
    @DisplayName("Get populated collection")
    void populatedBankListTest() {
        Bank testBank = new Bank("mellat", "Mellat Bank", "بانک ملت",
                Collections.singletonList("012"), Arrays.asList("610433", "991975"));
        assertThat(getTestCollectionInstance().findAllBy(bank -> true))
                .isNotNull()
                .isNotEmpty()
                .contains(testBank)
                .allSatisfy(bank -> {
                    assertThat(bank.getCodes())
                            .isNotNull()
                            .isNotEmpty()
                            .allMatch(code -> code.matches("0\\d{2}"));
                    assertThat(bank.getId())
                            .isNotBlank()
                            .matches(id -> id.matches("[a-z-]+"));
                    assertThat(bank.getName())
                            .isNotBlank()
                            .matches(name -> name.matches("[a-zA-Z-\\s]+"));
                    assertThat(bank.getPersianName())
                            .isNotBlank()
                            .matches(persianName -> persianName.matches("[" +
                                    RegexCharacterClass.PERSIAN_ALPHABET.getClassStr() + "\\s]+"));
                })
                .anySatisfy(bank -> assertThat(bank.getBins())
                        .isNotNull()
                        .isNotEmpty()
                        .allMatch(bin -> bin.matches("\\d{6}")));
    }

}

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

package com.persiantools4j.bank;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Bank collection")
class BankCollectionTest {

    private static BankCollection bankCollection;

    @BeforeAll
    static void beforeAll() {
        bankCollection = BankCollection.getInstance();
    }

    @Test
    @DisplayName("Get populated bank list test")
    void testHometownPopulatedList() {
        List<Bank> bankList = bankCollection.getCollection();
        Pattern bankCodePattern = Pattern.compile("0\\d{2}");
        AssertionsForInterfaceTypes.assertThat(bankList)
                .isNotNull()
                .contains(
                        Bank.of("mellat", "Mellat Bank", "بانک ملت",
                                Collections.singletonList("012"), Arrays.asList(610433, 991975))
                ).allSatisfy(bank -> {
                    AssertionsForInterfaceTypes.assertThat(bank.getCodes())
                            .isNotEmpty()
                            .allMatch(code -> bankCodePattern.matcher(code).matches());
                    assertThat(bank.getId()).isNotBlank();
                    assertThat(bank.getName()).isNotBlank();
                    assertThat(bank.getPersianName()).isNotBlank();
                });
    }

    @Nested
    @DisplayName("Get instance tests")
    class GetInstanceTests {

        @Test
        @DisplayName("Non-thread-safe test")
        void testGetInstance() {
            BankCollection firstBankCollection = BankCollection.getInstance();
            assertThat(firstBankCollection).isNotNull();
            BankCollection secondBankCollection = BankCollection.getInstance();
            assertThat(secondBankCollection).isNotNull();
            assertThat(firstBankCollection).isSameAs(secondBankCollection);
        }

        @Test
        @DisplayName("Thread-safe test")
        void testGetInstanceThreadSafe() throws InterruptedException {
            BankCollection[] bankCollections = new BankCollection[2];
            Thread firstThread = new Thread(() -> bankCollections[0] = BankCollection.getInstance());
            Thread secondThread = new Thread(() -> bankCollections[1] = BankCollection.getInstance());
            firstThread.start();
            secondThread.start();
            firstThread.join();
            secondThread.join();
            assertThat(bankCollections[0]).isNotNull();
            assertThat(bankCollections[1]).isNotNull();
            assertThat(bankCollections[0]).isSameAs(bankCollections[1]);
        }

    }

}

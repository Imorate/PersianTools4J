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

package com.persiantools4j.objectmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persiantools4j.utils.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Jackson object mapper wrapper")
class ObjectMapperWrapperTest {

    private static ObjectMapper objectMapper;

    private static Stream<Arguments> deserializationCases() {
        String testStr = "اين يك تست كاربردي مي باشد";
        String dontNeedToNormalizeStr = "متن بدون نیاز به تبدیل به فارسی";
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", ""),
                Arguments.of(testStr, StringUtils.normalizePersian(testStr)),
                Arguments.of(dontNeedToNormalizeStr, dontNeedToNormalizeStr)
        );
    }

    @BeforeAll
    static void beforeAll() {
        objectMapper = ObjectMapperWrapper.getInstance();
    }

    @Test
    @DisplayName("Module existence")
    void testModuleExistence() {
        assertThat(ObjectMapperWrapper.getInstance().getRegisteredModuleIds())
                .hasSize(1)
                .containsOnly("Persian-Module");
    }

    @ParameterizedTest
    @DisplayName("Deserialize")
    @MethodSource("deserializationCases")
    void testDeserialize(String input, String expected) throws IOException {
        String serializedJsonObject = objectMapper.writeValueAsString(new JsonObject(input));
        JsonObject deserializedJsonObject = objectMapper.readValue(serializedJsonObject, JsonObject.class);
        assertThat(deserializedJsonObject.getInput())
                .isEqualTo(expected);
    }

    @Nested
    @DisplayName("Get instance")
    class GetInstanceTest {

        @Test
        @DisplayName("Non-thread-safe")
        void testGetInstance() {
            ObjectMapper firstObjectMapper = ObjectMapperWrapper.getInstance();
            assertThat(firstObjectMapper).isNotNull();
            ObjectMapper secondObjectMapper = ObjectMapperWrapper.getInstance();
            assertThat(secondObjectMapper).isNotNull();
            assertThat(firstObjectMapper).isSameAs(secondObjectMapper);
        }

        @Test
        @DisplayName("Thread-safe")
        void testGetInstanceThreadSafe() throws InterruptedException {
            ObjectMapper[] objectMappers = new ObjectMapper[2];
            Thread firstThread = new Thread(() -> objectMappers[0] = ObjectMapperWrapper.getInstance());
            Thread secondThread = new Thread(() -> objectMappers[1] = ObjectMapperWrapper.getInstance());
            firstThread.start();
            secondThread.start();
            firstThread.join();
            secondThread.join();
            assertThat(objectMappers[0]).isNotNull();
            assertThat(objectMappers[1]).isNotNull();
            assertThat(objectMappers[0]).isSameAs(objectMappers[1]);
        }

    }

}

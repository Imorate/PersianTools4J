package com.persiantools4j.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Number utils")
class NumberUtilsTest {

    @ParameterizedTest(name = "[{index}] {0} {1}nth numeric character value is {2}")
    @DisplayName("Get numeric value test")
    @CsvSource({"1,0,1", "6104038932,9,2", "523,2,3"})
    void getNumericValueTest(String str, int index, int expected) {
        assertThat(NumberUtils.getNumericValue(str, index)).isEqualTo(expected);
    }
}

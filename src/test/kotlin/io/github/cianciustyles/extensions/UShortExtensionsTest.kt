package io.github.cianciustyles.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@ExperimentalUnsignedTypes
class UShortExtensionsTest {
    @Test
    fun testAnd() {
        // given
        val encoding: UShort = 0b1011001110010110u

        // when
        val result: UShort = encoding and 0x7u

        // then
        val expectedResult: UShort = 0b110u
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun testShr() {
        // given
        val encoding: UShort = 0b1011001110010110u

        // when
        val result: UShort = encoding shr 12

        // then
        val expectedResult: UShort = 0b1011u
        assertThat(result).isEqualTo(expectedResult)
    }
}
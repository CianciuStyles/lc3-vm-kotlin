package io.github.cianciustyles.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ShortExtensionsTest {
    @Test
    fun shortPlus() {
        val s1: Short = 5
        val s2: Short = 7

        val result: Short = s1 addShort s2

        assertThat(result).isEqualTo(12)
    }
}
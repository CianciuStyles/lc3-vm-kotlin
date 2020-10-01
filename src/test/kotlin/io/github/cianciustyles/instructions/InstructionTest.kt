package io.github.cianciustyles.instructions

import io.github.cianciustyles.exceptions.UnrecognisedInstructionException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@ExperimentalUnsignedTypes
class InstructionTest {
    @Test
    fun testAddEncoding() {
        // given
        val encoding = (0b0001 shl 12).toUShort()

        // when
        val add = Instruction.fetch(encoding)

        // then
        assertThat(add).isInstanceOf(Add::class.java)
    }

    @Test(expected = UnrecognisedInstructionException::class)
    fun testInvalidOpcode() {
        // given
        val encoding: UShort = (16 shl 12).toUShort()

        // when
        Instruction.fetch(encoding)

        // then - throw exception
    }
}

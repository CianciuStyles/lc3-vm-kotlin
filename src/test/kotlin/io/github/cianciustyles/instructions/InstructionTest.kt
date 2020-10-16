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

    @Test
    fun testAndEncoding() {
        // given
        val encoding = (0b0101 shl 12).toUShort()

        // when
        val and = Instruction.fetch(encoding)

        // then
        assertThat(and).isInstanceOf(And::class.java)
    }

    @Test
    fun testJumpEncoding() {
        // given
        val encoding = (0b1100 shl 12).toUShort()

        // when
        val jump = Instruction.fetch(encoding)

        // then
        assertThat(jump).isInstanceOf(Jump::class.java)
    }

    @Test
    fun testLoadBaseOffsetEncoding() {
        // given
        val encoding = (0b0110 shl 12).toUShort()

        // when
        val loadBaseOffset = Instruction.fetch(encoding)

        // then
        assertThat(loadBaseOffset).isInstanceOf(LoadBaseOffset::class.java)
    }

    @Test
    fun testLoadEffectiveAddressEncoding() {
        // given
        val encoding = (0b1110 shl 12).toUShort()

        // when
        val loadEffectiveAddress = Instruction.fetch(encoding)

        // then
        assertThat(loadEffectiveAddress).isInstanceOf(LoadEffectiveAddress::class.java)
    }

    @Test
    fun testLoadEncoding() {
        // given
        val encoding = (0b0010 shl 12).toUShort()

        // when
        val load = Instruction.fetch(encoding)

        // then
        assertThat(load).isInstanceOf(Load::class.java)
    }

    @Test
    fun testLoadIndirectEncoding() {
        // given
        val encoding = (0b1010 shl 12).toUShort()

        // when
        val loadIndirect = Instruction.fetch(encoding)

        // then
        assertThat(loadIndirect).isInstanceOf(LoadIndirect::class.java)
    }

    @Test
    fun testNotEncoding() {
        // given
        val encoding = (0b1001 shl 12).toUShort()

        // when
        val not = Instruction.fetch(encoding)

        // then
        assertThat(not).isInstanceOf(Not::class.java)
    }

    @Test
    fun testStoreEncoding() {
        // given
        val encoding = (0b0011 shl 12).toUShort()

        // when
        val store = Instruction.fetch(encoding)

        // then
        assertThat(store).isInstanceOf(Store::class.java)
    }

    @Test
    fun testStoreIndirectEncoding() {
        // given
        val encoding = (0b1011 shl 12).toUShort()

        // when
        val storeIndirect = Instruction.fetch(encoding)

        // then
        assertThat(storeIndirect).isInstanceOf(StoreIndirect::class.java)
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

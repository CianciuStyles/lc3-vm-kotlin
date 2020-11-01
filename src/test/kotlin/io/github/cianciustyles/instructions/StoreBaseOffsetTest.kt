package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.Utils.extendSign
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class StoreBaseOffsetTest {
    private lateinit var memory: Memory
    private lateinit var registers: Registers

    @Before
    fun setup() {
        memory = Memory()
        registers = Registers()
    }

    @Test
    fun testStoreBaseOffsetPositiveOffset() {
        // given
        val sourceRegister = 2
        val baseRegister = 5
        val offset6 = 15
        val offset6WithSignExtended = extendSign(offset6, 6)
        val encoding = encode(sourceRegister, baseRegister, offset6)

        val expectedResult: Short = 123
        registers[sourceRegister.toUShort()] = expectedResult
        val baseRegisterValue: Short = 0x400
        registers[baseRegister.toUShort()] = baseRegisterValue

        // when
        val storeBaseOffset = StoreBaseOffset(encoding)
        storeBaseOffset.execute(memory, registers)

        // then
        assertThat(storeBaseOffset.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(storeBaseOffset.baseRegister).isEqualTo(baseRegister.toUShort())
        assertThat(storeBaseOffset.offset6).isEqualTo(offset6WithSignExtended)
        val storeMemoryAddress = shortPlus(baseRegisterValue, offset6WithSignExtended).toUShort()
        assertThat(memory[storeMemoryAddress]).isEqualTo(expectedResult)
    }

    @Test
    fun testStoreBaseOffsetNegativeOffset() {
        // given
        val sourceRegister = 4
        val baseRegister = 1
        val offset6 = 0b111111 // -1 in 6 bits
        val offset6WithSignExtended = extendSign(offset6, 6)
        val encoding = encode(sourceRegister, baseRegister, offset6)

        val expectedResult: Short = 456
        registers[sourceRegister.toUShort()] = expectedResult
        val baseRegisterValue: Short = 0x280
        registers[baseRegister.toUShort()] = baseRegisterValue

        // when
        val storeBaseOffset = StoreBaseOffset(encoding)
        storeBaseOffset.execute(memory, registers)

        // then
        assertThat(storeBaseOffset.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(storeBaseOffset.baseRegister).isEqualTo(baseRegister.toUShort())
        assertThat(storeBaseOffset.offset6).isEqualTo(offset6WithSignExtended)
        val storeMemoryAddress = shortPlus(baseRegisterValue, offset6WithSignExtended).toUShort()
        assertThat(memory[storeMemoryAddress]).isEqualTo(expectedResult)
    }

    private fun encode(sourceRegister: Int, baseRegister: Int, offset6: Int) =
        ((0b1011 shl 12) or (sourceRegister shl 9) or (baseRegister shl 6) or offset6).toUShort()

    private fun shortPlus(short1: Short, short2: Short): Short =
        (short1 + short2).toShort()
}

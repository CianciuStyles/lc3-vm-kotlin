package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.Utils.extendSign
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class StoreIndirectTest {
    private lateinit var memory: Memory
    private lateinit var registers: Registers

    @Before
    fun setup() {
        memory = Memory()
        registers = Registers()
    }

    @Test
    fun testStoreIndirectPositiveOffset() {
        // given
        val sourceRegister = 2
        val pcOffset = 110 and 0x1FF
        val encoding = encode(sourceRegister, pcOffset)

        val initialValue: Short = 600
        registers.setPC(initialValue)
        val storeAddress: Short = 842
        memory[(initialValue + pcOffset).toUShort()] = storeAddress
        val expectedResult: Short = 42
        registers[sourceRegister.toUShort()] = expectedResult

        // when
        val storeIndirect = StoreIndirect(encoding)
        storeIndirect.execute(memory, registers)

        // then
        assertThat(storeIndirect.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(storeIndirect.pcOffset).isEqualTo(pcOffset.toShort())
        assertThat(memory[storeAddress.toUShort()]).isEqualTo(expectedResult)
    }

    @Test
    fun testStoreIndirectNegativeOffset() {
        // given
        val sourceRegister = 2
        val pcOffset = 0b111111111 // -1 in 9 bits
        val encoding = encode(sourceRegister, pcOffset)

        val initialValue: Short = 600
        registers.setPC(initialValue)
        val storeAddress: Short = 842
        memory[(initialValue + extendSign(pcOffset, 9)).toUShort()] = storeAddress
        val expectedResult: Short = 42
        registers[sourceRegister.toUShort()] = expectedResult

        // when
        val storeIndirect = StoreIndirect(encoding)
        storeIndirect.execute(memory, registers)

        // then
        assertThat(storeIndirect.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(storeIndirect.pcOffset).isEqualTo(extendSign(pcOffset, 9))
        assertThat(memory[storeAddress.toUShort()]).isEqualTo(expectedResult)
    }

    private fun encode(sourceRegister: Int, pcOffset: Int) =
        ((0b1011 shl 12) or (sourceRegister shl 9) or pcOffset).toUShort()
}

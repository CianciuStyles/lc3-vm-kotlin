package io.github.cianciustyles.instructions

import io.github.cianciustyles.ConditionFlags
import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class LoadTest {
    private lateinit var memory: Memory
    private lateinit var registers: Registers

    @Before
    fun setup() {
        memory = Memory()
        registers = Registers()
    }

    @Test
    fun testLoadPositive() {
        // given
        val destinationRegister = 5
        val pcOffset = 140 and 0x1FF
        val encoding = encode(destinationRegister, pcOffset)

        val expectedResult: Short = 12
        memory[(registers.getPC() + pcOffset).toUShort()] = expectedResult

        // when
        val load = Load(encoding)
        load.execute(memory, registers)

        // then
        assertThat(load.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(load.pcOffset).isEqualTo(pcOffset.toShort())
        assertThat(registers[load.destinationRegister]).isEqualTo(expectedResult)
        assertThat(registers.getCond()).isEqualTo(ConditionFlags.POSITIVE.value)
    }

    @Test
    fun testLoadZero() {
        // given
        val destinationRegister = 3
        val pcOffset = 8 and 0x1FF
        val encoding = encode(destinationRegister, pcOffset)

        registers.setPC(40)
        val expectedValue: Short = 0
        memory[(registers.getPC() + pcOffset).toUShort()] = expectedValue

        // when
        val load = Load(encoding)
        load.execute(memory, registers)

        // then
        assertThat(load.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(load.pcOffset).isEqualTo(pcOffset.toShort())
        assertThat(registers[load.destinationRegister]).isEqualTo(expectedValue)
        assertThat(registers.getCond()).isEqualTo(ConditionFlags.ZERO.value)
    }

    private fun encode(destinationRegister: Int, pcOffset: Int) =
        ((0b0010 shl 12) or (destinationRegister shl 9) or pcOffset).toUShort()
}

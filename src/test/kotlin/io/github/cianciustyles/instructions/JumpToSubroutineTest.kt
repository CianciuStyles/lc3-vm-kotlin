package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.Utils.extendSign
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class JumpToSubroutineTest {
    private lateinit var memory: Memory
    private lateinit var registers: Registers

    @Before
    fun setup() {
        memory = Memory()
        registers = Registers()
    }

    @Test
    fun testRegisterMode() {
        // given
        val baseRegister = 4
        val encoding = encodeRegister(baseRegister)

        val pcInitialValue: Short = 0x750
        registers.setPC(pcInitialValue)
        val pcFinalValue: Short = 0x400
        registers[baseRegister.toUShort()] = pcFinalValue

        // when
        val jumpToSubroutine = JumpToSubroutine(encoding)
        jumpToSubroutine.execute(memory, registers)

        // then
        assertThat(jumpToSubroutine.mode).isEqualTo(JumpToSubroutine.Mode.REGISTER_MODE)
        assertThat(jumpToSubroutine.baseRegister).isEqualTo(baseRegister.toUShort())
        assertThat(jumpToSubroutine.pcOffset11).isNull()
        assertThat(registers[7u]).isEqualTo(pcInitialValue)
        assertThat(registers.getPC()).isEqualTo(pcFinalValue)
    }

    @Test
    fun testImmediateModeForPositiveNumbers() {
        // given
        val pcOffset11 = 480
        val encoding = encodeImmediate(pcOffset11)

        val pcInitialValue: Short = 0x600
        registers.setPC(pcInitialValue)

        // when
        val jumpToSubroutine = JumpToSubroutine(encoding)
        jumpToSubroutine.execute(memory, registers)

        // then
        assertThat(jumpToSubroutine.mode).isEqualTo(JumpToSubroutine.Mode.IMMEDIATE_MODE)
        assertThat(jumpToSubroutine.baseRegister).isNull()
        assertThat(jumpToSubroutine.pcOffset11).isEqualTo(extendSign(pcOffset11, 11))
        assertThat(registers[7u]).isEqualTo(pcInitialValue)
        assertThat(registers.getPC()).isEqualTo(shortPlus(pcInitialValue, extendSign(pcOffset11, 11)))
    }

    @Test
    fun testImmediateModeForNegativeNumbers() {
        // given
        val pcOffset11 = 0x1FF // -1 in 11 bytes
        val encoding = encodeImmediate(pcOffset11)

        val pcInitialValue: Short = 0x800
        registers.setPC(pcInitialValue)

        // when
        val jumpToSubroutine = JumpToSubroutine(encoding)
        jumpToSubroutine.execute(memory, registers)

        // then
        assertThat(jumpToSubroutine.mode).isEqualTo(JumpToSubroutine.Mode.IMMEDIATE_MODE)
        assertThat(jumpToSubroutine.baseRegister).isNull()
        assertThat(jumpToSubroutine.pcOffset11).isEqualTo(extendSign(pcOffset11, 11))
        assertThat(registers[7u]).isEqualTo(pcInitialValue)
        assertThat(registers.getPC()).isEqualTo(shortPlus(pcInitialValue, extendSign(pcOffset11, 11)))
    }

    private fun encodeRegister(baseRegister: Int) =
        ((0b0100 shl 12) or (baseRegister shl 6)).toUShort()

    private fun encodeImmediate(pcOffset11: Int) =
        ((0b0100 shl 12) or (1 shl 11) or pcOffset11).toUShort()

    private fun shortPlus(short1: Short, short2: Short): Short =
        (short1 + short2).toShort()
}

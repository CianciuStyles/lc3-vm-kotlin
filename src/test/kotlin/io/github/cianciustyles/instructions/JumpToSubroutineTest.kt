package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import io.github.cianciustyles.Utils.shortPlus
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class JumpToSubroutineTest {
    private lateinit var vm: LC3VM

    @Before
    fun setup() {
        vm = LC3VM(running = true)
    }

    @Test
    fun testRegisterMode() {
        // given
        val baseRegister = 4
        val encoding = encodeRegister(baseRegister)

        val pcInitialValue: Short = 0x750
        vm.registers.setPC(pcInitialValue)
        val pcFinalValue: Short = 0x400
        vm.registers[baseRegister.toUShort()] = pcFinalValue

        // when
        val jumpToSubroutine = JumpToSubroutine(encoding)
        jumpToSubroutine.execute(vm)

        // then
        assertThat(jumpToSubroutine.mode).isEqualTo(JumpToSubroutine.Mode.REGISTER_MODE)
        assertThat(jumpToSubroutine.baseRegister).isEqualTo(baseRegister.toUShort())
        assertThat(jumpToSubroutine.pcOffset11).isNull()
        assertThat(vm.registers[7u]).isEqualTo(pcInitialValue)
        assertThat(vm.registers.getPC()).isEqualTo(pcFinalValue)
    }

    @Test
    fun testImmediateModeForPositiveNumbers() {
        // given
        val pcOffset11 = 480
        val encoding = encodeImmediate(pcOffset11)

        val pcInitialValue: Short = 0x600
        vm.registers.setPC(pcInitialValue)

        // when
        val jumpToSubroutine = JumpToSubroutine(encoding)
        jumpToSubroutine.execute(vm)

        // then
        assertThat(jumpToSubroutine.mode).isEqualTo(JumpToSubroutine.Mode.IMMEDIATE_MODE)
        assertThat(jumpToSubroutine.baseRegister).isNull()
        assertThat(jumpToSubroutine.pcOffset11).isEqualTo(extendSign(pcOffset11, 11))
        assertThat(vm.registers[7u]).isEqualTo(pcInitialValue)
        assertThat(vm.registers.getPC()).isEqualTo(shortPlus(pcInitialValue, extendSign(pcOffset11, 11)))
    }

    @Test
    fun testImmediateModeForNegativeNumbers() {
        // given
        val pcOffset11 = 0x1FF // -1 in 11 bytes
        val encoding = encodeImmediate(pcOffset11)

        val pcInitialValue: Short = 0x800
        vm.registers.setPC(pcInitialValue)

        // when
        val jumpToSubroutine = JumpToSubroutine(encoding)
        jumpToSubroutine.execute(vm)

        // then
        assertThat(jumpToSubroutine.mode).isEqualTo(JumpToSubroutine.Mode.IMMEDIATE_MODE)
        assertThat(jumpToSubroutine.baseRegister).isNull()
        assertThat(jumpToSubroutine.pcOffset11).isEqualTo(extendSign(pcOffset11, 11))
        assertThat(vm.registers[7u]).isEqualTo(pcInitialValue)
        assertThat(vm.registers.getPC()).isEqualTo(shortPlus(pcInitialValue, extendSign(pcOffset11, 11)))
    }

    private fun encodeRegister(baseRegister: Int) =
        ((0b0100 shl 12) or (baseRegister shl 6)).toUShort()

    private fun encodeImmediate(pcOffset11: Int) =
        ((0b0100 shl 12) or (1 shl 11) or pcOffset11).toUShort()
}

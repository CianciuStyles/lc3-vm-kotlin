package io.github.cianciustyles.instructions

import io.github.cianciustyles.ConditionFlags
import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class LoadIndirectTest {
    private lateinit var vm: LC3VM

    @Before
    fun setup() {
        vm = LC3VM(running = true)
    }

    @Test
    fun testLoadIndirectPositive() {
        // given
        val destinationRegister = 6
        val pcOffset = 200 and 0x1FF
        val encoding: UShort = encode(destinationRegister, pcOffset)

        vm.registers.setPC(400)
        val addressToLoad: Short = 405
        vm.memory[(vm.registers.getPC() + pcOffset).toUShort()] = addressToLoad
        val expectedResult: Short = 12
        vm.memory[addressToLoad.toUShort()] = expectedResult

        // when
        val loadIndirect = LoadIndirect(encoding)
        loadIndirect.execute(vm)

        // then
        assertThat(loadIndirect.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(loadIndirect.pcOffset).isEqualTo(extendSign(pcOffset, 9))
        assertThat(vm.registers[loadIndirect.destinationRegister]).isEqualTo(expectedResult)
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.POSITIVE.value)
    }

    @Test
    fun testLoadIndirectNegative() {
        // given
        val destinationRegister = 2
        val pcOffset = 250 and 0x1FF
        val encoding: UShort = encode(destinationRegister, pcOffset)

        val addressToLoad: Short = 113
        vm.memory[(vm.registers.getPC() + pcOffset).toUShort()] = addressToLoad
        val expectedResult: Short = -4
        vm.memory[addressToLoad.toUShort()] = expectedResult

        // when
        val loadIndirect = LoadIndirect(encoding)
        loadIndirect.execute(vm)

        // then
        assertThat(loadIndirect.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(loadIndirect.pcOffset).isEqualTo(extendSign(pcOffset, 9))
        assertThat(vm.registers[loadIndirect.destinationRegister]).isEqualTo(expectedResult)
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.NEGATIVE.value)
    }

    @Test
    fun testLoadIndirectZero() {
        // given
        val destinationRegister = 4
        val pcOffset = 150 and 0x1FF
        val encoding: UShort = encode(destinationRegister, pcOffset)

        vm.registers.setPC(782)
        val addressToLoad: Short = 210
        vm.memory[(vm.registers.getPC() + pcOffset).toUShort()] = addressToLoad
        val expectedResult: Short = 0
        vm.memory[addressToLoad.toUShort()] = expectedResult

        // when
        val loadIndirect = LoadIndirect(encoding)
        loadIndirect.execute(vm)

        // then
        assertThat(loadIndirect.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(loadIndirect.pcOffset).isEqualTo(extendSign(pcOffset, 9))
        assertThat(vm.registers[loadIndirect.destinationRegister]).isEqualTo(expectedResult)
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.ZERO.value)
    }

    private fun encode(destinationRegister: Int, pcOffset: Int) =
        ((0b1010 shl 12) or (destinationRegister shl 9) or pcOffset).toUShort()
}

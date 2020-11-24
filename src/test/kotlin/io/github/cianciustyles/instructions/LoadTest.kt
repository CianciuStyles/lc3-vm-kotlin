package io.github.cianciustyles.instructions

import io.github.cianciustyles.ConditionFlags
import io.github.cianciustyles.LC3VM
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class LoadTest {
    private lateinit var vm: LC3VM

    @Before
    fun setup() {
        vm = LC3VM(running = true)
    }

    @Test
    fun testLoadPositive() {
        // given
        val destinationRegister = 5
        val pcOffset = 140 and 0x1FF
        val encoding = encode(destinationRegister, pcOffset)

        val expectedResult: Short = 12
        vm.memory[(vm.registers.getPC() + pcOffset).toUShort()] = expectedResult

        // when
        val load = Load(encoding)
        load.execute(vm)

        // then
        assertThat(load.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(load.pcOffset).isEqualTo(pcOffset.toShort())
        assertThat(vm.registers[load.destinationRegister]).isEqualTo(expectedResult)
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.POSITIVE.value)
    }

    @Test
    fun testLoadZero() {
        // given
        val destinationRegister = 3
        val pcOffset = 8 and 0x1FF
        val encoding = encode(destinationRegister, pcOffset)

        vm.registers.setPC(40)
        val expectedValue: Short = 0
        vm.memory[(vm.registers.getPC() + pcOffset).toUShort()] = expectedValue

        // when
        val load = Load(encoding)
        load.execute(vm)

        // then
        assertThat(load.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(load.pcOffset).isEqualTo(pcOffset.toShort())
        assertThat(vm.registers[load.destinationRegister]).isEqualTo(expectedValue)
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.ZERO.value)
    }

    private fun encode(destinationRegister: Int, pcOffset: Int) =
        ((0b0010 shl 12) or (destinationRegister shl 9) or pcOffset).toUShort()
}

package io.github.cianciustyles.instructions

import io.github.cianciustyles.ConditionFlags
import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class LoadEffectiveAddressTest {
    private lateinit var vm: LC3VM

    @Before
    fun setup() {
        vm = LC3VM(running = true)
    }

    @Test
    fun testLoadEffectiveAddressPositive() {
        // given
        val destinationRegister = 5
        val pcOffset9 = 200 and 0x1FF
        val encoding = encode(destinationRegister, pcOffset9)

        val expectedResult: Short = 7
        vm.memory[(vm.registers.getPC() + pcOffset9).toUShort()] = expectedResult

        // when
        val loadEffectiveAddress = LoadEffectiveAddress(encoding)
        loadEffectiveAddress.execute(vm)

        // then
        assertThat(loadEffectiveAddress.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(loadEffectiveAddress.pcOffset9).isEqualTo(extendSign(pcOffset9, 9))
        assertThat(vm.registers[destinationRegister.toUShort()]).isEqualTo(expectedResult)
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.POSITIVE.value)
    }

    @Test
    fun testLoadEffectiveAddressNegative() {
        // given
        val destinationRegister = 2
        val pcOffset9 = 190 and 0x1FF
        val encoding = encode(destinationRegister, pcOffset9)

        vm.registers.setPC(30)
        val expectedResult: Short = -3
        vm.memory[(vm.registers.getPC() + pcOffset9).toUShort()] = expectedResult

        // when
        val loadEffectiveAddress = LoadEffectiveAddress(encoding)
        loadEffectiveAddress.execute(vm)

        // then
        assertThat(loadEffectiveAddress.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(loadEffectiveAddress.pcOffset9).isEqualTo(extendSign(pcOffset9, 9))
        assertThat(vm.registers[destinationRegister.toUShort()]).isEqualTo(expectedResult)
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.NEGATIVE.value)
    }

    @Test
    fun testLoadEffectiveAddressZero() {
        // given
        val destinationRegister = 4
        val pcOffset9 = 80 and 0x1FF
        val encoding = encode(destinationRegister, pcOffset9)

        vm.registers.setPC(100)
        val expectedResult: Short = 0
        vm.memory[(vm.registers.getPC() + pcOffset9).toUShort()] = expectedResult

        // when
        val loadEffectiveAddress = LoadEffectiveAddress(encoding)
        loadEffectiveAddress.execute(vm)

        // then
        assertThat(loadEffectiveAddress.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(loadEffectiveAddress.pcOffset9).isEqualTo(extendSign(pcOffset9, 9))
        assertThat(vm.registers[destinationRegister.toUShort()]).isEqualTo(expectedResult)
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.ZERO.value)
    }

    private fun encode(destinationRegister: Int, pcOffset9: Int) =
        ((0b1110 shl 12) or (destinationRegister shl 9) or pcOffset9).toUShort()
}

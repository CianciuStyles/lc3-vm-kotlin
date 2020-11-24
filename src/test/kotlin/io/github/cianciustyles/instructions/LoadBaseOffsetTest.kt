package io.github.cianciustyles.instructions

import io.github.cianciustyles.ConditionFlags
import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class LoadBaseOffsetTest {
    private lateinit var vm: LC3VM

    @Before
    fun setup() {
        vm = LC3VM(running = true)
    }

    @Test
    fun testLoadBaseOffsetPositive() {
        // given
        val destinationRegister = 3
        val baseRegister = 2
        val addressOffset = 14 and 0x3F
        val encoding = encode(destinationRegister, baseRegister, addressOffset)

        vm.registers.setPC(400)
        val baseAddress: Short = 200
        vm.registers[baseRegister.toUShort()] = baseAddress
        val expectedResult: Short = 7
        vm.memory[(baseAddress + addressOffset).toUShort()] = expectedResult

        // when
        val loadBaseOffset = LoadBaseOffset(encoding)
        loadBaseOffset.execute(vm)

        // then
        assertThat(loadBaseOffset.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(loadBaseOffset.baseRegister).isEqualTo(baseRegister.toUShort())
        assertThat(loadBaseOffset.offset6).isEqualTo(extendSign(addressOffset, 6))
        assertThat(vm.registers[loadBaseOffset.destinationRegister]).isEqualTo(expectedResult)
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.POSITIVE.value)
    }

    @Test
    fun testLoadBaseOffsetNegative() {
        // given
        val destinationRegister = 1
        val baseRegister = 5
        val addressOffset = 10 and 0x3F
        val encoding = encode(destinationRegister, baseRegister, addressOffset)

        vm.registers.setPC(420)
        val baseAddress: Short = 300
        vm.registers[baseRegister.toUShort()] = baseAddress
        val expectedResult: Short = -6
        vm.memory[(baseAddress + addressOffset).toUShort()] = expectedResult

        // when
        val loadBaseOffset = LoadBaseOffset(encoding)
        loadBaseOffset.execute(vm)

        // then
        assertThat(loadBaseOffset.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(loadBaseOffset.baseRegister).isEqualTo(baseRegister.toUShort())
        assertThat(loadBaseOffset.offset6).isEqualTo(extendSign(addressOffset, 6))
        assertThat(vm.registers[loadBaseOffset.destinationRegister]).isEqualTo(expectedResult)
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.NEGATIVE.value)
    }

    @Test
    fun testLoadBaseOffsetZero() {
        // given
        val destinationRegister = 0
        val baseRegister = 6
        val addressOffset = 0b111111 // -1 in 6 bytes
        val encoding = encode(destinationRegister, baseRegister, addressOffset)

        vm.registers.setPC(400)
        val addressBase: Short = 200
        vm.registers[baseRegister.toUShort()] = addressBase
        val expectedResult: Short = 0
        vm.memory[(addressBase + addressOffset).toUShort()] = expectedResult

        // when
        val loadBaseOffset = LoadBaseOffset(encoding)
        loadBaseOffset.execute(vm)

        // then
        assertThat(loadBaseOffset.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(loadBaseOffset.baseRegister).isEqualTo(baseRegister.toUShort())
        assertThat(loadBaseOffset.offset6).isEqualTo(extendSign(addressOffset, 6))
        assertThat(vm.registers[loadBaseOffset.destinationRegister]).isEqualTo(expectedResult)
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.ZERO.value)
    }

    private fun encode(destinationRegister: Int, baseRegister: Int, addressOffset: Int) =
        ((0b0110 shl 12) or (destinationRegister shl 9) or (baseRegister shl 6) or addressOffset).toUShort()
}

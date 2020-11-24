package io.github.cianciustyles.instructions

import io.github.cianciustyles.ConditionFlags
import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.experimental.and

@ExperimentalUnsignedTypes
class AndTest {
    private lateinit var vm: LC3VM

    @Before
    fun setup() {
        vm = LC3VM(running = true)
    }

    @Test
    fun testRegisterMode() {
        // given
        val destinationRegister = 5
        val sourceRegister1 = 1
        val sourceRegister2 = 4
        val encoding = encodeRegister(destinationRegister, sourceRegister1, sourceRegister2)

        val firstValue: Short = 0b10101
        vm.registers[sourceRegister1.toUShort()] = firstValue
        val secondValue: Short = 0b11110
        vm.registers[sourceRegister2.toUShort()] = secondValue

        // when
        val and = And(encoding)
        and.execute(vm)

        // then
        assertThat(and.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(and.sourceRegister1).isEqualTo(sourceRegister1.toUShort())
        assertThat(and.mode).isEqualTo(And.Mode.REGISTER_MODE)
        assertThat(and.sourceRegister2).isEqualTo(sourceRegister2.toUShort())
        assertThat(and.immediateValue).isNull()
        assertThat(vm.registers[destinationRegister.toUShort()]).isEqualTo(firstValue and secondValue)
    }

    @Test
    fun testImmediateModeForPositiveNumbers() {
        // given
        val destinationRegister = 3
        val sourceRegister1 = 1
        val immediateValue = 0b11101
        val encoding = encodeImmediate(destinationRegister, sourceRegister1, immediateValue)

        val registerValue: Short = 0b10001
        vm.registers[sourceRegister1.toUShort()] = registerValue

        // when
        val and = And(encoding)
        and.execute(vm)

        // then
        assertThat(and.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(and.sourceRegister1).isEqualTo(sourceRegister1.toUShort())
        assertThat(and.mode).isEqualTo(And.Mode.IMMEDIATE_MODE)
        assertThat(and.sourceRegister2).isNull()
        assertThat(and.immediateValue).isEqualTo(extendSign(immediateValue, 5))
        assertThat(vm.registers[destinationRegister.toUShort()]).isEqualTo(registerValue and extendSign(immediateValue, 5))
    }

    @Test
    fun testImmediateModeForNegativeNumbers() {
        // given
        val destinationRegister = 1
        val sourceRegister1 = 2
        val immediateValue = 0b11111 // -1 in 5 bytes
        val encoding = encodeImmediate(destinationRegister, sourceRegister1, immediateValue)

        val registerValue: Short = -4
        vm.registers[sourceRegister1.toUShort()] = registerValue

        // when
        val and = And(encoding)
        and.execute(vm)

        // then
        assertThat(and.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(and.sourceRegister1).isEqualTo(sourceRegister1.toUShort())
        assertThat(and.mode).isEqualTo(And.Mode.IMMEDIATE_MODE)
        assertThat(and.sourceRegister2).isNull()
        assertThat(and.immediateValue).isEqualTo(extendSign(immediateValue, 5))
        assertThat(vm.registers[destinationRegister.toUShort()]).isEqualTo(registerValue and extendSign(immediateValue, 5))
    }

    @Test
    fun testFlagZero() {
        // given
        val destinationRegister = 2
        val sourceRegister1 = 1
        val immediateValue = 0b11001
        val encoding: UShort = encodeImmediate(destinationRegister, sourceRegister1, immediateValue)

        vm.registers[sourceRegister1.toUShort()] = 0b110

        // when
        val and = And(encoding)
        and.execute(vm)

        // then
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.ZERO.value)
    }

    @Test
    fun testFlagPositive() {
        // given
        val destinationRegister = 6
        val sourceRegister1 = 2
        val immediateValue = 1
        val encoding = encodeImmediate(destinationRegister, sourceRegister1, immediateValue)

        vm.registers[sourceRegister1.toUShort()] = 3

        // when
        val and = And(encoding)
        and.execute(vm)

        // then
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.POSITIVE.value)
    }

    @Test
    fun testFlagNegative() {
        // given
        val destinationRegister = 4
        val sourceRegister1 = 3
        val immediateValue = 0b11111 // -1 in 5 bytes
        val encoding: UShort = encodeImmediate(destinationRegister, sourceRegister1, immediateValue)

        vm.registers[sourceRegister1.toUShort()] = -6

        // when
        val and = And(encoding)
        and.execute(vm)

        // then
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.NEGATIVE.value)
    }

    private fun encodeRegister(destinationRegister: Int, sourceRegister1: Int, sourceRegister2: Int) =
        ((0b0001 shl 12) or (destinationRegister shl 9) or (sourceRegister1 shl 6) or sourceRegister2).toUShort()

    private fun encodeImmediate(destinationRegister: Int, sourceRegister1: Int, immediateValue: Int) =
        ((0b0001 shl 12) or (destinationRegister shl 9) or (sourceRegister1 shl 6) or (1 shl 5) or immediateValue).toUShort()
}

package io.github.cianciustyles.instructions

import io.github.cianciustyles.ConditionFlags
import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class AddTest {
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
        val destinationRegister = 2
        val sourceRegister1 = 3
        val sourceRegister2 = 6
        val encoding = encodeRegister(destinationRegister, sourceRegister1, sourceRegister2)

        val firstValue: Short = 4
        registers[sourceRegister1.toUShort()] = firstValue
        val secondValue: Short = 2
        registers[sourceRegister2.toUShort()] = secondValue

        // when
        val add = Add(encoding)
        add.execute(memory, registers)

        // then
        assertThat(add.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(add.sourceRegister1).isEqualTo(sourceRegister1.toUShort())
        assertThat(add.mode).isEqualTo(Add.Mode.REGISTER_MODE)
        assertThat(add.sourceRegister2).isEqualTo(sourceRegister2.toUShort())
        assertThat(add.immediateValue).isNull()
        assertThat(registers[destinationRegister.toUShort()]).isEqualTo(shortPlus(firstValue, secondValue))
    }

    @Test
    fun testImmediateModeForPositiveNumbers() {
        // given
        val destinationRegister = 5
        val sourceRegister1 = 3
        val immediateValue = 7
        val encoding = encodeImmediate(destinationRegister, sourceRegister1, immediateValue)

        val registerValue: Short = 1
        registers[sourceRegister1.toUShort()] = registerValue

        // when
        val add = Add(encoding)
        add.execute(memory, registers)

        // then
        assertThat(add.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(add.sourceRegister1).isEqualTo(sourceRegister1.toUShort())
        assertThat(add.mode).isEqualTo(Add.Mode.IMMEDIATE_MODE)
        assertThat(add.sourceRegister2).isNull()
        assertThat(add.immediateValue).isEqualTo(extendSign(immediateValue))
        assertThat(registers[destinationRegister.toUShort()]).isEqualTo(shortPlus(registerValue, extendSign(immediateValue)))
    }

    @Test
    fun testImmediateModeForNegativeNumbers() {
        // given
        val destinationRegister = 0
        val sourceRegister1 = 3
        val immediateValue = 0b11111 // -1 in 5 bytes
        val encoding = encodeImmediate(destinationRegister, sourceRegister1, immediateValue)

        val registerValue: Short = 3
        registers[sourceRegister1.toUShort()] = registerValue

        // when
        val add = Add(encoding)
        add.execute(memory, registers)

        // then
        assertThat(add.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(add.sourceRegister1).isEqualTo(sourceRegister1.toUShort())
        assertThat(add.mode).isEqualTo(Add.Mode.IMMEDIATE_MODE)
        assertThat(add.sourceRegister2).isNull()
        assertThat(add.immediateValue).isEqualTo(extendSign(immediateValue))
        assertThat(registers[destinationRegister.toUShort()]).isEqualTo(shortPlus(registerValue, extendSign(immediateValue)))
    }

    @Test
    fun testFlagZero() {
        // given
        val destinationRegister = 5
        val sourceRegister1 = 3
        val immediateValue = 0b11111 // -1 in 5 bytes
        val encoding: UShort = encodeImmediate(destinationRegister, sourceRegister1, immediateValue)

        registers[sourceRegister1.toUShort()] = 1

        // when
        val add = Add(encoding)
        add.execute(memory, registers)

        // then
        assertThat(registers.getCond()).isEqualTo(ConditionFlags.ZERO.value)
    }

    @Test
    fun testFlagPositive() {
        // given
        val destinationRegister = 6
        val sourceRegister1 = 2
        val immediateValue = 1
        val encoding = encodeImmediate(destinationRegister, sourceRegister1, immediateValue)

        registers[sourceRegister1.toUShort()] = 3

        // when
        val add = Add(encoding)
        add.execute(memory, registers)

        // then
        assertThat(registers.getCond()).isEqualTo(ConditionFlags.POSITIVE.value)
    }

    @Test
    fun testFlagNegative() {
        // given
        val destinationRegister = 4
        val sourceRegister1 = 3
        val immediateValue = 2
        val encoding: UShort = encodeImmediate(destinationRegister, sourceRegister1, immediateValue)

        registers[sourceRegister1.toUShort()] = -4

        // when
        val add = Add(encoding)
        add.execute(memory, registers)

        // then
        assertThat(registers.getCond()).isEqualTo(ConditionFlags.NEGATIVE.value)
    }

    private fun encodeRegister(destinationRegister: Int, sourceRegister1: Int, sourceRegister2: Int) =
        ((0b0001 shl 12) or (destinationRegister shl 9) or (sourceRegister1 shl 6) or sourceRegister2).toUShort()

    private fun encodeImmediate(destinationRegister: Int, sourceRegister1: Int, immediateValue: Int) =
        ((0b0001 shl 12) or (destinationRegister shl 9) or (sourceRegister1 shl 6) or (1 shl 5) or immediateValue).toUShort()

    private fun extendSign(x: Int, bitCount: Int = 5): Short {
        val num = if (x shr bitCount - 1 and 0b1 == 1) {
            (0xFFFF shl bitCount) or x
        } else {
            x
        }

        return num.toShort()
    }

    private fun shortPlus(short1: Short, short2: Short): Short =
        (short1 + short2).toShort()
}

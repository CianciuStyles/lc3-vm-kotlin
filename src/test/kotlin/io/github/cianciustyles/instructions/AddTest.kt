package io.github.cianciustyles.instructions

import io.github.cianciustyles.ConditionFlags
import io.github.cianciustyles.Registers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class AddTest {
    private lateinit var registers: Registers

    @Before
    fun setup() {
        registers = Registers()
    }

    @Test
    fun testRegisterMode() {
        // given
        val encoding: UShort = 0b0001101011000110u
        registers[3u] = 4
        registers[6u] = 2

        // when
        val add = Add(encoding)
        add.execute(registers)

        // then
        val expectedDestinationRegister: UShort = 5u
        assertThat(add.destinationRegister).isEqualTo(expectedDestinationRegister)

        val expectedSourceRegister1: UShort = 3u
        assertThat(add.sourceRegister1).isEqualTo(expectedSourceRegister1)

        assertThat(add.mode).isEqualTo(Add.Mode.REGISTER_MODE)

        val expectedSourceRegister2: UShort = 6u
        assertThat(add.sourceRegister2).isEqualTo(expectedSourceRegister2)

        assertThat(add.immediateValue).isNull()

        assertThat(registers[expectedDestinationRegister]).isEqualTo(6)
    }

    @Test
    fun testImmediateModeForPositiveNumbers() {
        // given
        val encoding: UShort = 0b0001101011100111u
        registers[3u] = 1

        // when
        val add = Add(encoding)
        add.execute(registers)

        // then
        val expectedDestinationRegister: UShort = 5u
        assertThat(add.destinationRegister).isEqualTo(expectedDestinationRegister)

        val expectedSourceRegister1: UShort = 3u
        assertThat(add.sourceRegister1).isEqualTo(expectedSourceRegister1)

        assertThat(add.mode).isEqualTo(Add.Mode.IMMEDIATE_MODE)
        assertThat(add.sourceRegister2).isNull()

        val expectedImmediateValue: Short = 7
        assertThat(add.immediateValue).isEqualTo(expectedImmediateValue)

        assertThat(registers[expectedDestinationRegister]).isEqualTo(8)
    }

    @Test
    fun testImmediateModeForNegativeNumbers() {
        // given
        val encoding: UShort = 0b0001101011111111u
        registers[3u] = 3

        // when
        val add = Add(encoding)
        add.execute(registers)

        // then
        val expectedDestinationRegister: UShort = 5u
        assertThat(add.destinationRegister).isEqualTo(expectedDestinationRegister)

        val expectedSourceRegister1: UShort = 3u
        assertThat(add.sourceRegister1).isEqualTo(expectedSourceRegister1)

        assertThat(add.mode).isEqualTo(Add.Mode.IMMEDIATE_MODE)
        assertThat(add.sourceRegister2).isNull()

        val expectedImmediateValue: Short = -1
        assertThat(add.immediateValue).isEqualTo(expectedImmediateValue)

        assertThat(registers[expectedDestinationRegister]).isEqualTo(2)
    }

    @Test
    fun testFlagZero() {
        // given
        val encoding: UShort = 0b0001101011111111u
        registers[3u] = 1

        // when
        val add = Add(encoding)
        add.execute(registers)

        // then
        assertThat(registers.getCondRegister()).isEqualTo(ConditionFlags.ZERO.value)
    }

    @Test
    fun testFlagPositive() {
        // given
        val encoding: UShort = 0b0001101011111111u
        registers[3u] = 3

        // when
        val add = Add(encoding)
        add.execute(registers)

        // then
        assertThat(registers.getCondRegister()).isEqualTo(ConditionFlags.POSITIVE.value)
    }

    @Test
    fun testFlagNegative() {
        // given
        val encoding: UShort = 0b0001101011111111u
        registers[3u] = 0

        // when
        val add = Add(encoding)
        add.execute(registers)

        // then
        assertThat(registers.getCondRegister()).isEqualTo(ConditionFlags.NEGATIVE.value)
    }
}

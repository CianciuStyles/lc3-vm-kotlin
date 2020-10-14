package io.github.cianciustyles.instructions

import io.github.cianciustyles.ConditionFlags
import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.experimental.inv

@ExperimentalUnsignedTypes
class NotTest {
    private lateinit var memory: Memory
    private lateinit var registers: Registers

    @Before
    fun setup() {
        memory = Memory()
        registers = Registers()
    }

    @Test
    fun testFlagZero() {
        // given
        val destinationRegister = 2
        val sourceRegister = 1
        val encoding: UShort = encode(destinationRegister, sourceRegister)

        val value: Short = -1
        registers[sourceRegister.toUShort()] = value

        // when
        val not = Not(encoding)
        not.execute(memory, registers)

        // then
        assertThat(not.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(not.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(registers[destinationRegister.toUShort()]).isEqualTo(value.inv())
        assertThat(registers.getCond()).isEqualTo(ConditionFlags.ZERO.value)
    }

    @Test
    fun testFlagPositive() {
        // given
        val destinationRegister = 6
        val sourceRegister = 2
        val encoding = encode(destinationRegister, sourceRegister)

        val value: Short = -5
        registers[sourceRegister.toUShort()] = value

        // when
        val not = Not(encoding)
        not.execute(memory, registers)

        // then
        assertThat(not.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(not.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(registers[destinationRegister.toUShort()]).isEqualTo(value.inv())
        assertThat(registers.getCond()).isEqualTo(ConditionFlags.POSITIVE.value)
    }

    @Test
    fun testFlagNegative() {
        // given
        val destinationRegister = 4
        val sourceRegister = 3
        val encoding: UShort = encode(destinationRegister, sourceRegister)

        val value: Short = 5
        registers[sourceRegister.toUShort()] = value

        // when
        val not = Not(encoding)
        not.execute(memory, registers)

        // then
        assertThat(not.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(not.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(registers[destinationRegister.toUShort()]).isEqualTo(value.inv())
        assertThat(registers.getCond()).isEqualTo(ConditionFlags.NEGATIVE.value)
    }

    private fun encode(destinationRegister: Int, sourceRegister: Int) =
        ((0b1001 shl 12) or (destinationRegister shl 9) or (sourceRegister shl 6) or 0b111111).toUShort()
}

package io.github.cianciustyles.instructions

import io.github.cianciustyles.ConditionFlags
import io.github.cianciustyles.LC3VM
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.experimental.inv

@ExperimentalUnsignedTypes
class NotTest {
    private lateinit var vm: LC3VM

    @Before
    fun setup() {
        vm = LC3VM(running = true)
    }

    @Test
    fun testFlagZero() {
        // given
        val destinationRegister = 2
        val sourceRegister = 1
        val encoding: UShort = encode(destinationRegister, sourceRegister)

        val value: Short = -1
        vm.registers[sourceRegister.toUShort()] = value

        // when
        val not = Not(encoding)
        not.execute(vm)

        // then
        assertThat(not.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(not.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(vm.registers[destinationRegister.toUShort()]).isEqualTo(value.inv())
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.ZERO.value)
    }

    @Test
    fun testFlagPositive() {
        // given
        val destinationRegister = 6
        val sourceRegister = 2
        val encoding = encode(destinationRegister, sourceRegister)

        val value: Short = -5
        vm.registers[sourceRegister.toUShort()] = value

        // when
        val not = Not(encoding)
        not.execute(vm)

        // then
        assertThat(not.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(not.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(vm.registers[destinationRegister.toUShort()]).isEqualTo(value.inv())
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.POSITIVE.value)
    }

    @Test
    fun testFlagNegative() {
        // given
        val destinationRegister = 4
        val sourceRegister = 3
        val encoding: UShort = encode(destinationRegister, sourceRegister)

        val value: Short = 5
        vm.registers[sourceRegister.toUShort()] = value

        // when
        val not = Not(encoding)
        not.execute(vm)

        // then
        assertThat(not.destinationRegister).isEqualTo(destinationRegister.toUShort())
        assertThat(not.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(vm.registers[destinationRegister.toUShort()]).isEqualTo(value.inv())
        assertThat(vm.registers.getCond()).isEqualTo(ConditionFlags.NEGATIVE.value)
    }

    private fun encode(destinationRegister: Int, sourceRegister: Int) =
        ((0b1001 shl 12) or (destinationRegister shl 9) or (sourceRegister shl 6) or 0b111111).toUShort()
}

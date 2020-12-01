package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Memory
import io.github.cianciustyles.exceptions.UnrecognisedTrapException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintStream
import kotlin.experimental.and

@ExperimentalUnsignedTypes
class TrapTest {
    private lateinit var vm: LC3VM

    @Before
    fun setUp() {
        vm = LC3VM(running = true)
    }

    @Test
    fun testGetc() {
        // given
        val encoding = encodeTrap(Trap.GETC)
        val character = 's'
        vm.memory[Memory.KBSR.toUShort()] = 1
        vm.memory[Memory.KBDR.toUShort()] = character.toShort()

        // when
        val trap = Trap(encoding)
        trap.execute(vm)

        // then
        assertThat(vm.registers[0u]).isEqualTo(character.toShort())
    }

    @Test
    fun testHalt() {
        // given
        val encoding = encodeTrap(Trap.HALT)
        val output = redirectOutput()

        // when
        val trap = Trap(encoding)
        trap.execute(vm)

        // then
        assertThat(output.toString()).isEqualTo("HALT${System.lineSeparator()}")
        assertThat(vm.running).isEqualTo(false)
    }

    @Test
    fun testIn() {
        // given
        val encoding = encodeTrap(Trap.IN)
        val character = 'A'
        redirectInput(character.toString())
        val out = redirectOutput()

        // when
        val trap = Trap(encoding)
        trap.execute(vm)

        // then
        assertThat(vm.registers[0u]).isEqualTo(character.toShort() and 0xFF)
        assertThat(out.toString()).isEqualTo("Enter a character: $character")
    }

    @Test
    fun testOut() {
        // given
        val encoding = encodeTrap(Trap.OUT)
        val character = 'f'
        vm.registers[0u] = character.toShort()
        val output = redirectOutput()

        // when
        val trap = Trap(encoding)
        trap.execute(vm)

        // then
        assertThat(output.toString()).isEqualTo(character.toString())
    }

    @Test
    fun testPuts() {
        // given
        val encoding = encodeTrap(Trap.PUTS)
        val startAddress: Short = 0x150
        val text = "Hello, world"
        for ((index, char) in text.withIndex()) {
            val currentAddress = startAddress + index
            vm.memory[currentAddress.toUShort()] = char.toShort()
        }
        vm.registers[0u] = startAddress
        val output = redirectOutput()

        // when
        val trap = Trap(encoding)
        trap.execute(vm)

        // then
        assertThat(output.toString()).isEqualTo(text)
    }

    @Test(expected = UnrecognisedTrapException::class)
    fun testUnrecognisedTrap() {
        // given
        val encoding = encodeTrap(0x10)

        // when
        val trap = Trap(encoding)
        trap.execute(vm)

        // then - throw exception
    }

    private fun encodeTrap(trapVector: Int): UShort =
        (((0b1101) shl 12) or (trapVector)).toUShort()

    private fun redirectInput(text: String) {
        System.setIn(ByteArrayInputStream(text.toByteArray()))
    }

    private fun redirectOutput(): OutputStream {
        val out = ByteArrayOutputStream()
        System.setOut(PrintStream(out))
        return out
    }
}

package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.exceptions.PrivilegeModeException
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class ReturnFromInterruptTest {
    private lateinit var memory: Memory
    private lateinit var registers: Registers

    @Before
    fun setUp() {
        memory = Memory()
        registers = Registers()
    }

    @Test(expected = PrivilegeModeException::class)
    fun testThrowsPrivilegeModeException() {
        val encoding: UShort = (0b1000 shl 12).toUShort()

        val returnFromInterrupt = ReturnFromInterrupt(encoding)
        returnFromInterrupt.execute(memory, registers)
    }
}

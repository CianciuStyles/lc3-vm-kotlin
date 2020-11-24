package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.exceptions.PrivilegeModeException
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class ReturnFromInterruptTest {
    private lateinit var vm: LC3VM

    @Before
    fun setUp() {
        vm = LC3VM(running = true)
    }

    @Test(expected = PrivilegeModeException::class)
    fun testThrowsPrivilegeModeException() {
        val encoding: UShort = (0b1000 shl 12).toUShort()

        val returnFromInterrupt = ReturnFromInterrupt(encoding)
        returnFromInterrupt.execute(vm)
    }
}

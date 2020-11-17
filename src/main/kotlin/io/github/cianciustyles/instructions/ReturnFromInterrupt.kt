package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.exceptions.PrivilegeModeException

@ExperimentalUnsignedTypes
class ReturnFromInterrupt(val encoding: UShort) : Instruction() {
    override fun execute(memory: Memory, registers: Registers) {
        throw PrivilegeModeException()
    }
}

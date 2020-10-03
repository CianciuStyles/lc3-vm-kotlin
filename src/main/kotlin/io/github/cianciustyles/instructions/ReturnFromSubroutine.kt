package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers

@ExperimentalUnsignedTypes
class ReturnFromSubroutine : Instruction() {
    override fun execute(memory: Memory, registers: Registers) {
        registers.setPC(registers[7u])
    }
}

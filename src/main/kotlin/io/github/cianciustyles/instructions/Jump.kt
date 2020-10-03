package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers

@ExperimentalUnsignedTypes
class Jump(encoding: UShort) : Instruction() {
    val baseRegister: UShort = (encoding.toInt() shr 6 and 0x7).toUShort()
    override fun execute(memory: Memory, registers: Registers) {
        registers.setPC(registers[baseRegister])
    }
}

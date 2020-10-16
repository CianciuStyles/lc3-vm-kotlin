package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.Utils

@ExperimentalUnsignedTypes
class Store(encoding: UShort) : Instruction() {
    val sourceRegister: UShort = (encoding.toInt() shr 9 and 0x7).toUShort()
    val pcOffset: Short = Utils.extendSign(encoding.toInt() and 0x1FF, 9)

    override fun execute(memory: Memory, registers: Registers) {
        memory[(registers.getPC() + pcOffset).toUShort()] = registers[sourceRegister]
    }
}
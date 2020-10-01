package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers

@ExperimentalUnsignedTypes
class LoadIndirect(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = (encoding.toInt() shr 9 and 0x7).toUShort()
    val pcOffset: Short = extendSign(encoding.toInt() and 0x1FF, 9)

    override fun execute(memory: Memory, registers: Registers) {
        val newOffset: UShort = (registers.getPC() + pcOffset).toUShort()
        val addressToLoad: UShort = memory[newOffset].toUShort()
        val valueToCopy = memory[addressToLoad]
        registers[destinationRegister] = valueToCopy
        registers.setCond(valueToCopy)
    }
}

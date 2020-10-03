package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.Utils.extendSign

@ExperimentalUnsignedTypes
class LoadIndirect(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = (encoding.toInt() shr 9 and 0x7).toUShort()
    val pcOffset: Short = extendSign(encoding.toInt() and 0x1FF, 9)

    override fun execute(memory: Memory, registers: Registers) {
        val newOffset: UShort = (registers.getPC() + pcOffset).toUShort()
        val addressToLoad: UShort = memory[newOffset].toUShort()
        loadAndSetConditionCodes(
            memory,
            registers,
            addressToLoad,
            destinationRegister
        )
    }
}
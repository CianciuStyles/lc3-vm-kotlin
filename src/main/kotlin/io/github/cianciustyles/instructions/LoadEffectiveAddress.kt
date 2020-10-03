package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.Utils.extendSign

@ExperimentalUnsignedTypes
class LoadEffectiveAddress(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = (encoding.toInt() shr 9 and 0x7).toUShort()
    val pcOffset9: Short = extendSign(encoding.toInt() and 0x1FF, 9)

    override fun execute(memory: Memory, registers: Registers) {
        loadAndSetConditionCodes(
            memory,
            registers,
            (registers.getPC() + pcOffset9).toUShort(),
            destinationRegister
        )
    }
}
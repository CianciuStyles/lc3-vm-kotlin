package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.Utils.extendSign

@ExperimentalUnsignedTypes
class LoadBaseOffset(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = (encoding.toInt() shr 9 and 0x7).toUShort()
    val baseRegister: UShort = (encoding.toInt() shr 6 and 0x7).toUShort()
    val offset6: Short = extendSign(encoding.toInt() and 0x3F, 6)

    override fun execute(memory: Memory, registers: Registers) {
        loadAndSetConditionCodes(
            memory,
            registers,
            (registers[baseRegister] + offset6).toUShort(),
            destinationRegister
        )
    }
}

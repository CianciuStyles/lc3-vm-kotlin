package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.Utils.extendSign

@ExperimentalUnsignedTypes
class StoreBaseOffset(val encoding: UShort) : Instruction() {
    val sourceRegister: UShort = (encoding.toInt() shr 9 and 0b111).toUShort()
    val baseRegister: UShort = (encoding.toInt() shr 6 and 0b111).toUShort()
    val offset6: Short = extendSign(encoding.toInt() and 0b111111, 6)

    override fun execute(memory: Memory, registers: Registers) {
        store(
            memory,
            (registers[baseRegister] + offset6).toUShort(),
            registers[sourceRegister]
        )
    }
}

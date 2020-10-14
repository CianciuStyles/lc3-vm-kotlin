package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import kotlin.experimental.inv

@ExperimentalUnsignedTypes
class Not(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = (encoding.toInt() shr 9 and 0x7).toUShort()
    val sourceRegister: UShort = (encoding.toInt() shr 6 and 0x7).toUShort()

    override fun execute(memory: Memory, registers: Registers) {
        storeAndSetConditionCodes(
            registers,
            destinationRegister,
            registers[sourceRegister].inv()
        )
    }
}

package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.Utils.extendSign

@ExperimentalUnsignedTypes
class Add(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = (encoding.toInt() shr 9 and 0x7).toUShort()
    val sourceRegister1: UShort = (encoding.toInt() shr 6 and 0x7).toUShort()
    val mode: Mode
    val sourceRegister2: UShort?
    val immediateValue: Short?

    init {
        mode = Mode.valueOf(encoding.toInt() shr 5 and 0x1)
        if (mode == Mode.REGISTER_MODE) {
            sourceRegister2 = (encoding.toInt() and 0x7).toUShort()
            immediateValue = null
        } else {
            sourceRegister2 = null
            immediateValue = extendSign(encoding.toInt() and 0b11111, 5)
        }
    }

    enum class Mode {
        REGISTER_MODE,
        IMMEDIATE_MODE;

        companion object {
            fun valueOf(value: Int): Mode {
                return values()[value]
            }
        }
    }

    override fun execute(memory: Memory, registers: Registers) {
        val secondOperand: Short = if (mode == Mode.REGISTER_MODE) {
            registers[sourceRegister2!!]
        } else {
            immediateValue!!
        }

        val result = (registers[sourceRegister1] + secondOperand).toShort()
        registers[destinationRegister] = result
        registers.setCond(result)
    }
}

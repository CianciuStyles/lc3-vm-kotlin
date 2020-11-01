package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.Utils.extendSign

@ExperimentalUnsignedTypes
class JumpToSubroutine(val encoding: UShort) : Instruction() {
    val mode: Mode
    val baseRegister: UShort?
    val pcOffset11: Short?

    init {
        mode = Mode.valueOf(encoding.toInt() shr 11 and 0b1)
        if (mode == Mode.REGISTER_MODE) {
            baseRegister = (encoding.toInt() shr 6 and 0b111).toUShort()
            pcOffset11 = null
        } else {
            baseRegister = null
            pcOffset11 = extendSign(encoding.toInt() and 0x1FF, 11)
        }
    }

    override fun execute(memory: Memory, registers: Registers) {
        registers[7u] = registers.getPC()
        if (mode == Mode.REGISTER_MODE) {
            registers.setPC(registers[baseRegister!!])
        } else {
            registers.setPC((registers.getPC() + pcOffset11!!).toShort())
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
}

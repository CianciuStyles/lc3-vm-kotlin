package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import io.github.cianciustyles.extensions.shr
import kotlin.experimental.and

@ExperimentalUnsignedTypes
class And(val encoding: UShort) : Instruction() {
    val destinationRegister: UShort = encoding shr 9 and 0x7u
    val sourceRegister1: UShort = encoding shr 6 and 0x7u
    val mode: Mode
    val sourceRegister2: UShort?
    val immediateValue: Short?

    init {
        mode = Mode.valueOf(encoding shr 5 and 0x1u)
        if (mode == Mode.REGISTER_MODE) {
            sourceRegister2 = encoding and 0x7u
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
            fun valueOf(value: UShort): Mode {
                return values()[value.toInt()]
            }
        }
    }

    override fun execute(vm: LC3VM) {
        val secondOperand = if (mode == Mode.REGISTER_MODE) {
            vm.registers[sourceRegister2!!]
        } else {
            immediateValue!!
        }

        storeAndSetConditionCodes(
            vm,
            destinationRegister,
            vm.registers[sourceRegister1] and secondOperand
        )
    }
}

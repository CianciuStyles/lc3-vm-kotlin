package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import io.github.cianciustyles.extensions.addShort
import io.github.cianciustyles.extensions.shr

@ExperimentalUnsignedTypes
class JumpToSubroutine(val encoding: UShort) : Instruction() {
    val mode: Mode
    val baseRegister: UShort?
    val pcOffset11: Short?

    init {
        mode = Mode.valueOf(encoding shr 11 and 0x1u)
        if (mode == Mode.REGISTER_MODE) {
            baseRegister = encoding shr 6 and 0x7u
            pcOffset11 = null
        } else {
            baseRegister = null
            pcOffset11 = extendSign(encoding.toInt() and 0x7FF, 11)
        }
    }

    override fun execute(vm: LC3VM) {
        vm.registers[7u] = vm.registers.getPC()
        if (mode == Mode.REGISTER_MODE) {
            vm.registers.setPC(vm.registers[baseRegister!!])
        } else {
            vm.registers.setPC(vm.registers.getPC() addShort pcOffset11!!)
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
}

package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.extensions.shr

@ExperimentalUnsignedTypes
class Jump(encoding: UShort) : Instruction() {
    private val baseRegister: UShort = encoding shr 6 and 0x7u

    override fun execute(vm: LC3VM) {
        vm.registers.setPC(vm.registers[baseRegister])
    }
}

package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM

@ExperimentalUnsignedTypes
class Jump(encoding: UShort) : Instruction() {
    val baseRegister: UShort = (encoding.toInt() shr 6 and 0x7).toUShort()
    override fun execute(vm: LC3VM) {
        vm.registers.setPC(vm.registers[baseRegister])
    }
}

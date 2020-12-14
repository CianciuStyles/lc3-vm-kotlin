package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils
import io.github.cianciustyles.extensions.shr

@ExperimentalUnsignedTypes
class Store(encoding: UShort) : Instruction() {
    val sourceRegister: UShort = encoding shr 9 and 0x7u
    val pcOffset: Short = Utils.extendSign(encoding.toInt() and 0x1FF, 9)

    override fun execute(vm: LC3VM) {
        store(
            vm,
            (vm.registers.getPC() + pcOffset).toUShort(),
            vm.registers[sourceRegister]
        )
    }
}

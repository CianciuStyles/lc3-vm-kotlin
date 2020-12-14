package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import io.github.cianciustyles.extensions.shr

@ExperimentalUnsignedTypes
class StoreBaseOffset(val encoding: UShort) : Instruction() {
    val sourceRegister: UShort = encoding shr 9 and 0x7u
    val baseRegister: UShort = encoding shr 6 and 0x7u
    val offset6: Short = extendSign(encoding.toInt() and 0b111111, 6)

    override fun execute(vm: LC3VM) {
        store(
            vm,
            (vm.registers[baseRegister] + offset6).toUShort(),
            vm.registers[sourceRegister]
        )
    }
}

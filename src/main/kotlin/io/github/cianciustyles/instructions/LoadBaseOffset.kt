package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import io.github.cianciustyles.extensions.shr

@ExperimentalUnsignedTypes
class LoadBaseOffset(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = encoding shr 9 and 0x7u
    val baseRegister: UShort = encoding shr 6 and 0x7u
    val offset6: Short = extendSign(encoding.toInt() and 0x3F, 6)

    override fun execute(vm: LC3VM) {
        loadAndSetConditionCodes(
            vm,
            (vm.registers[baseRegister] + offset6).toUShort(),
            destinationRegister
        )
    }
}

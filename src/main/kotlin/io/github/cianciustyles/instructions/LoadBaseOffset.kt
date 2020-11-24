package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign

@ExperimentalUnsignedTypes
class LoadBaseOffset(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = (encoding.toInt() shr 9 and 0x7).toUShort()
    val baseRegister: UShort = (encoding.toInt() shr 6 and 0x7).toUShort()
    val offset6: Short = extendSign(encoding.toInt() and 0x3F, 6)

    override fun execute(vm: LC3VM) {
        loadAndSetConditionCodes(
            vm,
            (vm.registers[baseRegister] + offset6).toUShort(),
            destinationRegister
        )
    }
}

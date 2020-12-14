package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import io.github.cianciustyles.extensions.shr

@ExperimentalUnsignedTypes
class Load(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = encoding shr 9 and 0x7u
    val pcOffset: Short = extendSign(encoding.toInt() and 0x1FF, 9)

    override fun execute(vm: LC3VM) =
        loadAndSetConditionCodes(
            vm,
            (vm.registers.getPC() + pcOffset).toUShort(),
            destinationRegister
        )
}

package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import io.github.cianciustyles.extensions.addShort
import io.github.cianciustyles.extensions.shr

@ExperimentalUnsignedTypes
class LoadEffectiveAddress(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = encoding shr 9 and 0x7u
    val pcOffset9: Short = extendSign(encoding.toInt() and 0x1FF, 9)

    override fun execute(vm: LC3VM) {
        storeAndSetConditionCodes(
            vm,
            destinationRegister,
            vm.registers.getPC() addShort pcOffset9
        )
    }
}

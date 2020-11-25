package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import io.github.cianciustyles.Utils.shortPlus

@ExperimentalUnsignedTypes
class LoadEffectiveAddress(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = (encoding.toInt() shr 9 and 0x7).toUShort()
    val pcOffset9: Short = extendSign(encoding.toInt() and 0x1FF, 9)

    override fun execute(vm: LC3VM) {
        storeAndSetConditionCodes(
            vm,
            destinationRegister,
            shortPlus(vm.registers.getPC(), pcOffset9)
        )
    }
}

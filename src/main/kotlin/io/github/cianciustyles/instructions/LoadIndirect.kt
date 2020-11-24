package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign

@ExperimentalUnsignedTypes
class LoadIndirect(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = (encoding.toInt() shr 9 and 0x7).toUShort()
    val pcOffset: Short = extendSign(encoding.toInt() and 0x1FF, 9)

    override fun execute(vm: LC3VM) {
        val newOffset: UShort = (vm.registers.getPC() + pcOffset).toUShort()
        val addressToLoad: UShort = vm.memory[newOffset].toUShort()
        loadAndSetConditionCodes(
            vm,
            addressToLoad,
            destinationRegister
        )
    }
}

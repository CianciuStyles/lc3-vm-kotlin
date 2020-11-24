package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import kotlin.experimental.inv

@ExperimentalUnsignedTypes
class Not(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = (encoding.toInt() shr 9 and 0x7).toUShort()
    val sourceRegister: UShort = (encoding.toInt() shr 6 and 0x7).toUShort()

    override fun execute(vm: LC3VM) {
        storeAndSetConditionCodes(
            vm,
            destinationRegister,
            vm.registers[sourceRegister].inv()
        )
    }
}

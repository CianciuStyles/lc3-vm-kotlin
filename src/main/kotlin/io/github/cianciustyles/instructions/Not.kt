package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.extensions.shr
import kotlin.experimental.inv

@ExperimentalUnsignedTypes
class Not(encoding: UShort) : Instruction() {
    val destinationRegister: UShort = encoding shr 9 and 0x7u
    val sourceRegister: UShort = encoding shr 6 and 0x7u

    override fun execute(vm: LC3VM) {
        storeAndSetConditionCodes(
            vm,
            destinationRegister,
            vm.registers[sourceRegister].inv()
        )
    }
}

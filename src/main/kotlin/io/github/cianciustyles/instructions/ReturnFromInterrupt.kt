package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.exceptions.PrivilegeModeException

@ExperimentalUnsignedTypes
class ReturnFromInterrupt(val encoding: UShort) : Instruction() {
    override fun execute(vm: LC3VM) {
        throw PrivilegeModeException()
    }
}

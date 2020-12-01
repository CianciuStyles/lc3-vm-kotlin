package io.github.cianciustyles.instructions

import io.github.cianciustyles.ConditionFlags
import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign

@ExperimentalUnsignedTypes
class Branch(val encoding: UShort) : Instruction() {
    val negative: Boolean = (encoding.toInt() shr 11) and 1 == 1
    val zero: Boolean = (encoding.toInt() shr 10) and 1 == 1
    val positive: Boolean = (encoding.toInt() shr 9) and 1 == 1
    val pcOffset9: Short = extendSign(encoding.toInt() and 0x1FF, 9)

    override fun execute(vm: LC3VM) {
        val branchOnNegative = negative and (vm.registers.getCond() == ConditionFlags.NEGATIVE.value)
        val branchOnZero = zero and (vm.registers.getCond() == ConditionFlags.ZERO.value)
        val branchOnPositive = positive and (vm.registers.getCond() == ConditionFlags.POSITIVE.value)

        if (branchOnNegative or branchOnZero or branchOnPositive) {
            val newPC = vm.registers.getPC() + pcOffset9
            vm.registers.setPC(newPC.toShort())
        }
    }
}

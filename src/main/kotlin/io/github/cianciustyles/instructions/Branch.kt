package io.github.cianciustyles.instructions

import io.github.cianciustyles.ConditionFlags
import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.Utils.extendSign

@ExperimentalUnsignedTypes
class Branch(val encoding: UShort) : Instruction() {
    val negative: Boolean = (encoding.toInt() shr 11) == 1
    val zero: Boolean = (encoding.toInt() shr 10) == 1
    val positive: Boolean = (encoding.toInt() shr 9) == 1
    val pcOffset9: Short = extendSign(encoding.toInt() and 0x1FF, 9)

    override fun execute(memory: Memory, registers: Registers) {
        val branchOnNegative = negative and (registers.getCond() == ConditionFlags.NEGATIVE.value)
        val branchOnZero = zero and (registers.getCond() == ConditionFlags.ZERO.value)
        val branchOnPositive = positive and (registers.getCond() == ConditionFlags.POSITIVE.value)

        if (branchOnNegative or branchOnZero or branchOnPositive) {
            val newPC = registers.getPC() + pcOffset9
            registers.setPC(newPC.toShort())
        }
    }
}

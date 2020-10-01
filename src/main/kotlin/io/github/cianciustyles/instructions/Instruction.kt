package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import io.github.cianciustyles.exceptions.UnrecognisedInstructionException

@ExperimentalUnsignedTypes
abstract class Instruction {
    companion object {
        fun fetch(encoding: UShort): Instruction {
            return when (encoding.toInt() shr 12) {
                0b0001 -> Add(encoding)
                0b1010 -> LoadIndirect(encoding)
                else -> throw UnrecognisedInstructionException()
            }
        }
    }

    abstract fun execute(memory: Memory, registers: Registers)

    fun extendSign(x: Int, bitCount: Int): Short {
        val num = if (x shr bitCount - 1 and 0b1 == 1) {
            (0xFFFF shl bitCount) or x
        } else {
            x
        }

        return num.toShort()
    }
}

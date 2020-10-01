package io.github.cianciustyles.instructions

import io.github.cianciustyles.Registers
import io.github.cianciustyles.exceptions.UnrecognisedInstructionException

@ExperimentalUnsignedTypes
abstract class Instruction {
    companion object {
        fun fetch(encoding: UShort): Instruction {
            return when (encoding.toInt() shr 12) {
                0b0001 -> Add(encoding)
                else -> throw UnrecognisedInstructionException()
            }
        }
    }

    abstract fun execute(registers: Registers)
}

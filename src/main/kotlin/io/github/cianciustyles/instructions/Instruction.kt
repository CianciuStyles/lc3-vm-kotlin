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
                0b0010 -> Load(encoding)
                0b0110 -> LoadBaseOffset(encoding)
                0b1010 -> LoadIndirect(encoding)
                0b1110 -> LoadEffectiveAddress(encoding)
                else -> throw UnrecognisedInstructionException()
            }
        }
    }

    abstract fun execute(memory: Memory, registers: Registers)

    fun loadAndSetConditionCodes(
        memory: Memory,
        registers: Registers,
        sourceAddress: UShort,
        destinationRegister: UShort
    ) {
        val valueToCopy = memory[sourceAddress]
        registers[destinationRegister] = valueToCopy
        registers.setCond(valueToCopy)
    }
}

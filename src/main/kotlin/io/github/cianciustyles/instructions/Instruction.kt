package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.exceptions.UnrecognisedInstructionException

@ExperimentalUnsignedTypes
abstract class Instruction {
    companion object {
        fun fetch(encoding: UShort): Instruction {
            return when (encoding.toInt() shr 12) {
                0b0000 -> Branch(encoding)
                0b0001 -> Add(encoding)
                0b0010 -> Load(encoding)
                0b0011 -> Store(encoding)
                0b0100 -> JumpToSubroutine(encoding)
                0b0101 -> And(encoding)
                0b0110 -> LoadBaseOffset(encoding)
                0b0111 -> StoreBaseOffset(encoding)
                0b1000 -> ReturnFromInterrupt(encoding)
                0b1001 -> Not(encoding)
                0b1010 -> LoadIndirect(encoding)
                0b1011 -> StoreIndirect(encoding)
                0b1100 -> Jump(encoding)
                0b1110 -> LoadEffectiveAddress(encoding)
                0b1111 -> Trap(encoding)
                else -> throw UnrecognisedInstructionException()
            }
        }
    }

    abstract fun execute(vm: LC3VM)

    fun loadAndSetConditionCodes(
        vm: LC3VM,
        sourceAddress: UShort,
        destinationRegister: UShort
    ) {
        val valueToCopy = vm.memory[sourceAddress]
        vm.registers[destinationRegister] = valueToCopy
        vm.registers.setCond(valueToCopy)
    }

    fun storeAndSetConditionCodes(
        vm: LC3VM,
        destinationRegister: UShort,
        value: Short
    ) {
        vm.registers[destinationRegister] = value
        vm.registers.setCond(value)
    }

    fun store(
        vm: LC3VM,
        address: UShort,
        value: Short
    ) {
        vm.memory[address] = value
    }
}

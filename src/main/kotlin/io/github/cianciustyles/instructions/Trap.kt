package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils
import io.github.cianciustyles.Utils.shortPlus
import io.github.cianciustyles.exceptions.UnrecognisedTrapException
import java.util.Scanner
import kotlin.experimental.and

@ExperimentalUnsignedTypes
class Trap(
    val encoding: UShort,
    val readCharacter: (Boolean) -> Short = Utils::readCharacter,
    private val scanner: Scanner = Scanner(System.`in`)
) : Instruction() {
    companion object {
        const val GETC = 0x20
        const val OUT = 0x21
        const val PUTS = 0x22
        const val IN = 0x23
        const val PUTSP = 0x24
        const val HALT = 0x25
    }

    override fun execute(vm: LC3VM) =
        when (encoding.toInt() and 0xFF) {
            GETC -> getc(vm)
            OUT -> out(vm)
            PUTS -> puts(vm)
            IN -> `in`(vm)
            HALT -> halt(vm)
            else -> throw UnrecognisedTrapException()
        }

    private fun getc(vm: LC3VM) {
        vm.registers[0u] = readCharacter.invoke(true)
    }

    private fun out(vm: LC3VM) {
        val characterToPrint = vm.registers[0u]
        print(characterToPrint.toChar())
        System.out.flush()
    }

    private fun puts(vm: LC3VM) {
        var startAddress = vm.registers[0u]
        var currentCharacter = vm.memory[startAddress.toUShort()]
        while (currentCharacter > 0) {
            print(currentCharacter.toChar())
            startAddress = shortPlus(startAddress, 1)
            currentCharacter = vm.memory[startAddress.toUShort()]
        }
        System.out.flush()
    }

    private fun `in`(vm: LC3VM) {
        print("Enter a character: ")
        System.out.flush()

        val character = scanner.next().single()
        print(character)
        System.out.flush()

        vm.registers[0u] = character.toShort() and 0xFF
    }

    private fun halt(vm: LC3VM) {
        println("HALT")
        vm.running = false
    }
}

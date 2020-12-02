package io.github.cianciustyles

import io.github.cianciustyles.Utils.shortPlus
import io.github.cianciustyles.exceptions.NoCommandLineArgumentsException
import io.github.cianciustyles.instructions.Instruction
import java.io.File
import kotlin.experimental.and
import kotlin.experimental.or

@ExperimentalUnsignedTypes
class LC3VM(
    val memory: Memory = Memory(),
    val registers: Registers = Registers(),
    var running: Boolean = false,
    var pcStart: Short = 0x3000
) {
    fun run(vararg args: String) {
        loadParameters(*args)

        registers.setPC(pcStart)
        running = true
        while (running) {
            val encoding = memory[registers.getPC().toUShort()].toUShort()
            registers.setPC(shortPlus(registers.getPC(), 1))
            Instruction.fetch(encoding).execute(this)
        }
    }

    private fun loadParameters(vararg args: String) {
        if (args.isEmpty())
            throw NoCommandLineArgumentsException("Too few arguments")

        // read image
        val program = File(args[0]).readBytes()
        pcStart = combineBytes(program[0], program[1])

        var currentAddress = pcStart
        for (index in 2 until program.size step 2) {
            memory[currentAddress.toUShort()] = combineBytes(program[index], program[index + 1])
            currentAddress = shortPlus(currentAddress, 1)
        }
    }

    private fun combineBytes(firstByte: Byte, secondByte: Byte): Short {
        return (firstByte.toInt() shl 8).toShort() or (secondByte.toShort() and 0xFF)
    }
}

package io.github.cianciustyles

import io.github.cianciustyles.exceptions.NoCommandLineArgumentsException

@ExperimentalUnsignedTypes
class LC3VM(
    val registers: Registers = Registers()
) {
    fun run(vararg args: String) {
        loadParameters(*args)
        // Setup

        var running = true
        while (running) {
            // fetch instruction
            running = false
        }

        // Shutdown
    }

    private fun loadParameters(vararg args: String) {
        if (args.isEmpty())
            throw NoCommandLineArgumentsException("Too few arguments")

        // read images
    }
}

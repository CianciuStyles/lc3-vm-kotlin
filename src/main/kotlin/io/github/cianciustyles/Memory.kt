package io.github.cianciustyles

@ExperimentalUnsignedTypes
class Memory(
    private val memory: ShortArray = ShortArray(UShort.MAX_VALUE.toInt())
) {
    companion object {
        const val KBSR = 0xFE00
        const val KBDR = 0xFE02
    }

    operator fun get(index: UShort): Short =
        memory[index.toInt()]

    operator fun set(index: UShort, value: Short) {
        memory[index.toInt()] = value
    }
}

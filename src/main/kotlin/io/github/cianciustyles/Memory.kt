package io.github.cianciustyles

import io.github.cianciustyles.Utils.readCharacter

@ExperimentalUnsignedTypes
class Memory(
    private val memory: ShortArray = ShortArray(UShort.MAX_VALUE.toInt())
) {
    companion object {
        const val KBSR = 0xFE00
        const val KBDR = 0xFE02
    }

    operator fun get(index: UShort): Short {
        if (index == KBSR.toUShort()) {
            val characterRead = readCharacter(false)
            if (characterRead >= 0) {
                memory[KBSR] = (1 shl 15).toShort()
                memory[KBDR] = characterRead
            }
        }

        return memory[index.toInt()]
    }

    operator fun set(index: UShort, value: Short) {
        memory[index.toInt()] = value
    }
}

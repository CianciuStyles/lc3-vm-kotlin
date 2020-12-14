package io.github.cianciustyles

import biz.source_code.utils.RawConsoleInput

object Utils {
    fun extendSign(x: Int, bitCount: Int): Short {
        val num = if (x shr bitCount - 1 and 0b1 == 1) {
            (0xFFFF shl bitCount) or x
        } else {
            x
        }

        return num.toShort()
    }

    fun readCharacter(wait: Boolean): Short =
        when (val characterRead = RawConsoleInput.read(wait)) {
            13 -> 10
            else -> characterRead.toShort()
        }
}

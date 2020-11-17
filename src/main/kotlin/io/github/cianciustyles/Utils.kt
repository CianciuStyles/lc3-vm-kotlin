package io.github.cianciustyles

object Utils {
    fun extendSign(x: Int, bitCount: Int): Short {
        val num = if (x shr bitCount - 1 and 0b1 == 1) {
            (0xFFFF shl bitCount) or x
        } else {
            x
        }

        return num.toShort()
    }

    fun shortPlus(short1: Short, short2: Short): Short =
        (short1 + short2).toShort()
}

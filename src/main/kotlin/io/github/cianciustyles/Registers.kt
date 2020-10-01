package io.github.cianciustyles

@ExperimentalUnsignedTypes
class Registers {
    private val registers: ShortArray = ShortArray(16)

    operator fun get(index: UShort): Short {
        return registers[index.toInt()]
    }

    operator fun set(index: UShort, value: Short) {
        registers[index.toInt()] = value
    }

    fun setCondRegister(value: Short) {
        val newFlag = when {
            value > 0 -> {
                ConditionFlags.POSITIVE
            }
            value < 0 -> {
                ConditionFlags.NEGATIVE
            }
            else -> {
                ConditionFlags.ZERO
            }
        }
        registers[RegistersNames.COND.ordinal] = newFlag.value
    }

    fun getCondRegister(): Short =
        registers[RegistersNames.COND.ordinal]

    enum class RegistersNames {
        R0,
        R1,
        R2,
        R3,
        R4,
        R5,
        R6,
        R7,
        PC,
        COND;
    }
}

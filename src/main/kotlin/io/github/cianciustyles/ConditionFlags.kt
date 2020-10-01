package io.github.cianciustyles

enum class ConditionFlags(val value: Short) {
    POSITIVE(1 shl 0),
    ZERO(1 shl 1),
    NEGATIVE(1 shl 2);
}

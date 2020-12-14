package io.github.cianciustyles.extensions

@ExperimentalUnsignedTypes
infix fun UShort.and(other: Int): UShort =
        (this and other.toUShort())

@ExperimentalUnsignedTypes
infix fun UShort.shr(numShifts: Int): UShort =
        (this.toUInt() shr numShifts).toUShort()


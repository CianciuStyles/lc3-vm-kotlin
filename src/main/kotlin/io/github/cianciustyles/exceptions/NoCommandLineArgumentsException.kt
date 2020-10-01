package io.github.cianciustyles.exceptions

import java.lang.IllegalArgumentException

class NoCommandLineArgumentsException(
    override val message: String
) : IllegalArgumentException(message)

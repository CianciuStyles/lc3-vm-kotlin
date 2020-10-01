package io.github.cianciustyles

import io.github.cianciustyles.exceptions.NoCommandLineArgumentsException
import org.junit.Test

@ExperimentalUnsignedTypes
class LC3VMTest {
    @Test(expected = NoCommandLineArgumentsException::class)
    fun itThrowsAnExceptionForNoParameters() {
        // given
        val vm = LC3VM()

        // when
        vm.run()

        // then - throw exception
    }
}

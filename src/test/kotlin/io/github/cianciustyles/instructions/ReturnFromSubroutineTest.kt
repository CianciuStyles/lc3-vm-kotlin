package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@ExperimentalUnsignedTypes
class ReturnFromSubroutineTest {
    @Test
    fun testReturnFromSubroutine() {
        // given
        val memory = Memory()
        val registers = Registers()
        registers.setPC(730)
        registers[7u] = 480

        // when
        val returnFromSubroutine = ReturnFromSubroutine()
        returnFromSubroutine.execute(memory, registers)

        // then
        assertThat(registers.getPC()).isEqualTo(registers[7u])
    }
}

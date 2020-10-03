package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@ExperimentalUnsignedTypes
class JumpTest {
    @Test
    fun testJump() {
        // given
        val memory = Memory()
        val registers = Registers()

        registers.setPC(730)
        val baseRegister = 2
        registers[baseRegister.toUShort()] = 480

        val encoding = ((0b1100 shl 12) or (baseRegister shl 6)).toUShort()

        // when
        val jump = Jump(encoding)
        jump.execute(memory, registers)

        // then
        assertThat(registers.getPC()).isEqualTo(registers[baseRegister.toUShort()])
    }

    @Test
    fun testReturnFromSubroutine() {
        // given
        val memory = Memory()
        val registers = Registers()

        registers.setPC(200)
        val baseRegister = 7
        registers[baseRegister.toUShort()] = 500

        val encoding = ((0b1100 shl 12) or (baseRegister shl 6)).toUShort()

        // when
        val returnFromSubroutine = Jump(encoding)
        returnFromSubroutine.execute(memory, registers)

        // then
        assertThat(registers.getPC()).isEqualTo(registers[baseRegister.toUShort()])
    }
}

package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class JumpTest {
    private lateinit var vm: LC3VM

    @Before
    fun setUp() {
        vm = LC3VM(running = true)
    }

    @Test
    fun testJump() {
        // given
        vm.registers.setPC(730)
        val baseRegister = 2
        vm.registers[baseRegister.toUShort()] = 480

        val encoding = ((0b1100 shl 12) or (baseRegister shl 6)).toUShort()

        // when
        val jump = Jump(encoding)
        jump.execute(vm)

        // then
        assertThat(vm.registers.getPC()).isEqualTo(vm.registers[baseRegister.toUShort()])
    }

    @Test
    fun testReturnFromSubroutine() {
        // given
        vm.registers.setPC(200)
        val baseRegister = 7
        vm.registers[baseRegister.toUShort()] = 500

        val encoding = ((0b1100 shl 12) or (baseRegister shl 6)).toUShort()

        // when
        val returnFromSubroutine = Jump(encoding)
        returnFromSubroutine.execute(vm)

        // then
        assertThat(vm.registers.getPC()).isEqualTo(vm.registers[baseRegister.toUShort()])
    }
}

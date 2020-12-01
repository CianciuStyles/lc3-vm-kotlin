package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import io.github.cianciustyles.Utils.shortPlus
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class BranchTest {
    private lateinit var vm: LC3VM

    @Before
    fun setUp() {
        vm = LC3VM(running = true)
    }

    @Test
    fun testBranchOnNegative() {
        // given
        val pcOffset9 = 0x130
        val pcOffset9WithSignExtended = extendSign(pcOffset9, 9)
        val encoding = encodeBranch(n = 1, z = 0, p = 0, pcOffset9 = pcOffset9)

        val initialPC: Short = 0x180
        vm.registers.setPC(initialPC)
        vm.registers.setCond(-1)

        // when
        val branch = Branch(encoding)
        branch.execute(vm)

        // then
        assertThat(branch.negative).isEqualTo(true)
        assertThat(branch.zero).isEqualTo(false)
        assertThat(branch.positive).isEqualTo(false)
        assertThat(branch.pcOffset9).isEqualTo(pcOffset9WithSignExtended)
        assertThat(vm.registers.getPC()).isEqualTo(shortPlus(initialPC, pcOffset9WithSignExtended))
    }

    @Test
    fun testDoesNotBranchOnNegative() {
        // given
        val pcOffset9 = 0x180
        val pcOffset9WithSignExtended = extendSign(pcOffset9, 9)
        val encoding = encodeBranch(n = 1, z = 0, p = 0, pcOffset9 = pcOffset9)

        val initialPC: Short = 0x140
        vm.registers.setPC(initialPC)
        vm.registers.setCond(1)

        // when
        val branch = Branch(encoding)
        branch.execute(vm)

        // then
        assertThat(branch.negative).isEqualTo(true)
        assertThat(branch.zero).isEqualTo(false)
        assertThat(branch.positive).isEqualTo(false)
        assertThat(branch.pcOffset9).isEqualTo(pcOffset9WithSignExtended)
        assertThat(vm.registers.getPC()).isEqualTo(initialPC)
    }

    @Test
    fun testBranchOnZero() {
        // given
        val pcOffset9 = 0x140
        val pcOffset9WithSignExtended = extendSign(pcOffset9, 9)
        val encoding = encodeBranch(n = 0, z = 1, p = 0, pcOffset9 = pcOffset9)

        val initialPC: Short = 0x180
        vm.registers.setPC(initialPC)
        vm.registers.setCond(0)

        // when
        val branch = Branch(encoding)
        branch.execute(vm)

        // then
        assertThat(branch.negative).isEqualTo(false)
        assertThat(branch.zero).isEqualTo(true)
        assertThat(branch.positive).isEqualTo(false)
        assertThat(branch.pcOffset9).isEqualTo(pcOffset9WithSignExtended)
        assertThat(vm.registers.getPC()).isEqualTo(shortPlus(initialPC, pcOffset9WithSignExtended))
    }

    @Test
    fun testDoesNotBranchOnZero() {
        // given
        val pcOffset9 = 0x180
        val pcOffset9WithSignExtended = extendSign(pcOffset9, 9)
        val encoding = encodeBranch(n = 0, z = 1, p = 0, pcOffset9 = pcOffset9)

        val initialPC: Short = 0x140
        vm.registers.setPC(initialPC)
        vm.registers.setCond(15)

        // when
        val branch = Branch(encoding)
        branch.execute(vm)

        // then
        assertThat(branch.negative).isEqualTo(false)
        assertThat(branch.zero).isEqualTo(true)
        assertThat(branch.positive).isEqualTo(false)
        assertThat(branch.pcOffset9).isEqualTo(pcOffset9WithSignExtended)
        assertThat(vm.registers.getPC()).isEqualTo(initialPC)
    }

    @Test
    fun testBranchOnPositive() {
        // given
        val pcOffset9 = 0x350
        val pcOffset9WithSignExtended = extendSign(pcOffset9, 9)
        val encoding = encodeBranch(n = 0, z = 0, p = 1, pcOffset9 = pcOffset9)

        val initialPC: Short = 0x180
        vm.registers.setPC(initialPC)
        vm.registers.setCond(46)

        // when
        val branch = Branch(encoding)
        branch.execute(vm)

        // then
        assertThat(branch.negative).isEqualTo(false)
        assertThat(branch.zero).isEqualTo(false)
        assertThat(branch.positive).isEqualTo(true)
        assertThat(branch.pcOffset9).isEqualTo(pcOffset9WithSignExtended)
        assertThat(vm.registers.getPC()).isEqualTo(shortPlus(initialPC, pcOffset9WithSignExtended))
    }

    @Test
    fun testDoesNotBranchOnPositive() {
        // given
        val pcOffset9 = 0x180
        val pcOffset9WithSignExtended = extendSign(pcOffset9, 9)
        val encoding = encodeBranch(n = 0, z = 0, p = 1, pcOffset9 = pcOffset9)

        val initialPC: Short = 0x140
        vm.registers.setPC(initialPC)
        vm.registers.setCond(0)

        // when
        val branch = Branch(encoding)
        branch.execute(vm)

        // then
        assertThat(branch.negative).isEqualTo(false)
        assertThat(branch.zero).isEqualTo(false)
        assertThat(branch.positive).isEqualTo(true)
        assertThat(branch.pcOffset9).isEqualTo(pcOffset9WithSignExtended)
        assertThat(vm.registers.getPC()).isEqualTo(initialPC)
    }

    private fun encodeBranch(n: Int, z: Int, p: Int, pcOffset9: Int): UShort =
        ((n shl 11) or (z shl 10) or (p shl 9) or pcOffset9).toUShort()
}

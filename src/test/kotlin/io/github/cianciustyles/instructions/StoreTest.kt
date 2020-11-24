package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class StoreTest {
    private lateinit var vm: LC3VM

    @Before
    fun setup() {
        vm = LC3VM(running = true)
    }

    @Test
    fun testStore() {
        // given
        val sourceRegister = 5
        val pcOffset = 140 and 0x1FF
        val encoding = encode(sourceRegister, pcOffset)

        val initialValue: Short = 750
        vm.registers.setPC(initialValue)
        val expectedResult: Short = 12
        vm.registers[sourceRegister.toUShort()] = expectedResult

        // when
        val store = Store(encoding)
        store.execute(vm)

        // then
        assertThat(store.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(store.pcOffset).isEqualTo(pcOffset.toShort())
        assertThat(vm.memory[(initialValue + pcOffset).toUShort()]).isEqualTo(expectedResult)
    }

    private fun encode(sourceRegister: Int, pcOffset: Int) =
        ((0b0011 shl 12) or (sourceRegister shl 9) or pcOffset).toUShort()
}

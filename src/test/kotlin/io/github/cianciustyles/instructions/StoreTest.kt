package io.github.cianciustyles.instructions

import io.github.cianciustyles.Memory
import io.github.cianciustyles.Registers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class StoreTest {
    private lateinit var memory: Memory
    private lateinit var registers: Registers

    @Before
    fun setup() {
        memory = Memory()
        registers = Registers()
    }

    @Test
    fun testStore() {
        // given
        val sourceRegister = 5
        val pcOffset = 140 and 0x1FF
        val encoding = encode(sourceRegister, pcOffset)

        val initialValue: Short = 750
        registers.setPC(initialValue)
        val expectedResult: Short = 12
        registers[sourceRegister.toUShort()] = expectedResult

        // when
        val store = Store(encoding)
        store.execute(memory, registers)

        // then
        assertThat(store.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(store.pcOffset).isEqualTo(pcOffset.toShort())
        assertThat(memory[(initialValue + pcOffset).toUShort()]).isEqualTo(expectedResult)
    }

    private fun encode(sourceRegister: Int, pcOffset: Int) =
        ((0b0011 shl 12) or (sourceRegister shl 9) or pcOffset).toUShort()
}

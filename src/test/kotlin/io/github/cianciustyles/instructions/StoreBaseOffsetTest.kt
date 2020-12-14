package io.github.cianciustyles.instructions

import io.github.cianciustyles.LC3VM
import io.github.cianciustyles.Utils.extendSign
import io.github.cianciustyles.extensions.addShort
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalUnsignedTypes
class StoreBaseOffsetTest {
    private lateinit var vm: LC3VM

    @Before
    fun setup() {
        vm = LC3VM(running = true)
    }

    @Test
    fun testStoreBaseOffsetPositiveOffset() {
        // given
        val sourceRegister = 2
        val baseRegister = 5
        val offset6 = 15
        val offset6WithSignExtended = extendSign(offset6, 6)
        val encoding = encode(sourceRegister, baseRegister, offset6)

        val expectedResult: Short = 123
        vm.registers[sourceRegister.toUShort()] = expectedResult
        val baseRegisterValue: Short = 0x400
        vm.registers[baseRegister.toUShort()] = baseRegisterValue

        // when
        val storeBaseOffset = StoreBaseOffset(encoding)
        storeBaseOffset.execute(vm)

        // then
        assertThat(storeBaseOffset.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(storeBaseOffset.baseRegister).isEqualTo(baseRegister.toUShort())
        assertThat(storeBaseOffset.offset6).isEqualTo(offset6WithSignExtended)
        val storeMemoryAddress = (baseRegisterValue addShort offset6WithSignExtended).toUShort()
        assertThat(vm.memory[storeMemoryAddress]).isEqualTo(expectedResult)
    }

    @Test
    fun testStoreBaseOffsetNegativeOffset() {
        // given
        val sourceRegister = 4
        val baseRegister = 1
        val offset6 = 0b111111 // -1 in 6 bits
        val offset6WithSignExtended = extendSign(offset6, 6)
        val encoding = encode(sourceRegister, baseRegister, offset6)

        val expectedResult: Short = 456
        vm.registers[sourceRegister.toUShort()] = expectedResult
        val baseRegisterValue: Short = 0x280
        vm.registers[baseRegister.toUShort()] = baseRegisterValue

        // when
        val storeBaseOffset = StoreBaseOffset(encoding)
        storeBaseOffset.execute(vm)

        // then
        assertThat(storeBaseOffset.sourceRegister).isEqualTo(sourceRegister.toUShort())
        assertThat(storeBaseOffset.baseRegister).isEqualTo(baseRegister.toUShort())
        assertThat(storeBaseOffset.offset6).isEqualTo(offset6WithSignExtended)
        val storeMemoryAddress = (baseRegisterValue addShort offset6WithSignExtended).toUShort()
        assertThat(vm.memory[storeMemoryAddress]).isEqualTo(expectedResult)
    }

    private fun encode(sourceRegister: Int, baseRegister: Int, offset6: Int) =
        ((0b1011 shl 12) or (sourceRegister shl 9) or (baseRegister shl 6) or offset6).toUShort()
}

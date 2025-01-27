package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)


    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should add cereal to empty container`() {
        val amount = storage.addCereal(Cereal.RICE, 5f)
        assertEquals(0f, amount, 0.01f)
        assertEquals(5f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should add cereal to existing container`() {
        storage.addCereal(Cereal.RICE, 5f)
        val amount = storage.addCereal(Cereal.RICE, 3f)
        assertEquals(0f, amount, 0.01f)
        assertEquals(8f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should return remaining amount when container is full`() {
        storage.addCereal(Cereal.RICE, 7f)
        val amount = storage.addCereal(Cereal.RICE, 5f)
        assertEquals(2f, amount, 0.01f)
        assertEquals(10f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should throw when amount is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.RICE, -5f)
        }
    }

    @Test
    fun `should throw when storage is full for new cereal type`() {
        storage.addCereal(Cereal.RICE, 10f)
        storage.addCereal(Cereal.BUCKWHEAT, 10f)

        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.MILLET, 5f)
        }
    }

    @Test
    fun `should get cereal from container`() {
        storage.addCereal(Cereal.RICE, 8f)
        val amount = storage.getCereal(Cereal.RICE, 3f)
        assertEquals(3f, amount, 0.01f)
        assertEquals(5f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should return available amount when requesting more than available`() {
        storage.addCereal(Cereal.RICE, 5f)
        val amount = storage.getCereal(Cereal.RICE, 8f)
        assertEquals(5f, amount, 0.01f)
        assertEquals(0f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should return zero for non-existent cereal`() {
        val amount = storage.getCereal(Cereal.RICE, 5f)
        assertEquals(0f, amount, 0.01f)
    }

    @Test
    fun `should throw when requesting negative amount in getCereal`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.RICE, -5f)
        }
    }

    @Test
    fun `should remove empty container`() {
        storage.addCereal(Cereal.RICE, 5f)
        storage.getCereal(Cereal.RICE, 5f) 
        val result = storage.removeContainer(Cereal.RICE)
        assertTrue(result)
        assertEquals(0f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should not remove non-empty container`() {
        storage.addCereal(Cereal.RICE, 5f)
        val result = storage.removeContainer(Cereal.RICE)
        assertFalse(result)
        assertEquals(5f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should return false when removing non-existent container`() {
        val result = storage.removeContainer(Cereal.RICE)
        assertFalse(result)
    }

    @Test
    fun `should return correct amount for existing cereal`() {
        storage.addCereal(Cereal.RICE, 7.5f)
        assertEquals(7.5f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should return zero amount for non-existent cereal`() {
        assertEquals(0f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should return updated amount after adding cereal`() {
        storage.addCereal(Cereal.RICE, 5f)
        storage.addCereal(Cereal.RICE, 3f)
        assertEquals(8f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should return updated amount after removing cereal`() {
        storage.addCereal(Cereal.RICE, 8f)
        storage.getCereal(Cereal.RICE, 3f)
        assertEquals(5f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should return full container capacity for new cereal`() {
        assertEquals(10f, storage.getSpace(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should return remaining space in partially filled container`() {
        storage.addCereal(Cereal.RICE, 7f)
        assertEquals(3f, storage.getSpace(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should return zero space for full container`() {
        storage.addCereal(Cereal.RICE, 10f)
        assertEquals(0f, storage.getSpace(Cereal.RICE), 0.01f)
    }

    @Test
    fun `should return updated space after removing some cereal`() {
        storage.addCereal(Cereal.RICE, 8f)
        storage.getCereal(Cereal.RICE, 3f)
        assertEquals(5f, storage.getSpace(Cereal.RICE), 0.01f)
    }

    @Test
    fun `toString should return empty storage info`() {
        val expected = "Хранилище: вместимость = 20.0, размер контейнера = 10.0, пусто"
        assertEquals(expected, storage.toString())
    }

    @Test
    fun `toString should return storage info with one cereal`() {
        storage.addCereal(Cereal.RICE, 7.5f)
        val expected = "Хранилище: вместимость = 20.0, размер контейнера = 10.0\n" +
                      "Рис: 7.5/10.0"
        assertEquals(expected, storage.toString())
    }

    @Test
    fun `toString should return storage info with multiple cereals`() {
        storage.addCereal(Cereal.RICE, 7.5f)
        storage.addCereal(Cereal.BUCKWHEAT, 4.0f)
        val expected = "Хранилище: вместимость = 20.0, размер контейнера = 10.0\n" +
                      "Гречка: 4.0/10.0\n" +
                      "Рис: 7.5/10.0"
        assertEquals(expected, storage.toString())
    }
}


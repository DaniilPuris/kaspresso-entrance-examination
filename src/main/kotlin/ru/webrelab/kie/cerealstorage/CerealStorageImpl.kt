package ru.webrelab.kie.cerealstorage

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()

    private fun getCurrentAmount(cereal: Cereal): Float {
        return storage.getOrDefault(cereal, 0f)
    }

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Количество крупы не может быть отрицательным" }
        
        if (!storage.containsKey(cereal) && (storage.size + 1) * containerCapacity > storageCapacity) {
            throw IllegalStateException("Невозможно добавить новый контейнер: хранилище заполнено")
        }

        val currentAmount = getCurrentAmount(cereal)
        val spaceInContainer = containerCapacity - currentAmount
        val amountToAdd = minOf(amount, spaceInContainer)
        
        storage[cereal] = currentAmount + amountToAdd
        return amount - amountToAdd
    }
    
    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Количество крупы не может быть отрицательным" }
        
        val currentAmount = storage.getOrDefault(cereal, 0f)
        val amountToGet = minOf(amount, currentAmount)
        
        if (amountToGet > 0) {
            storage[cereal] = currentAmount - amountToGet
            if (storage[cereal] == 0f) {
                storage.remove(cereal)
            }
        }
        
        return amountToGet
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        if (!storage.containsKey(cereal)) {
            return false
        }
        
        val currentAmount = getCurrentAmount(cereal)
        return if (currentAmount == 0f) {
            storage.remove(cereal)
            true
        } else {
            false
        }
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage.getOrDefault(cereal, 0f)
    }

    override fun getSpace(cereal: Cereal): Float {
        val currentAmount = storage.getOrDefault(cereal, 0f)
        return containerCapacity - currentAmount
    }

    override fun toString(): String {
        val header = "Хранилище: вместимость = $storageCapacity, размер контейнера = $containerCapacity"
        
        if (storage.isEmpty()) {
            return "$header, пусто"
        }
        
        return storage.entries
            .sortedBy { it.key.local }
            .joinToString(
                separator = "\n",
                prefix = "$header\n"
            ) { (cereal, amount) ->
                "${cereal.local}: $amount/$containerCapacity"
            }
    }


}


package org.chicagoedt.rosette.collectibles

class ItemInventory {
    private val inventory = HashMap<Int, Int>() // <ItemID, Count>

    fun addItem(itemID: Int) {
        // See if item exists, if exists, increment item counter. Otherwise, add it to the list
        if (inventory.containsKey(itemID)) {
            val oldCount = inventory[itemID]!!
            inventory[itemID] = oldCount + 1
        }
        else
            inventory[itemID] = 1
    }

    fun addItems(items: IntArray) {
        for (item in items) {
            addItem(item)
        }
    }

    fun removeItem(itemID: Int): Boolean {
        if (!hasItem(itemID)) return false

        val oldCount = inventory[itemID]!!
        when {
            oldCount == 1 -> inventory.remove(itemID)
            oldCount > 1 -> inventory[itemID] = oldCount - 1
            else -> return false
        }
        return true
    }

    fun removeItem(itemID: Int, quantity: Int): Boolean {
        // Item exists?
        if (!hasItem(itemID)) return false

        // Has enough items to remove?
        if (itemQuantity(itemID) < quantity) return false

        // Remove that much of the item
        val oldCount = inventory[itemID]!!
        inventory[itemID] = oldCount - quantity

        return true
    }

    fun hasItem(itemId: Int): Boolean = inventory.containsKey(itemId)

    fun itemQuantity(itemID: Int): Int = if (hasItem(itemID)) inventory[itemID]!! else 0
}
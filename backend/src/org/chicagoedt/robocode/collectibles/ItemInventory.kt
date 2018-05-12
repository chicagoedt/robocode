package org.chicagoedt.robocode.collectibles

/**
 * A single inventory of collectible items
 * @property inventory The current inventory for the items
 * @property checkpointInventory The saved state of an inventory for the items
 * @property oneTypeOnly True if this inventory should only contain a single item type, false otherwise
 */
class ItemInventory {
    private var inventory = HashMap<Int, Int>() // <ItemID, Count>
    private val checkpointInventory = HashMap<Int, Int>()
    var oneTypeOnly = false

    /**
     * Adds an item to inventory
     * @param itemID The item to add by its itemID
     */
    fun addItem(itemID: Int) : Boolean{
        // See if item exists, if exists, increment item counter. Otherwise, add it to the list
        if (inventory.containsKey(itemID)) {
            val oldCount = inventory[itemID]!!
            inventory[itemID] = oldCount + 1
        }
        else{
            if (this.allItemTypes().size > 0){
                if (!oneTypeOnly) inventory[itemID] = 1
                else return false
            }
            else{
                inventory[itemID] = 1
            }
        }
        return true
    }

    /**
     * Adds a list of items to inventory
     * @param items An array of itemID to add to inventory
     */
    fun addItems(items: IntArray) {
        for (item in items) {
            addItem(item)
        }
    }

    /**
     * Removes an item from inventory
     * @param itemID The item to remove by its itemID
     * @return True if remove successful. False if not.
     */
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

    /**
     * Removes a number of a certain item from inventory
     * @param itemID The item to remove by its itemID
     * @param quantity The quantity of the specified item to remove
     * @return True if remove successful. False if not.
     */
    fun removeItem(itemID: Int, quantity: Int): Boolean {
        // Item exists?
        if (!hasItem(itemID)) return false

        // Has enough items to remove?
        if (itemQuantity(itemID) < quantity) return false

        // Remove that much of the item
        val oldCount = inventory[itemID]!!
        inventory[itemID] = oldCount - quantity

        if (inventory[itemID] == 0) inventory.remove(itemID)

        return true
    }

    /**
     * Checks if an item exists in inventory
     * @param itemID The item to check by its itemID
     * @return True if item exists. False if not.
     */
    fun hasItem(itemId: Int): Boolean = inventory.containsKey(itemId)

    /**
     * Gets the number of the specified item found in inventory
     * @param itemID The item to check by its itemID
     * @return Quantity of itemID
     */
    fun itemQuantity(itemID: Int): Int = if (hasItem(itemID)) inventory[itemID]!! else 0

    /**
     * Gets a list of IDs for all the items in this inventory
     * @return A list of IDs for all the items in this inventory
     */
    fun allItemTypes() : ArrayList<Int> {
        val list = arrayListOf<Int>()
        list.addAll(inventory.keys.toMutableList())
        return list
    }

    /**
     * Saves the state of the inventory to restore later
     */
    fun saveCheckpoint(){
        checkpointInventory.clear()
        for ((first, second) in inventory){
            checkpointInventory[first] = second
        }
    }

    /**
     * Restores the state of the inventory that was saved previously
     */
    fun restoreCheckpoint(){
        inventory.clear()
        for ((first, second) in checkpointInventory){
            inventory[first] = second
        }

    }
}
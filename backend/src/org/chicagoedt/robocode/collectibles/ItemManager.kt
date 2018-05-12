package org.chicagoedt.robocode.collectibles

import org.chicagoedt.robocode.collectibles.etc.Sand
import org.chicagoedt.robocode.collectibles.etc.Water

/**
 * An object used to manage all the known items supported in the game
 */
object ItemManager {
    private val itemList = HashMap<Int, Collectible>()

    init {
        fun addItem(item: Collectible) {
            itemList[item.id] = item
        }

        // Load all items to be used for the game
        addItem(Sand)
        addItem(Water)
    }

    /**
     * Checks if an item exists in game
     * @param itemID The item to check by its itemID
     * @return True if item exists. False if not.
     */
    fun itemExist(itemID: Int): Boolean = itemList.containsKey(itemID)

    /**
     * Gets the item object requested by itemID
     * @param itemID The item to get by its itemID
     * @return The Collectible object if exists, null if not
     */
    fun getItem(itemID: Int): Collectible? = itemList[itemID]

    /**
     * Gets a list of all possible items in the game
     * @return A list of all possible items in the game
     */
    fun getAllItems() : ArrayList<Collectible>{
        val list = arrayListOf<Collectible>()
        list.addAll(itemList.values)
        return list
    }
}
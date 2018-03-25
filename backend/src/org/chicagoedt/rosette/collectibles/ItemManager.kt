package org.chicagoedt.rosette.collectibles

import org.chicagoedt.rosette.collectibles.etc.Sand
import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer


object ItemManager {
    private val itemList = HashMap<Int, Collectible>()

    init {
        fun addItem(item: Collectible) {
            itemList[item.id] = item
        }

        // Load all items to be used for the game
        addItem(Sand)
    }

    fun itemExist(itemID: Int): Boolean = itemList.containsKey(itemID)

    fun getItem(itemID: Int): Collectible? = itemList[itemID]
}
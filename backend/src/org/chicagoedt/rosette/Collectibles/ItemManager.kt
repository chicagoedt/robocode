package org.chicagoedt.rosette.Collectibles

import org.chicagoedt.rosette.Collectibles.Etc.Sand
import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer

class ItemManager() {
    private val itemList = HashMap<Int, Collectible>()

    lateinit var levelContext: Level

    init {
        fun addItem(item: Collectible) {
            itemList[item.id] = item
        }

        // Load all known items
        addItem(Sand())
    }

    fun useItem(robotContext: RobotPlayer, itemID: Int): Boolean {
        // Check if item is registered for use
        if (!itemList.containsKey(itemID)) return false

        // Check if player has the item it wants to use
        if (!robotContext.itemInventory.containsKey(itemID)) return false

        // Check if player has enough of the item
        if (robotContext.itemInventory[itemID]!! < itemList[itemID]!!.minUseQuantity) return false

        // Use the item
        itemList[itemID]!!.use(levelContext, robotContext)

        // Update item count
        val oldCount = robotContext.itemInventory[itemID]!!
        robotContext.itemInventory[itemID] = oldCount - 1

        return true
    }
}
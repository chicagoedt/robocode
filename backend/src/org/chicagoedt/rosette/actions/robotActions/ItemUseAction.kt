package org.chicagoedt.rosette.actions.robotActions

import org.chicagoedt.rosette.actions.Action
import org.chicagoedt.rosette.collectibles.ItemManager
import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer

/**
 * Uses an item [parameter] by itemID
 */
class ItemUseAction: Action<Int>() {
    override val name: String = "Use item"
    override var parameter = 1

    override fun function(level: Level, robot: RobotPlayer, parameter: Int) {
        // Check if item is registered for use
        if (!ItemManager.itemExist(parameter)) return

        // Check if player has the item it wants to use
        if (!robot.itemInventory.hasItem(parameter)) return

        // Check if player has enough of the item
        if (robot.itemInventory.itemQuantity(parameter) < ItemManager.getItem(parameter)!!.useQuantity) return

        // Use the item
        ItemManager.getItem(parameter)!!.use(level, robot)

        // Update item count
        robot.itemInventory.removeItem(parameter, ItemManager.getItem(parameter)!!.useQuantity)
    }
}
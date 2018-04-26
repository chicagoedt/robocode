package org.chicagoedt.robocode.actions.robotActions

import org.chicagoedt.robocode.actions.Action
import org.chicagoedt.robocode.collectibles.ItemManager
import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.RobotPlayer

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
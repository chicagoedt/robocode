package org.chicagoedt.rosette.actions.robotActions

import org.chicagoedt.rosette.actions.Action
import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer

/**
 * Pick up an item [parameter] by itemID
 */
class ItemPickupAction: Action<Int>() {
    override val name: String = "Pick up item"
    override var parameter = 1

    override fun function(level: Level, robot: RobotPlayer, parameter: Int) {
        // Does tile have the item it wants to drop?
        if (!level.tileAt(robot.x, robot.y).items.hasItem(parameter)) return

        // Yes, Remove item from the ground and put in robot player's inventory
        level.tileAt(robot.x, robot.y).items.removeItem(parameter)
        robot.itemInventory.addItem(parameter)
    }
}
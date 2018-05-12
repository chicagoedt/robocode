package org.chicagoedt.robocode.actions.robotActions

import org.chicagoedt.robocode.actions.Action
import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.RobotPlayer

/**
 * Drops an item [parameter] by itemID
 */
class ItemDropAction: Action<Int>() {
    override val name: String = "Drop item"
    override var parameter = 1

    override fun function(level: Level, robot: RobotPlayer, parameter: Int) {
        // Does robot have the item it wants to drop?
        if (!robot.itemInventory.hasItem(parameter)) return

        // Yes, remove from player and add item back to board
        val dropped = level.tileAt(robot.x, robot.y).items.addItem(parameter)
        if (dropped) robot.itemInventory.removeItem(parameter)
    }
}
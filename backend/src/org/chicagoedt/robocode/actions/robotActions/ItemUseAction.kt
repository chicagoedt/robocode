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
        //TODO adapt this action to a selection of functions that items can do
    }
}
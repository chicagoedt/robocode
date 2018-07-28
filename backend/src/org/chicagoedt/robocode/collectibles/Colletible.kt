package org.chicagoedt.robocode.collectibles

import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.RobotPlayer

/**
 * An item that can be picked up by the RobotPlayer
 * @param id The unique identifier of the item
 * @param name The name of the item
 * @param graphic The image URI
 */
data class Collectible(val id : Int,
                       val name : String,
                       val graphic : String) {
}
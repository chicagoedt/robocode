package org.chicagoedt.rosette.collectibles

import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer

/**
 * An item that can be picked up by the RobotPlayer
 * @property id The unique identifier of the item
 * @property type The type of item
 * @property name The name of the item
 * @property graphic The image URI
 * @property minUseQuantity The minimum number of this item to use the item
 */
interface Collectible {
    enum class ItemType {
        EQUIPMENT,
        CONSUMABLES,
        ETC
    }

    val id: Int
    val type: ItemType
    val name: String
    val graphic: String

    val minUseQuantity: Int

    /**
     * Interact with the item
     */
    fun use(level: Level, robot: RobotPlayer)
}
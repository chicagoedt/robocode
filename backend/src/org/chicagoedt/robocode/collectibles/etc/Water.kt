package org.chicagoedt.robocode.collectibles.etc

import org.chicagoedt.robocode.collectibles.Collectible
import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.RobotPlayer

object Water: Collectible {
    override val id = 1001
    override val type = Collectible.ItemType.ETC
    override val name = "Water"
    override val graphic = ""
    override val useQuantity = 5

    override fun use(level: Level, robot: RobotPlayer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
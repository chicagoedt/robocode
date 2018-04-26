package org.chicagoedt.robocode.collectibles.etc

import org.chicagoedt.robocode.collectibles.Collectible
import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.RobotPlayer

object Sand: Collectible {
    override val id = 1000
    override val type = Collectible.ItemType.ETC
    override val name = "Sand"
    override val graphic = ""
    override val useQuantity = 5

    override fun use(level: Level, robot: RobotPlayer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
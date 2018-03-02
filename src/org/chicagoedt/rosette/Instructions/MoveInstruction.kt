package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Level.Level
import org.chicagoedt.rosette.Robots.RobotOrientation
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Tiles.TileType

class MoveInstruction : Instruction {
    override val name: String = "Move"
    override var parameter: Any = 1

    override fun function(level: Level, robot: RobotPlayer, parameter: Any) {
        for (i in 1..parameter as Int) {
            if (robot.direction == RobotOrientation.DIRECTION_UP &&
                    robot.y + 1 < level.properties.height &&
                    level.tileAt(robot.x, robot.y + 1).type != TileType.OBSTACLE)
                robot.y++
            else if (robot.direction == RobotOrientation.DIRECTION_DOWN &&
                    robot.y - 1 >= 0 &&
                    level.tileAt(robot.x, robot.y - 1).type != TileType.OBSTACLE)
                robot.y--
            else if (robot.direction == RobotOrientation.DIRECTION_LEFT &&
                    robot.x - 1 >= 0 &&
                    level.tileAt(robot.x - 1, robot.y).type != TileType.OBSTACLE)
                robot.x--
            else if (robot.direction == RobotOrientation.DIRECTION_RIGHT &&
                    robot.x + 1 < level.properties.width &&
                    level.tileAt(robot.x + 1, robot.y).type != TileType.OBSTACLE)
                robot.x++
        }
    }
}
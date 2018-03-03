package org.chicagoedt.rosette.Sensors

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotOrientation
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Tiles.TileType

class DistanceSensor : Sensor {
    override val type: SensorType = SensorType.DISTANCE

    override fun function(player : RobotPlayer, level: Level, position: SensorPosition) : Int {
        var x = player.x
        var y = player.y
        var tile = level.tileAt(x, y)
        var sum = 0
        while (tile.type != TileType.OBSTACLE &&
                x >= 0 &&
                x < level.properties.width &&
                y >= 0 &&
                y < level.properties.height){
            if (position == SensorPosition.FRONT){
                when (player.direction){
                    RobotOrientation.DIRECTION_UP -> y++
                    RobotOrientation.DIRECTION_DOWN -> y--
                    RobotOrientation.DIRECTION_LEFT -> x--
                    RobotOrientation.DIRECTION_RIGHT -> x++
                }
            }
            else if (position == SensorPosition.LEFT){
                when (player.direction){
                    RobotOrientation.DIRECTION_UP -> x--
                    RobotOrientation.DIRECTION_DOWN -> x++
                    RobotOrientation.DIRECTION_LEFT -> y--
                    RobotOrientation.DIRECTION_RIGHT -> y++
                }
            }
            else if (position == SensorPosition.RIGHT){
                when (player.direction){
                    RobotOrientation.DIRECTION_UP -> x++
                    RobotOrientation.DIRECTION_DOWN -> x--
                    RobotOrientation.DIRECTION_LEFT -> y++
                    RobotOrientation.DIRECTION_RIGHT -> y--
                }
            }
            else if (position == SensorPosition.BACK){
                when (player.direction){
                    RobotOrientation.DIRECTION_UP -> y--
                    RobotOrientation.DIRECTION_DOWN -> y++
                    RobotOrientation.DIRECTION_LEFT -> x++
                    RobotOrientation.DIRECTION_RIGHT -> x--
                }
            }
            sum++
            tile = level.tileAt(x,y)
        }
        return sum
    }
}
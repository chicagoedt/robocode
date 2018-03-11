package org.chicagoedt.rosette.Sensors

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotOrientation
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Tiles.TileType
import org.chicagoedt.rosette.Topic
import org.chicagoedt.rosette.Robots.RobotPosition

class DistanceSensor : Sensor() {
    override val type: SensorType = SensorType.DISTANCE

     override fun readFromSensor(player : RobotPlayer, level: Level, topic: Topic) {
        val position = getSensorPos(player)
        var x = player.x
        var y = player.y
        var tile = level.tileAt(x, y)
        var sum = 0
        while (tile.type != TileType.OBSTACLE &&
                x >= 0 &&
                x < level.properties.width &&
                y >= 0 &&
                y < level.properties.height){
            if (position == RobotPosition.FRONT){
                when (player.direction){
                    RobotOrientation.DIRECTION_UP -> y++
                    RobotOrientation.DIRECTION_DOWN -> y--
                    RobotOrientation.DIRECTION_LEFT -> x--
                    RobotOrientation.DIRECTION_RIGHT -> x++
                }
            }
            else if (position == RobotPosition.LEFT){
                when (player.direction){
                    RobotOrientation.DIRECTION_UP -> x--
                    RobotOrientation.DIRECTION_DOWN -> x++
                    RobotOrientation.DIRECTION_LEFT -> y--
                    RobotOrientation.DIRECTION_RIGHT -> y++
                }
            }
            else if (position == RobotPosition.RIGHT){
                when (player.direction){
                    RobotOrientation.DIRECTION_UP -> x++
                    RobotOrientation.DIRECTION_DOWN -> x--
                    RobotOrientation.DIRECTION_LEFT -> y++
                    RobotOrientation.DIRECTION_RIGHT -> y--
                }
            }
            else if (position == RobotPosition.BACK){
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
        topic.value = sum
    }
}
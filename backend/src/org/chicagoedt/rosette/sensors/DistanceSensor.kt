package org.chicagoedt.rosette.sensors

import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotOrientation
import org.chicagoedt.rosette.robots.RobotPlayer
import org.chicagoedt.rosette.tiles.TileType
import org.chicagoedt.rosette.Topic
import org.chicagoedt.rosette.robots.RobotPosition

/**
 * A sensor to measure a the distance from the robot to an obstacle or edge
 * It measures outwards from the side that the sensor is attached to
 */
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
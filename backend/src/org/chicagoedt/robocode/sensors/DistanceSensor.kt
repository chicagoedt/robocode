package org.chicagoedt.robocode.sensors

import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.RobotOrientation
import org.chicagoedt.robocode.robots.RobotPlayer
import org.chicagoedt.robocode.tiles.TileType
import org.chicagoedt.robocode.Topic
import org.chicagoedt.robocode.robots.RobotPosition

/**
 * A sensor to measure a the distance from the robot to an obstacle or edge
 * It measures outwards from the side that the sensor is attached to
 */
class DistanceSensor : Sensor() {
    override val name = "Distance Sensor"
    override val type: SensorType = SensorType.DISTANCE

     override fun readFromSensor(level: Level, topic: Topic) {
        var x = player!!.x
        var y = player!!.y
        var tile = level.tileAt(x, y)

         //start at -1 since it will always be incremented at least once
        var sum = -1

        while (x >= 0 &&
                x < level.properties.width &&
                y >= 0 &&
                y < level.properties.height){
            tile = level.tileAt(x,y)
            if (tile.type == TileType.OBSTACLE) break

            sum++
          
            if (sensorPosition == RobotPosition.FRONT){
                when (player!!.direction){
                    RobotOrientation.DIRECTION_UP -> y++
                    RobotOrientation.DIRECTION_DOWN -> y--
                    RobotOrientation.DIRECTION_LEFT -> x--
                    RobotOrientation.DIRECTION_RIGHT -> x++
                }
            }
            else if (sensorPosition == RobotPosition.LEFT){
                when (player!!.direction){
                    RobotOrientation.DIRECTION_UP -> x--
                    RobotOrientation.DIRECTION_DOWN -> x++
                    RobotOrientation.DIRECTION_LEFT -> y--
                    RobotOrientation.DIRECTION_RIGHT -> y++
                }
            }
            else if (sensorPosition == RobotPosition.RIGHT){
                when (player!!.direction){
                    RobotOrientation.DIRECTION_UP -> x++
                    RobotOrientation.DIRECTION_DOWN -> x--
                    RobotOrientation.DIRECTION_LEFT -> y++
                    RobotOrientation.DIRECTION_RIGHT -> y--
                }
            }
            else if (sensorPosition == RobotPosition.BACK){
                when (player!!.direction){
                    RobotOrientation.DIRECTION_UP -> y--
                    RobotOrientation.DIRECTION_DOWN -> y++
                    RobotOrientation.DIRECTION_LEFT -> x++
                    RobotOrientation.DIRECTION_RIGHT -> x--
                }
            }

        }
        topic.value = sum
    }
}
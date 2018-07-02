package org.chicagoedt.robocode.sensors

import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.RobotPlayer
import org.chicagoedt.robocode.robots.RobotPosition
import org.chicagoedt.robocode.Topic

/**
 * A class containing all types of sensors
 */
enum class SensorType{
    /**
     * An empty sensor spot
     */
    EMPTY,
    /**
     * A sensor to measure distance to the nearest obstacle
     */
    DISTANCE
}

/**
 * A tool for the robot to examine the grid around it
 * @property type The type of sensor for the current instance
 * @property name The name of the sensor
 * @property sensorPosition The position of the sensor. Null if the sensor is not attached to a robot player
 * @property player The robot player that this sensor is attached to. Null if the sensor is not attached to a robot player
 */
abstract class Sensor {
    abstract val type : SensorType
    abstract val name : String
    var sensorPosition  : RobotPosition? = null
    var player : RobotPlayer? = null
        set(value) {
            updateSensorPos(value)
            field = value
        }

    /**
     * Stores the value from a sensor in [topic]
     * @param player The player that the sensor is attached to
     * @param level The level that the player is on
     * @param topic The topic to store the output from the sensor
     */
    internal abstract fun readFromSensor(level: Level, topic: Topic)

    /**
     * @param player The player that the sensor is attached to
     * @return The position of the sensor on the robot
     * @throws Exception If not found on the robot
     */
    private fun updateSensorPos(player: RobotPlayer?){
        if (player == null) sensorPosition = null
        else if (player.getSensors(RobotPosition.FRONT).contains(this)) sensorPosition = RobotPosition.FRONT
        else if (player.getSensors(RobotPosition.BACK).contains(this)) sensorPosition = RobotPosition.BACK
        else if (player.getSensors(RobotPosition.LEFT).contains(this)) sensorPosition = RobotPosition.LEFT
        else if (player.getSensors(RobotPosition.RIGHT).contains(this)) sensorPosition = RobotPosition.RIGHT
        else throw Exception("Sensor not found on robot")
    }
}
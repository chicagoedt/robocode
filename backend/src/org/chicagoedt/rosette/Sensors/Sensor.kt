package org.chicagoedt.rosette.Sensors

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Robots.RobotPosition
import org.chicagoedt.rosette.Topic

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
 */
abstract class Sensor {
    /**
     * The type of sensor for the current instance
     */
    internal abstract val type : SensorType

    /**
     * Stores the value from a sensor in [topic]
     * @param player The player that the sensor is attached to
     * @param level The level that the player is on
     * @param topic The topic to store the output from the sensor
     */
    internal abstract fun readFromSensor(player : RobotPlayer, level: Level, topic: Topic)

    /**
     * @param player The player that the sensor is attached to
     * @return The position of the sensor on the robot
     * @throws Exception If not found on the robot
     */
    internal fun getSensorPos(player: RobotPlayer) : RobotPosition{
        if (player.getSensors(RobotPosition.FRONT).contains(this)) return RobotPosition.FRONT
        else if (player.getSensors(RobotPosition.BACK).contains(this)) return RobotPosition.BACK
        else if (player.getSensors(RobotPosition.LEFT).contains(this)) return RobotPosition.LEFT
        else if (player.getSensors(RobotPosition.RIGHT).contains(this)) return RobotPosition.RIGHT

        throw Exception("Sensor not found on robot")
    }
}
package org.chicagoedt.rosette.Sensors

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Robots.RobotPosition
import org.chicagoedt.rosette.Topic

enum class SensorType{
    EMPTY,
    DISTANCE
}

abstract class Sensor {
    internal abstract val type : SensorType

    internal abstract fun readFromSensor(player : RobotPlayer, level: Level, topic: Topic)

    internal fun getSensorPos(player: RobotPlayer) : RobotPosition{
        if (player.getSensors(RobotPosition.FRONT).contains(this)) return RobotPosition.FRONT
        else if (player.getSensors(RobotPosition.BACK).contains(this)) return RobotPosition.BACK
        else if (player.getSensors(RobotPosition.LEFT).contains(this)) return RobotPosition.LEFT
        else if (player.getSensors(RobotPosition.RIGHT).contains(this)) return RobotPosition.RIGHT

        throw Exception("Sensor not found on robot")
    }
}
package org.chicagoedt.rosette.Sensors

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer

enum class SensorType{
    DISTANCE
}

enum class SensorPosition{
    FRONT,
    LEFT,
    RIGHT,
    BACK
}

interface Sensor {
    val type : SensorType

    fun function(player : RobotPlayer, level: Level, position: SensorPosition) : Int
}
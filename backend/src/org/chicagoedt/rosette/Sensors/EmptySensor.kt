package org.chicagoedt.rosette.Sensors

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer

class EmptySensor() : Sensor {
    override val type: SensorType = SensorType.EMPTY
    override fun function(player: RobotPlayer, level: Level, position: SensorPosition): Int {
        return -1
    }
}
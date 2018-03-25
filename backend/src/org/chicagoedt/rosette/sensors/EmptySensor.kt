package org.chicagoedt.rosette.sensors

import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer
import org.chicagoedt.rosette.Topic

/**
 * A placeholder sensor when there is no sensor in a location on a robot
 */
class EmptySensor() : Sensor() {
    override val type: SensorType = SensorType.EMPTY
    override fun readFromSensor(player: RobotPlayer, level: Level, topic: Topic) {
        //nothing
    }
}
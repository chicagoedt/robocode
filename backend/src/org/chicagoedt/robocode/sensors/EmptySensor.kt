package org.chicagoedt.robocode.sensors

import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.RobotPlayer
import org.chicagoedt.robocode.Topic

/**
 * A placeholder sensor when there is no sensor in a location on a robot
 */
class EmptySensor() : Sensor() {
    override val name = "Empty"
    override val type: SensorType = SensorType.EMPTY
    override fun readFromSensor(player: RobotPlayer, level: Level, topic: Topic) {
        //nothing
    }
}
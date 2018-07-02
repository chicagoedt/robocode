package org.chicagoedt.robocode.actions.robotActions

import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.RobotPlayer
import org.chicagoedt.robocode.sensors.EmptySensor
import org.chicagoedt.robocode.sensors.Sensor
import org.chicagoedt.robocode.Topic
import org.chicagoedt.robocode.actions.Action

/**
 * Reads the data from a sensor and stores it
 * @param topic The topic to store the data in
 */
class ReadSensorAction(val topic: Topic) : Action<Sensor>() {
    override var parameter: Sensor = EmptySensor()
    override val name: String = "Read Sensor"

    override fun function(level: Level, robot: RobotPlayer, parameter: Sensor) {
        parameter.readFromSensor(level, topic)
    }

}
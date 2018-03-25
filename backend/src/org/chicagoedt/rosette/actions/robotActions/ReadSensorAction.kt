package org.chicagoedt.rosette.actions.robotActions

import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer
import org.chicagoedt.rosette.sensors.EmptySensor
import org.chicagoedt.rosette.sensors.Sensor
import org.chicagoedt.rosette.Topic
import org.chicagoedt.rosette.actions.Action

/**
 * Reads the data from a sensor and stores it
 * @param topic The topic to store the data in
 */
class ReadSensorAction(val topic: Topic) : Action<Sensor>() {
    override var parameter: Sensor = EmptySensor()
    override val name: String = "Read Sensor"

    override fun function(level: Level, robot: RobotPlayer, parameter: Sensor) {
        parameter.readFromSensor(robot, level, topic)
    }

}
package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Sensors.EmptySensor
import org.chicagoedt.rosette.Sensors.Sensor
import org.chicagoedt.rosette.Topic

/**
 * Reads the data from a sensor and stores it
 * @param topic The topic to store the data in
 */
class ReadSensorInstruction(val topic: Topic) : Instruction<Sensor>() {
    override var parameter: Sensor = EmptySensor()
    override val name: String = "Read Sensor"

    override fun function(level: Level, robot: RobotPlayer, parameter: Sensor) {
        parameter.readFromSensor(robot, level, topic)
    }

}
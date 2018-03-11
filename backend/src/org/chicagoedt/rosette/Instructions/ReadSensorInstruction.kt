package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Sensors.EmptySensor
import org.chicagoedt.rosette.Sensors.Sensor
import org.chicagoedt.rosette.Topic

class ReadSensorInstruction(val topic: Topic) : Instruction() {
    override var parameter: Any = EmptySensor()
    override val name: String = "Read Sensor"

    override fun function(level: Level, robot: RobotPlayer, parameter: Any) {
        if (parameter is Sensor){
            parameter.readFromSensor(robot, level, topic)
        }
    }

}
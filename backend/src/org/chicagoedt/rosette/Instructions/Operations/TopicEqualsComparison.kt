package org.chicagoedt.rosette.Instructions.Operations

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Sensors.Sensor
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Topic

class TopicEqualsComparison<U>(first: Topic, second: U) : Comparison<Topic,U>(first, second) {
    
    override fun result(): Boolean {
        return first.value == second
    }
}
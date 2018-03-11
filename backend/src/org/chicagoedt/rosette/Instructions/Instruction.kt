package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer

val availableInstructions = arrayListOf<Pair<String, String>>(
        Pair("Move", "Int"),
        Pair("Turn", "Rotation"),
        Pair("Conditional", "Comparison"),
        Pair("Read Sensor", "Sensor")
        )

interface Instruction {
    var parameter : Any

    val name : String

    fun function(level: Level, robot: RobotPlayer, parameter: Any);
}
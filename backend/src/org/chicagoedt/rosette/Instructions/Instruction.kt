package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer

val availableInstructions = arrayListOf<Pair<String, String>>(
        Pair("Move", "Int"),
        Pair("Turn", "Rotation"),
        Pair("Conditional", "Comparison"),
        Pair("Read Sensor", "Sensor")
        )

abstract class Instruction {
    abstract var parameter : Any

    abstract val name : String

    internal abstract fun function(level: Level, robot: RobotPlayer, parameter: Any);
}
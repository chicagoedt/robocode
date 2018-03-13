package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer

abstract class Instruction {
    abstract var parameter : Any

    abstract val name : String

    internal abstract fun function(level: Level, robot: RobotPlayer, parameter: Any);
}
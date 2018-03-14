package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer

abstract class Instruction<T> {
    abstract var parameter : T

    abstract val name : String

    internal abstract fun function(level: Level, robot: RobotPlayer, parameter: Any);
}
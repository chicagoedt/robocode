package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer

/**
 * A process to run on a robot including, but not limited to, moving and turning the robot.
 *
 * The generic type is the type of parameter that this instruction takes
 *
 * @property parameter A value for this instruction to use in [function]
 * @property name The user-friendly name of this instruction
 */
abstract class Instruction<T> {
    abstract var parameter : T

    abstract val name : String

    /**
     * The function to run when this instruction is executed
     * @param level The current level that the robot is on
     * @param robot The current robot that the instruction is attached to
     * @param parameter The value to be used by this instruction
     */
    internal abstract fun function(level: Level, robot: RobotPlayer, parameter: T);
}
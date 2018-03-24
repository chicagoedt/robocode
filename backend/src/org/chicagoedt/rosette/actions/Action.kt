package org.chicagoedt.rosette.actions

import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer

/**
 * A process to run on a robot including, but not limited to, moving and turning the robot.
 *
 * The generic type is the type of parameter that this action takes
 *
 * @property parameter A value for this action to use in [function]
 * @property name The user-friendly name of this action
 */
abstract class Action<T> {
    abstract var parameter : T

    abstract val name : String

    /**
     * The function to run when this action is executed
     * @param level The current level that the robot is on
     * @param robot The current robot that the action is attached to
     * @param parameter The value to be used by this action
     */
    internal abstract fun function(level: Level, robot: RobotPlayer, parameter: T)
}
package org.chicagoedt.rosette.actions.robotActions

import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer
import org.chicagoedt.rosette.robots.RobotRotation
import org.chicagoedt.rosette.robots.nextDirection
import org.chicagoedt.rosette.actions.Action

/**
 * Turns the robot in the direction specified by the parameter
 */
class TurnAction : Action<RobotRotation>() {
    override val name: String = "Turn"
    override var parameter = RobotRotation.CLOCKWISE

    override fun function(level: Level, robot: RobotPlayer, parameter: RobotRotation) {
        robot.direction = nextDirection(robot.direction, parameter)
    }
}
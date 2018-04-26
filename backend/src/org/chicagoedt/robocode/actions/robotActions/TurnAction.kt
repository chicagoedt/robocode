package org.chicagoedt.robocode.actions.robotActions

import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.RobotPlayer
import org.chicagoedt.robocode.robots.RobotRotation
import org.chicagoedt.robocode.robots.nextDirection
import org.chicagoedt.robocode.actions.Action

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
package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Robots.RobotRotation
import org.chicagoedt.rosette.Robots.nextDirection

/**
 * Turns the robot in the direction specified by the parameter
 */
class TurnInstruction : Instruction<RobotRotation>() {
    override val name: String = "Turn"
    override var parameter = RobotRotation.CLOCKWISE

    override fun function(level: Level, robot: RobotPlayer, parameter: RobotRotation) {
        robot.direction = nextDirection(robot.direction, parameter)
    }
}
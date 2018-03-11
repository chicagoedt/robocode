package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Robots.RobotRotation
import org.chicagoedt.rosette.Robots.nextDirection

class TurnInstruction : Instruction() {
    override val name: String = "Turn"
    override var parameter: Any = RobotRotation.CLOCKWISE

    override fun function(level: Level, robot: RobotPlayer, parameter: Any) {
        robot.direction = nextDirection(robot.direction, parameter as RobotRotation)
    }
}
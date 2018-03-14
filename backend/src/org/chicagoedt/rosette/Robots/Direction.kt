package org.chicagoedt.rosette.Robots

/**
 * The directions that a robot can be facing
 * @property DIRECTION_UP Robot facing up
 * @property DIRECTION_LEFT Robot facing left
 * @property DIRECTION_RIGHT Robot facing right
 * @property DIRECTION_DOWN Robot facing down
 * @property DIRECTION_NONE Robot not facing a direction. If you encounter this, it's probably an error
 */
enum class RobotOrientation {
    DIRECTION_UP,
    DIRECTION_DOWN,
    DIRECTION_LEFT,
    DIRECTION_RIGHT,
    DIRECTION_NONE
}

/**
 * The directions that a robot can rotate
 * @property CLOCKWISE Clockwise rotation
 * @property COUNTERCLOCKWISE Counterclockwise rotation
 */
enum class RobotRotation {
    CLOCKWISE,
    COUNTERCLOCKWISE
}

/**
 * @param orientation The current orientation of the robot
 * @param turnDirection The direction to turn the robot
 * @return The direction the robot would be facing after 1 rotation in the specified direction
 */
fun nextDirection(orientation: RobotOrientation, turnDirection: RobotRotation): RobotOrientation {
    if (turnDirection == RobotRotation.CLOCKWISE) {
        when (orientation) {
            RobotOrientation.DIRECTION_UP -> return RobotOrientation.DIRECTION_RIGHT
            RobotOrientation.DIRECTION_RIGHT -> return RobotOrientation.DIRECTION_DOWN
            RobotOrientation.DIRECTION_DOWN -> return RobotOrientation.DIRECTION_LEFT
            RobotOrientation.DIRECTION_LEFT -> return RobotOrientation.DIRECTION_UP
        }
    }
    else if (turnDirection == RobotRotation.COUNTERCLOCKWISE) {
        when (orientation) {
            RobotOrientation.DIRECTION_UP -> return RobotOrientation.DIRECTION_LEFT
            RobotOrientation.DIRECTION_RIGHT -> return RobotOrientation.DIRECTION_UP
            RobotOrientation.DIRECTION_DOWN -> return RobotOrientation.DIRECTION_RIGHT
            RobotOrientation.DIRECTION_LEFT -> return RobotOrientation.DIRECTION_DOWN
        }
    }

    return RobotOrientation.DIRECTION_NONE
}
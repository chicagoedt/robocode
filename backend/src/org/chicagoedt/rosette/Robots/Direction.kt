package org.chicagoedt.rosette.Robots

enum class RobotOrientation {
    DIRECTION_UP,
    DIRECTION_DOWN,
    DIRECTION_LEFT,
    DIRECTION_RIGHT,
    DIRECTION_NONE
}

enum class RobotRotation {
    CLOCKWISE,
    COUNTERCLOCKWISE
}

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
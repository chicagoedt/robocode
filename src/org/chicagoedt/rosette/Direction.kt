package org.chicagoedt.rosette

val DIRECTION_UP = 0
val DIRECTION_DOWN = 1
val DIRECTION_LEFT = 2
val DIRECTION_RIGHT = 3
val TURN_DIRECTION_CLOCKWISE = 10;
val TURN_DIRECTION_COUNTERCLOCKWISE = 11;

fun nextDirection(direction: Int, turnDirection: Int) : Int{
    if (turnDirection == TURN_DIRECTION_CLOCKWISE) {
        if (direction == DIRECTION_UP) return DIRECTION_RIGHT
        else if (direction == DIRECTION_RIGHT) return DIRECTION_DOWN
        else if (direction == DIRECTION_DOWN) return DIRECTION_LEFT
        else if (direction == DIRECTION_LEFT) return DIRECTION_UP
    }
    else if (turnDirection == TURN_DIRECTION_COUNTERCLOCKWISE) {
        if (direction == DIRECTION_UP) return DIRECTION_LEFT
        else if (direction == DIRECTION_RIGHT) return DIRECTION_UP
        else if (direction == DIRECTION_DOWN) return DIRECTION_RIGHT
        else if (direction == DIRECTION_LEFT) return DIRECTION_DOWN
    }
    return -1
}
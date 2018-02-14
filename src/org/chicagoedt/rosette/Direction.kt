package org.chicagoedt.rosette

const val DIRECTION_UP = 0
const val DIRECTION_DOWN = 1
const val DIRECTION_LEFT = 2
const val DIRECTION_RIGHT = 3
const val TURN_DIRECTION_CLOCKWISE = 10
const val TURN_DIRECTION_COUNTERCLOCKWISE = 11

fun nextDirection(direction: Int, turnDirection: Int) : Int{
    if (turnDirection == TURN_DIRECTION_CLOCKWISE) {
        when(direction) {
            DIRECTION_UP -> return DIRECTION_RIGHT
            DIRECTION_RIGHT -> return DIRECTION_DOWN
            DIRECTION_DOWN -> return DIRECTION_LEFT
            DIRECTION_LEFT -> return DIRECTION_UP
        }
    }
    else if (turnDirection == TURN_DIRECTION_COUNTERCLOCKWISE) {
        when(direction) {
            DIRECTION_UP -> return DIRECTION_LEFT
            DIRECTION_RIGHT -> return DIRECTION_UP
            DIRECTION_DOWN -> return DIRECTION_RIGHT
            DIRECTION_LEFT -> return DIRECTION_DOWN
        }
    }
    return -1
}
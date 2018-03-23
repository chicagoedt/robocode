package org.chicagoedt.rosette.actions.robotActions

import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotOrientation
import org.chicagoedt.rosette.robots.RobotPlayer
import org.chicagoedt.rosette.tiles.TileType
import org.chicagoedt.rosette.actions.Instruction

/**
 * Moves the robot by [parameter] tiles in the direction that it's facing
 */
class MoveInstruction : Instruction<Int>() {
    override val name: String = "Move"
    override var parameter = 1

    override fun function(level: Level, robot: RobotPlayer, parameter: Int) {
        val difference = distanceCanMove(robot.x, robot.y, robot.direction, parameter, level)
        when (robot.direction){
            RobotOrientation.DIRECTION_UP -> robot.y += difference
            RobotOrientation.DIRECTION_DOWN -> robot.y -= difference
            RobotOrientation.DIRECTION_RIGHT -> robot.x += difference
            RobotOrientation.DIRECTION_LEFT -> robot.x -= difference
        }
    }

    fun distanceCanMove(x: Int, y: Int, orientation: RobotOrientation, distance: Int, level: Level): Int{
        var currentX = x
        var currentY = y
        var possibleDistance = 0
        for (i in 0..distance-1){
            if (orientation == RobotOrientation.DIRECTION_UP){
                if (currentY + 1 >= level.properties.height || level.tileAt(currentX, currentY+1).type == TileType.OBSTACLE) return possibleDistance
                else {
                    possibleDistance++
                    currentY++
                }
            }
            else if (orientation == RobotOrientation.DIRECTION_DOWN){
                if (currentY - 1 < 0 || level.tileAt(currentX, currentY-1).type == TileType.OBSTACLE) return possibleDistance
                else {
                    possibleDistance++
                    currentY--
                }
            }
            else if (orientation == RobotOrientation.DIRECTION_RIGHT){
                if (currentX + 1 >= level.properties.width || level.tileAt(currentX+1, currentY).type == TileType.OBSTACLE) return possibleDistance
                else {
                    possibleDistance++
                    currentX++
                }
            }
            else if (orientation == RobotOrientation.DIRECTION_DOWN){
                if (currentX - 1 < 0 || level.tileAt(currentX-1, currentY).type == TileType.OBSTACLE) return possibleDistance
                else {
                    possibleDistance++
                    currentX--
                }
            }
        }
        return possibleDistance
    }
}
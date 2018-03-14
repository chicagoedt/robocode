package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotOrientation
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Tiles.TileType

/**
 * Moves the robot by [parameter] tiles in the direction that it's facing
 */
class MoveInstruction : Instruction<Int>() {
    override val name: String = "Move"
    override var parameter = 1

    override fun function(level: Level, robot: RobotPlayer, parameter: Int) {
        val difference = distanceCanMove(robot, parameter, level)
        when (robot.direction){
            RobotOrientation.DIRECTION_UP -> robot.y += difference
            RobotOrientation.DIRECTION_DOWN -> robot.y -= difference
            RobotOrientation.DIRECTION_RIGHT -> robot.x += difference
            RobotOrientation.DIRECTION_LEFT -> robot.x -= difference
        }
    }

    fun distanceCanMove(robot: RobotPlayer, distance: Int, level: Level): Int{
        var currentX = robot.x
        var currentY = robot.y
        var possibleDistance = 0
        for (i in 0..distance-1){
            if (robot.direction == RobotOrientation.DIRECTION_UP){
                if (currentY + 1 >= level.properties.height || level.tileAt(currentX, currentY+1).type == TileType.OBSTACLE) return possibleDistance
                else {
                    possibleDistance++
                    currentY++
                }
            }
            else if (robot.direction == RobotOrientation.DIRECTION_DOWN){
                if (currentY - 1 < 0 || level.tileAt(currentX, currentY-1).type == TileType.OBSTACLE) return possibleDistance
                else {
                    possibleDistance++
                    currentY--
                }
            }
            else if (robot.direction == RobotOrientation.DIRECTION_RIGHT){
                if (currentX + 1 >= level.properties.width || level.tileAt(currentX+1, currentY).type == TileType.OBSTACLE) return possibleDistance
                else {
                    possibleDistance++
                    currentX++
                }
            }
            else if (robot.direction == RobotOrientation.DIRECTION_DOWN){
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
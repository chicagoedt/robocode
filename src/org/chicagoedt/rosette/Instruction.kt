package org.chicagoedt.rosette

internal const val INSTRUCTION_MOVE = 0
internal const val INSTRUCTION_TURN = 1

val availableInstructions = ArrayList<Pair<Int, String>>()

private val instructionList = hashMapOf<Int, (Level, RobotPlayer, Any) -> Unit>()

internal fun loadInstructions(){
    availableInstructions.add(Pair(INSTRUCTION_MOVE, "Move"))
    availableInstructions.add(Pair(INSTRUCTION_TURN, "Turn"))

    for(pair: Pair<Int, String> in availableInstructions){
        if (pair.first == INSTRUCTION_MOVE){
            instructionList[pair.first] = { level, robot, parameter ->
                for (i in 1..parameter as Int) {
                    if (robot.direction == RobotOrientation.DIRECTION_UP && robot.y + 1 < level.properties.height && level.tileAt(robot.x, robot.y + 1).type != TileType.OBSTACLE) robot.y++
                    else if (robot.direction == RobotOrientation.DIRECTION_DOWN && robot.y - 1 >= 0 && level.tileAt(robot.x, robot.y - 1).type != TileType.OBSTACLE) robot.y--
                    else if (robot.direction == RobotOrientation.DIRECTION_LEFT && robot.x - 1 >= 0 && level.tileAt(robot.x - 1, robot.y).type != TileType.OBSTACLE) robot.x--
                    else if (robot.direction == RobotOrientation.DIRECTION_RIGHT && robot.x + 1 < level.properties.width && level.tileAt(robot.x + 1, robot.y).type != TileType.OBSTACLE) robot.x++
                }
            }
        }
        else if (pair.first == INSTRUCTION_TURN){
            instructionList[pair.first] = { level, robot, parameter ->
                robot.direction = nextDirection(robot.direction, parameter as RobotRotation)
            }
        }
    }
}

class Instruction(val type: Int) {
    internal val function = instructionList[type]

    var parameter : Any

    init{
        when(type){
            INSTRUCTION_MOVE -> parameter = 1
            INSTRUCTION_TURN -> parameter = RobotRotation.CLOCKWISE
            else -> parameter = 0
        }
    }
}
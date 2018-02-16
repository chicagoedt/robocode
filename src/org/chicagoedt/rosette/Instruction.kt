package org.chicagoedt.rosette

internal const val INSTRUCTION_MOVE = 0
internal const val INSTRUCTION_TURN = 1

val availableInstructions = ArrayList<Pair<Int, String>>()

private val instructionList = hashMapOf<Int, (ArrayList<ArrayList<Tile>>, RobotPlayer, Any) -> Unit>()

internal fun loadInstructions(){
    availableInstructions.add(Pair(INSTRUCTION_MOVE, "Move"))
    availableInstructions.add(Pair(INSTRUCTION_TURN, "Turn"))

    for(pair: Pair<Int, String> in availableInstructions){
        if (pair.first == INSTRUCTION_MOVE){
            instructionList[pair.first] = { grid, robot, parameter ->
                for (i in 1..parameter as Int) {
                    if (robot.direction == DIRECTION_UP && robot.y + 1 < grid[0].size && grid[robot.x][robot.y + 1].type != TILE_TYPE_OBSTACLE /*height*/) robot.y++
                    else if (robot.direction == DIRECTION_DOWN && robot.y - 1 >= 0 && grid[robot.x][robot.y - 1].type != TILE_TYPE_OBSTACLE /*bottom*/) robot.y--
                    else if (robot.direction == DIRECTION_LEFT && robot.x - 1 >= 0 && grid[robot.x - 1][robot.y].type != TILE_TYPE_OBSTACLE /*left*/) robot.x--
                    else if (robot.direction == DIRECTION_RIGHT && robot.x + 1 < grid.size && grid[robot.x + 1][robot.y].type != TILE_TYPE_OBSTACLE/*left*/) robot.x++
                }
            }
        }
        else if (pair.first == INSTRUCTION_TURN){
            instructionList[pair.first] = { grid, robot, parameter ->
                robot.direction = nextDirection(robot.direction, parameter as Int)
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
            INSTRUCTION_TURN -> parameter = TURN_DIRECTION_CLOCKWISE
            else -> parameter = 0
        }
    }
}
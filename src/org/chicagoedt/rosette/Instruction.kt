package org.chicagoedt.rosette

internal const val INSTRUCTION_MOVE = 0
internal const val INSTRUCTION_TURN = 1

val availableInstructions = ArrayList<Pair<Int, String>>()

private val instructionList = hashMapOf<Int, (ArrayList<ArrayList<Tile>>, RobotPlayer) -> Unit>()

internal fun loadInstructions(){
    availableInstructions.add(Pair(INSTRUCTION_MOVE, "Move"))
    availableInstructions.add(Pair(INSTRUCTION_TURN, "Turn"))

    for(pair: Pair<Int, String> in availableInstructions){
        if (pair.first == INSTRUCTION_MOVE){
            instructionList[pair.first] = { grid, robot ->
                if (robot.direction == DIRECTION_UP && robot.y + 1 < grid[0].size /*height*/) robot.y++
                else if (robot.direction == DIRECTION_DOWN && robot.y - 1 > 0 /*bottom*/) robot.y--
                else if (robot.direction == DIRECTION_LEFT && robot.x - 1 > 0 /*left*/) robot.x--
                else if (robot.direction == DIRECTION_RIGHT && robot.x + 1 < grid.size /*left*/) robot.x++
            }
        }
        else if (pair.first == INSTRUCTION_TURN){
            instructionList[pair.first] = { grid, robot ->
                //only clockwise for now
                when (robot.direction){
                    DIRECTION_UP -> robot.direction = DIRECTION_RIGHT
                    DIRECTION_DOWN -> robot.direction = DIRECTION_LEFT
                    DIRECTION_LEFT -> robot.direction = DIRECTION_UP
                    DIRECTION_RIGHT -> robot.direction = DIRECTION_DOWN
                }
            }
        }
    }
}

class Instruction(val type: Int) {
    internal val function = instructionList[type]

    lateinit var parameter : Any

    init{
        if (type == INSTRUCTION_MOVE) {
            //parameter as Int
            parameter = 1
        }
        else if (type == INSTRUCTION_TURN) {
            //parameter as Int
            parameter = TURN_DIRECTION_CLOCKWISE
        }
        else parameter = 0 //just to initialize it to something, because it's lateinit
    }
}
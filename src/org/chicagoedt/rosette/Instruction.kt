package org.chicagoedt.rosette

val availableInstructions = ArrayList<Pair<Int, String>>()

internal val INSTRUCTION_MOVE = 0;
internal val INSTRUCTION_TURN = 1;

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
                if (robot.direction == DIRECTION_UP) robot.direction = DIRECTION_RIGHT
                else if (robot.direction == DIRECTION_DOWN) robot.direction = DIRECTION_LEFT
                else if (robot.direction == DIRECTION_LEFT) robot.direction = DIRECTION_UP
                else if (robot.direction == DIRECTION_RIGHT) robot.direction = DIRECTION_DOWN
            }
        }
    }
}

class Instruction(val type: Int) {
    val function = instructionList[type]
}
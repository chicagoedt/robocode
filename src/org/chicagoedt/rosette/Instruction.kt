package org.chicagoedt.rosette

val INSTRUCTION_MOVE = 0;
val INSTRUCTION_TURN = 1;

val INSTRUCTION_LIST = listOf<(ArrayList<ArrayList<Tile>>, RobotPlayer) -> Unit>(
        { grid, robot ->
            if (robot.direction == DIRECTION_UP && robot.y + 1 < grid[0].size /*height*/) robot.y++
            else if (robot.direction == DIRECTION_DOWN && robot.y - 1 > 0 /*bottom*/) robot.y--;
            else if (robot.direction == DIRECTION_LEFT && robot.x - 1 > 0 /*left*/) robot.x--;
            else if (robot.direction == DIRECTION_RIGHT && robot.x + 1 < grid.size /*left*/) robot.x++;
        },
        { grid, robot ->
            if (robot.direction == DIRECTION_UP) robot.direction = DIRECTION_RIGHT
            else if (robot.direction == DIRECTION_DOWN) robot.direction = DIRECTION_LEFT
            else if (robot.direction == DIRECTION_LEFT) robot.direction = DIRECTION_UP
            else if (robot.direction == DIRECTION_RIGHT) robot.direction = DIRECTION_DOWN
        })


class Instruction(val type: Int) {
    val function = INSTRUCTION_LIST[type]
}
package org.chicagoedt.rosette

val INSTRUCTION_MOVE = 0;
val INSTRUCTION_TURN = 1;

val instructions = listOf<(ArrayList<ArrayList<Tile>>,
                           RobotPlayer) -> Boolean>({ grid, robot ->
    if (robot.direction == DIRECTION_UP && robot.y + 1 < grid[0].size /*height*/) {robot.y++;true}
    if (robot.direction == DIRECTION_DOWN && robot.y - 1 > 0 /*bottom*/) robot.y++;true
})


class Instruction(val type: Int) {

}
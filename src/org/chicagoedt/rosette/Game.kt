package org.chicagoedt.rosette

class Game (private val levels: HashMap<String, Level>,
            val robots: HashMap<String, Robot>,
            private val levelOrder: ArrayList<String>){
    private var levelNumber = 0
    var currentLevel = levels[levelOrder[levelNumber]]!!

    init{
        loadInstructions()
    }

    fun nextLevel(){
        if (levelOrder.size > levelNumber + 1) {
            levelNumber++
            currentLevel = levels[levelOrder[levelNumber]]!!
        }
    }

    fun attachInstruction(name: String, inst: Instruction){
        currentLevel.players[name]!!.instructions.add(inst)
    }

    fun removeInstruction(name: String, inst: Instruction){
        currentLevel.players[name]!!.instructions.remove(inst)
    }

    fun getInstructions(name: String) : List<Instruction>{
        return currentLevel.players[name]!!.instructions
    }

    fun runInstructionsFor(name: String){
        val robot = currentLevel.players[name]!!
        for(inst: Instruction in robot.instructions){
            inst.function!!.invoke(currentLevel.grid, currentLevel.players[name]!!, inst.parameter)
            if (!checkRobotStatus(name)) break
        }
    }

    private fun checkRobotStatus(name: String): Boolean{
        val robot = currentLevel.players[name]!!
        when (currentLevel.grid[robot.x][robot.y].type){
            TILE_TYPE_VICTORY -> return false
            TILE_TYPE_OBSTACLE -> return false
        }
        return true
    }

}
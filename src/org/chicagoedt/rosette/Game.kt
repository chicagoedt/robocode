package org.chicagoedt.rosette

class Game (val levels: HashMap<String, Level>,
            val robots: HashMap<String, Robot>,
            val levelOrder: ArrayList<String>){
    internal var levelNumber = 0
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
        robots[name]!!.instructions.add(inst)
    }

    fun removeInstruction(name: String, inst: Instruction){
        robots[name]!!.instructions.remove(inst)
    }

    fun getInstructions(name: String) : List<Instruction>{
        return robots[name]!!.instructions
    }

    fun runInstructionsFor(name: String){
        val robot = robots[name]!!
        for(inst: Instruction in robot.instructions){
            inst.function!!.invoke(currentLevel.grid, currentLevel.players[name]!!)
        }
    }

}
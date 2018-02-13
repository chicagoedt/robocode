package org.chicagoedt.rosette

class Game (val levels: HashMap<String, Level>,
            val robots: HashMap<String, Robot>){
    var currentLevel : Level
    internal var levelNumber = 0
    val levelNames = arrayListOf<String>()

    init{
        for (level: MutableMap.MutableEntry<String, Level> in levels){
            levelNames.add(level.key)
        }
        currentLevel = levels[levelNames[levelNumber]]!!
    }

    fun nextLevel(){
        levelNumber++
        currentLevel = levels[levelNames[levelNumber]]!!
    }

    fun attachInstruction(name: String, inst: Instruction){
        robots[name]!!.instructions.add(inst)
    }

    fun getInstructionsFor(name: String) : List<Instruction>{
        return robots[name]!!.instructions
    }

    fun runInstructionsFor(name: String){
        val robot = robots[name]
        for(inst: Instruction in robot!!.instructions){
            INSTRUCTION_LIST[inst.type].invoke(currentLevel.properties.grid, currentLevel.players[name]!!)
        }
    }

}
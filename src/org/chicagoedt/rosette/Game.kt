package org.chicagoedt.rosette

class Game (val levels: HashMap<String, Level>,
            val robots: HashMap<String, Robot>){
    lateinit var currentLevel : Level
    internal var levelNumber = 0

    init{
        //robots.ke
    }

    fun nextLevel(){
        //levelNumber++
        //currentLevel = levels[levelNumber]
    }

    fun attachInstruction(name: String, inst: Instruction){

    }

    fun getInstructionsFor(name: String) : List<Instruction>{
        return listOf()
    }

}
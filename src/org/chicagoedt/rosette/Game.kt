package org.chicagoedt.rosette

import org.chicagoedt.rosette.Instructions.Instruction
import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.Robot
import org.chicagoedt.rosette.Tiles.TileType

enum class Event {
    LEVEL_VICTORY
}

class Game (private val levels: HashMap<String, Level>,
            val robots: HashMap<String, Robot>,
            private val levelOrder: ArrayList<String>){
    private var levelNumber = 0
    var currentLevel = levels[levelOrder[levelNumber]]!!
    private var eventListener: (Event) -> Unit

    init{
        eventListener = {}
    }

    fun attachEventListener(newEventListener: (Event) -> Unit){
        eventListener = newEventListener
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
            inst.function(currentLevel, currentLevel.players[name]!!, inst.parameter)

            //check to see if the player won after the instruction
            if (currentLevel.tileAt(currentLevel.players[name]!!.x, currentLevel.players[name]!!.y).type == TileType.VICTORY){
                eventListener.invoke(Event.LEVEL_VICTORY)
                break
            }
        }
    }
}
package org.chicagoedt.rosette

import org.chicagoedt.rosette.Instructions.Instruction
import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.Robot
import org.chicagoedt.rosette.Tiles.TileType

enum class Event {
    LEVEL_VICTORY
}

class Game (private val levelsList: ArrayList<Level>,
            val robotsList: ArrayList<Robot>){

    private var levelNumber = 0
    private var eventListener: (Event) -> Unit
    private var robots: HashMap<String, Robot> = hashMapOf()

    var currentLevel : Level
    var mainTopic = Topic()

    init{
        eventListener = {}

        for (robot in robotsList){
            robots[robot.name] = robot
        }

       currentLevel = levelsList[levelNumber]
    }

    fun attachEventListener(newEventListener: (Event) -> Unit){
        eventListener = newEventListener
    }

    fun nextLevel(){
        if (levelsList.size > levelNumber + 1) {
            levelNumber++
            currentLevel = levelsList[levelNumber]
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
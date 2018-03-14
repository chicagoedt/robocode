package org.chicagoedt.rosette

import org.chicagoedt.rosette.Instructions.Instruction
import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.Robot
import org.chicagoedt.rosette.Tiles.TileType

enum class Event {
    LEVEL_VICTORY
}

internal var eventListener : (Event) -> Unit = {}

class Game (private val levelsList: ArrayList<Level>,
            val robotsList: ArrayList<Robot>){

    private var levelNumber = 0
    private var robots: HashMap<String, Robot> = hashMapOf()

    var currentLevel : Level
    var mainTopic = Topic()

    init{
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
}
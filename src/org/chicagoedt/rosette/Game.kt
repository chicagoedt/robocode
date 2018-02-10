package org.chicagoedt.rosette

class Game (val levels: ArrayList<Level>,
            val robots: ArrayList<Robot>){
    var currentLevel = levels[0]
    internal var levelNumber = 0

    fun nextLevel(){
        levelNumber++
        currentLevel = levels[levelNumber]
    }
}
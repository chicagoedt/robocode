package org.chicagoedt.rosette

import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.Robot

/**
 * A signal to communicate with the client running the program
 * @property LEVEL_VICTORY A robot has reached a victory tile
 */
enum class Event {
    LEVEL_VICTORY
}

/**
 * A callback lambda when an event occurs
 */
internal var eventListener : (Event) -> Unit = {}

/**
 * The main class to start a game of rosette
 * @param levelsList A list of all levels in the game, in order
 * @param robotsList A list of all robots in the game
 * @property robots All of the robots in the entire game
 * @property currentLevel The current level to be displayed and interacted with by the user
 * @property mainTopic The main topic for the game. This should be used to store and read sensor data
 */
class Game (private val levelsList: ArrayList<Level>,
            val robotsList: ArrayList<Robot>){

    /**
     * The current level number. Starts at 0
     */
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

    /**
     * Replaces the current event listener.
     *
     * @param newEventListener A lambda to be called when an occurs. The parameter is the event occurring
     */
    fun attachEventListener(newEventListener: (Event) -> Unit){
        eventListener = newEventListener
    }

    /**
     * Moves the game on to the next level
     */
    fun nextLevel(){
        if (levelsList.size > levelNumber + 1) {
            levelNumber++
            currentLevel = levelsList[levelNumber]
        }
    }
}
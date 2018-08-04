package org.chicagoedt.robocode

import org.chicagoedt.robocode.collectibles.Collectible
import org.chicagoedt.robocode.collectibles.ItemManager
import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.Robot

/**
 * A signal to communicate with the client running the program
 * @property LEVEL_VICTORY A robot has achieved the victory condition for the level
 * @property LEVEL_UPDATE The game board has been updated (and the UI needs to reflect the update)
 * @property LEVEL_FAILURE The robot  has finished its run and has not reached victory
 * @property ACTION_ADDED An action was added to a robot
 * @property ACTION_REMOVED An action was removed from a robot
 * @property ROBOT_RUN_START A robot has started running its actions
 * @property ROBOT_RUN_END A robot has finished running its actions
 */
enum class Event {
    LEVEL_VICTORY,
    LEVEL_UPDATE,
    LEVEL_FAILURE,
    ACTION_ADDED,
    ACTION_REMOVED,
    ROBOT_RUN_START,
    ROBOT_RUN_END
}

/**
 * The main topic used for the program
 */
var mainTopic = Topic()

/**
 * A callback lambda when an event occurs
 */
internal var eventListeners : ArrayList<(Event) -> Unit> = arrayListOf()

/**
 * Calls all event listeners with the specified event
 * @param event The event to call all listeners with
 */
internal fun broadcastEvent(event : Event){
    for (listener in eventListeners){
        listener(event)
    }
}

/**
 * The main class to start a game of robocode
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
    var robots: HashMap<String, Robot> = hashMapOf()

    var currentLevel : Level

    var currentLevelNumber = 0
        get() {
            return levelNumber + 1
        }


    init{
        for (robot in robotsList){
            robots[robot.name] = robot
        }

       currentLevel = levelsList[levelNumber]
    }

    /**
     * Adds a listener for all game events
     * @param newEventListener A lambda to be called when an occurs. The parameter is the event occurring
     */
    fun attachEventListener(newEventListener: (Event) -> Unit){
        eventListeners.add(newEventListener)
    }

    /**
     * Removes a listener from receiving events
     * @param eventListener A lambda that is already an event listener
     */
    fun removeEventListener(newEventListener: (Event) -> Unit){
        eventListeners.remove(newEventListener)
    }

    /**
     * @param levelnum The level number to jump to
     * @return True if the jump was successful, false otherwise
     */
    fun jumpToLevel(levelnum : Int) : Boolean{
        if (levelnum - 1 < levelsList.size){
            levelNumber = levelnum - 1
            currentLevel = levelsList[levelNumber]
            return true
        }
        return false
    }

    /**
     * Moves the game on to the next level
     */
    fun nextLevel(){
        if (hasNextLevel()) {
            levelNumber++
            currentLevel = levelsList[levelNumber]
        }
    }

    /**
     * @return True if the game has another level, false otherwise
     */
    fun hasNextLevel() : Boolean{
        return (levelsList.size > levelNumber + 1)
    }

    /**
     * Adds an item to the list of available items in the game
     */
    fun addItem(item : Collectible){
        ItemManager.addItem(item)
    }
}
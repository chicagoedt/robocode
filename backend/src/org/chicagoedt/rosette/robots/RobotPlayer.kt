package org.chicagoedt.rosette.robots

import org.chicagoedt.rosette.actions.*
import org.chicagoedt.rosette.sensors.*
import org.chicagoedt.rosette.levels.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.collectibles.ItemInventory
import org.chicagoedt.rosette.tiles.*

enum class RobotPosition{
    FRONT,
    LEFT,
    RIGHT,
    BACK
}

/**
 * A robot specific to a level
 * @param name The name of the robot, corresponding to a Robot name passed to Game
 * @param x The starting X value of the robot
 * @param y The starting Y value of the robot
 * @param direction The starting orientation of the robot
 * @param level The level that this robot is contained in
 * @property procedure A sequential list of actions to run assigned to the robot
 * @property sensors All of the sensors attached to the robot
 * @property sensorCounts The number of sensors available on each side
 * @property itemInventory List of collectible items in the robot's possession
 * @property checkpointX The X value at the last checkpoint
 * @property checkpointY The Y value at the last checkpoint
 * @property checkpointDirection The direction at the last checkpoint
 * @property victorious A variable to track whether the player was victorious in their current run
 */
class RobotPlayer(val name: String,
                  var x: Int,
                  var y: Int,
                  var direction: RobotOrientation,
                  var level : Level){

    internal val procedure = arrayListOf<Action<Any>>()
    private val sensors = hashMapOf<RobotPosition, MutableList<Sensor>>()
    private val sensorCounts = hashMapOf<RobotPosition, Int>()
    val itemInventory = ItemInventory()
    private var checkpointX = x
    private var checkpointY = y
    private var checkpointDirection = direction
    private var victorious = false;

    init{
        setSensorCountAt(RobotPosition.FRONT, 1)
        setSensorCountAt(RobotPosition.BACK, 1)
        setSensorCountAt(RobotPosition.LEFT, 1)
        setSensorCountAt(RobotPosition.RIGHT, 1)
    }

    /**
     * Restores the state of the robot from the checkpoint
     */
    fun restoreCheckpoint(){
        x = checkpointX
        y = checkpointY
        direction = checkpointDirection
        itemInventory.restoreCheckpoint()
    }

    /**
     * Saves the state of the robot to the checkpoint
     */
    fun saveCheckpoint(){
        checkpointX = x
        checkpointY = y
        checkpointDirection = direction
        itemInventory.saveCheckpoint()
    }

    /**
     * @return The number of sensors available on a side
     */
    fun sensorCountAt(pos: RobotPosition) : Int {
        return sensorCounts[pos]!!
    }

    /**
     * Sets the number of sensors available on a side
     * @param pos The side to modify the sensor count for
     * @param count The new sensor count
     */
    fun setSensorCountAt(pos: RobotPosition, count : Int) {
        if (sensorCounts[pos] == null) {
            sensorCounts[pos] = count
            sensors[pos] = mutableListOf()
        }
        else {
            val currentCount = sensorCounts[pos]!!
            sensorCounts[pos] = count
            val actualSize = sensors[pos]!!.size

            if (count < currentCount && count < actualSize) {
                for (i in count until actualSize) {
                    sensors[pos]!!.removeAt(i)
                }
            }
        }
    }

    /**
     * Attaches a sensor to the robot
     * @param pos The side to add the sensor to
     * @param sensor The sensor to attach
     * @return True if the attach succeeded, false if it didn't
     */
    fun addSensorTo(pos: RobotPosition, sensor: Sensor): Boolean {
        if (sensors[pos]!!.size + 1 <= sensorCounts[pos]!!) {
            sensors[pos]!!.add(sensor)
            return true
        }
        return false
    }

    /**
     * Removes a sensor from a side
     * @param pos The side to remove the sensor from
     * @param sensor The sensor to remove
     */
    fun removeSensorFrom(pos: RobotPosition, sensor: Sensor) {
        sensors[pos]!!.remove(sensor)
    }

    /**
     * Removes a sensor from a side
     * @param pos The side to remove the sensor from
     * @param index The index of the sensor to remove
     */
    fun removeSensorFrom(pos: RobotPosition, index: Int) {
        sensors[pos]!!.removeAt(index)
    }

    /**
     * @param pos The side to retrieve the sensors from
     * @return A list, in order, of all sensors on a side
     */
    fun getSensors(pos: RobotPosition): ArrayList<Sensor> {
        val list = arrayListOf<Sensor>()
        list.addAll(sensors[pos]!!)
        if (list.size < sensorCountAt(pos)){
            for (i in list.size until sensorCountAt(pos)) {
                list.add(EmptySensor())
            }
        }
        return list
    }

    /**
     * Adds an action to a robot's procedure
     * @param action The action to append
     */
    fun appendAction(action: Action<*>){
        procedure.add(action as Action<Any>)
    }

    /**
     * Removes an action from a robot's procedure
     * @param name The name of the robot to remove the action from
     * @param action The action to remove from the robot
     */
    fun removeAction(action: Action<*>){
        procedure.remove(action)
    }

    /**
     * @param name The name of the robot to retrieve the procedure
     * @return A list of actions describing the robot's procedure
     */
    fun getProcedure() : List<Action<*>>{
        return procedure
    }

    /**
     * Runs the action at the specified index
     * @param i The index of the action to run
     * @param reset True if the grid should reset if finished, false otherwise
     */
    fun runAction(i : Int, reset : Boolean){
        if (i == procedure.size){
            if (!victorious){
                if (reset) level.restoreCheckpoint()
                eventListener.invoke(Event.LEVEL_FAILURE)
            }
            victorious = false
            return
        }
        val action = procedure[i]
        action.function(level, this, action.parameter)

        checkForVictory()        
    }

    /**
     * Executes procedure assigned to a robot
     * @param reset true if the level should reset afterwards, false otherwise
     * @param run The lambda to run the action. Necessary to set an interval. The parameter is the actual run function for each action, it must be called
     * @param clear The function to call once all intervals have been run
     */
    fun runInstructions(reset : Boolean, run: (() -> Unit) -> Unit, clear: () -> Unit){
        var i = 0;
        var currentMacroIndex = 0
        val runNextAction = {
            if (i < procedure.size){
                val action = procedure[i]
                if (action is ActionMacro){
                    action.runMacroAt(currentMacroIndex, level, this)
                    currentMacroIndex++
                    if (currentMacroIndex == action.getMacro().size){
                        i++
                        currentMacroIndex = 0
                        checkForVictory()
                    }
                }
                else{
                    runAction(i, reset)
                    i++
                }
            }
            else{
                runAction(i, reset)
                i++
                clear()
            }

            eventListener.invoke(Event.LEVEL_UPDATE)
            
        }
        runNextAction()
        run(runNextAction)
    }

    /**
     * Executes procedure assigned to a robot
     * @param reset true if the level should reset afterwards, false otherwise
     */
    fun runInstructions(reset : Boolean){
        level.saveCheckpoint()
        for (i in  0..procedure.size){
            runAction(i, reset)
        }
    }

    /**
     * If the player is on a victory tile, this calls the proper event and set [victorious] to true
     */
    fun checkForVictory(){
        if (level.tileAt(x, y) is VictoryTile){
            eventListener.invoke(Event.LEVEL_VICTORY)
            victorious = true
        }
    }
}
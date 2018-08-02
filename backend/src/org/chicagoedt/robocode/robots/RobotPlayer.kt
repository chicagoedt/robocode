package org.chicagoedt.robocode.robots

import org.chicagoedt.robocode.actions.*
import org.chicagoedt.robocode.sensors.*
import org.chicagoedt.robocode.levels.*
import org.chicagoedt.robocode.*
import org.chicagoedt.robocode.actions.robotActions.MoveActionMacro
import org.chicagoedt.robocode.collectibles.ItemInventory
import org.chicagoedt.robocode.tiles.*

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
 * @property actionLimit The limit on the number of actions that this robot can have. -1 for unlimited
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
    var actionLimit = -1

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
            sensor.player = this
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
        sensor.player = null
        sensors[pos]!!.remove(sensor)
    }

    /**
     * Removes a sensor from a side
     * @param pos The side to remove the sensor from
     * @param index The index of the sensor to remove
     */
    fun removeSensorFrom(pos: RobotPosition, index: Int) {
        sensors[pos]!![index].player = null
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
     * @param pos The side to retrieve the sensors from
     * @return A list, in order, of all sensors on a side, excluding the empty sensor
     */
    fun getSensorsExceptEmpty(pos: RobotPosition): ArrayList<Sensor> {
        val list = arrayListOf<Sensor>()
        list.addAll(sensors[pos]!!)
        return list
    }

    /**
     * Adds an action to a robot's procedure
     * @param action The action to append
     * @return True if the action was appended, false if the [actionLimit] was reached
     */
    fun appendAction(action: Action<*>) : Boolean{
        return insertAction(action, procedure.size)
    }

    /**
     * Inserts an action into a robot's procedure at a specific spot
     * @return True if the action was appended, false if the [actionLimit] was reached
     * @param action The action to append
     */
    fun insertAction(action: Action<*>, i : Int) : Boolean{
        var totalSize = getFullProcedureSize(procedure)
        if (action is ActionMacro){
            try{
                if (action as ActionMacro<Int> !is MoveActionMacro)
                    totalSize += getFullProcedureSize(action.getMacro())
            }
            catch (e : ClassCastException){
                totalSize += getFullProcedureSize(action.getMacro())
            }
        }

        if (actionLimit == -1 || totalSize < actionLimit){
            if (i > procedure.size - 1){
                procedure.add(action as Action<Any>)
            }
            else{
                procedure.add(i, action as Action<Any>)
            }
            broadcastEvent(Event.ACTION_ADDED)
            return true
        }
        return false
    }

    /**
     * Removes an action from a robot's procedure
     * @param name The name of the robot to remove the action from
     * @param action The action to remove from the robot
     */
    fun removeAction(action: Action<*>){
        procedure.remove(action)
        broadcastEvent(Event.ACTION_REMOVED)
    }

    /**
     * @return The limit difference available for an ActionMacro
     */
    fun getLimitDifference() : Int{
        if (actionLimit != -1) return actionLimit - getFullProcedureSize(getProcedure())
        else return -1
    }

    /**
     * @return A list of actions describing the robot's procedure
     */
    fun getFullProcedureSize(actions : List<Action<*>>) : Int{
        var size = 0
        for(action in actions){
            size += 1
            if (action is ActionMacro){
                try{
                    if (action as ActionMacro<Int> !is MoveActionMacro)
                        size += getFullProcedureSize(action.getMacro())
                }
                catch (e : ClassCastException){

                }

            }
        }
        return size
    }

    /**
     * @return A list of actions describing the robot's procedure
     */
    fun getProcedure() : List<Action<*>>{
        return procedure
    }

    /**
     * Interprets an action, including macros, when running a list
     * @param action The action to interpret
     * @param fullProcedure The list currently being run
     * @return True if an action was run, false otherwise
     */
    fun interpretAction(action: Action<Any>, fullProcedure : ArrayList<Action<Any>>) : Boolean{
        if (action is ActionMacro){
            val index = fullProcedure.indexOf(action)
            val additions = action.getActualMacro()
            fullProcedure.addAll(index + 1, additions)
            return false
        }
        else {
            action.function(level, this, action.parameter)
            return true
        }
    }

    /**
     * Executes procedure assigned to a robot
     * @param reset true if the level should reset afterwards, false otherwise
     * @param run The lambda to run the action. Necessary to set an interval. The parameter is the actual run function for each action, it must be called
     * @param clear The function to call once all intervals have been run
     */
    fun runInstructions(reset : Boolean, run: (() -> Unit) -> Unit, clear: () -> Unit){
        broadcastEvent(Event.ROBOT_RUN_START)
        val procedureCopy = arrayListOf<Action<Any>>()
        procedureCopy.addAll(getProcedure() as ArrayList<Action<Any>>)
        level.saveCheckpoint()
        var i = 0
        val runNextAction = {
            var actuallyRun = true
            do{
                actuallyRun = true
                if (i < procedureCopy.size){
                    actuallyRun = interpretAction(procedureCopy[i] as Action<Any>, procedureCopy)
                    broadcastEvent(Event.LEVEL_UPDATE)
                }
                else{
                    handleEndOfRun(reset)
                    clear()
                }

                i++
            } while(!actuallyRun)
        }
        runNextAction()
        //console.log(procedureCopy)
        if (procedureCopy.size > 0) run(runNextAction)
    }

    /**
     * Executes procedure assigned to a robot
     * @param reset true if the level should reset afterwards, false otherwise
     */
    fun runInstructions(reset : Boolean){
        var shouldRun = true
        val toRun = {runner : () -> Unit->
            while(shouldRun){
                runner()
            }
        }
        val clear = {shouldRun = false}

        runInstructions(reset, toRun, clear)
    }

    /**
     * Handles the events for the end of the run
     * @param reset True to revert to checkpoint if not victorious, false otherwise
     */
    fun handleEndOfRun(reset : Boolean){
        val victory = level.checkForVictory(this)
        if (victory){
            broadcastEvent(Event.LEVEL_VICTORY)
        }
        else{
            if (reset){
                level.restoreCheckpoint()
                mainTopic.reset()
            }
            broadcastEvent(Event.LEVEL_FAILURE)
        }
        broadcastEvent(Event.ROBOT_RUN_END)
    }
}
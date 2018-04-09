package org.chicagoedt.rosette.robots

import org.chicagoedt.rosette.actions.Action
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

    init{
        setSensorCountAt(RobotPosition.FRONT, 1)
        setSensorCountAt(RobotPosition.BACK, 1)
        setSensorCountAt(RobotPosition.LEFT, 1)
        setSensorCountAt(RobotPosition.RIGHT, 1)
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
     * Executes procedure assigned to a robot
     * @param name The name of the robot to run the procedure for
     */
    fun runInstructions(){
        for(action: Action<Any> in procedure){
            action.function(level, this, action.parameter)

            //check to see if the player won after the instruction
            if (level.tileAt(x, y) is VictoryTile){
                eventListener.invoke(Event.LEVEL_VICTORY)
                break
            }
        }
        eventListener.invoke(Event.LEVEL_UPDATE)
    }
}
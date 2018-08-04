package org.chicagoedt.robocode.actions

import org.chicagoedt.robocode.Event
import org.chicagoedt.robocode.actions.robotActions.MoveActionMacro
import org.chicagoedt.robocode.broadcastEvent
import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.RobotPlayer

/**
 * An action with a macro of actions to be run
 * @property macro The list of actions describing the macro
 */
abstract class ActionMacro<T> : Action<T>() {
    internal val macro = mutableListOf<Action<Any>>()

    override fun function(level: Level, robot: RobotPlayer, parameter: T){
        val list = getActualMacro()
        for (action in list){
            action.function(level, robot, parameter as Any)
        }
    }

    /**
     * @return A list of actions describing the robot's procedure
     */
    fun getFullMacroSize(actions : List<Action<*>>) : Int{
        var size = 0
        for(action in actions){
            size += 1
            if (action is ActionMacro){
                try{
                    if (action as ActionMacro<Int> !is MoveActionMacro)
                        size += getFullMacroSize(action.getMacro())
                }
                catch (e : ClassCastException){
                    size += getFullMacroSize(action.getMacro())
                }

            }
        }
        return size
    }

    /**
     * Adds an action to the macro
     * @param action The action to add to the macro's list
     * @param actionsToLimit The number of action until the limit is reached. -1 for unlimited
     * @return True if the action was appended, false if the [actionLimit] was reached
     */
    fun addToMacro(action: Action<*>, actionsToLimit : Int) : Boolean{
        return addToMacroAt(action, macro.size, actionsToLimit)
    }

    /**
     * Adds an action to the macro
     * @param action The action to add to the macro's list
     * @param pos The position to add the action at
     * @param actionsToLimit The number of action until the limit is reached. -1 for unlimited
     * @return True if the action was appended, false if the [actionLimit] was reached
     */
    fun addToMacroAt(action: Action<*>, pos : Int, actionsToLimit : Int) : Boolean{
        var totalSize = 1
        if (action is ActionMacro){
            try{
                if (action as ActionMacro<Int> !is MoveActionMacro)
                    totalSize += getFullMacroSize(action.getMacro())
            }
            catch (e : ClassCastException){
                totalSize += getFullMacroSize(action.getMacro())
            }
        }

        if (actionsToLimit == -1 || totalSize < actionsToLimit){
            if (pos < macro.size) macro.add(pos, action as Action<Any>)
            else macro.add(action as Action<Any>)
            broadcastEvent(Event.ACTION_ADDED)
            return true
        }
        return false
    }

    /**
     * Removes an action from the macro
     * @param action The action to remove from the macro's list
     */
    fun removeFromMacro(action: Action<*>){
        macro.remove(action)
        broadcastEvent(Event.ACTION_REMOVED)
    }

    /**
     * Removes an action from the macro
     * @param i The index of the action to remove from the macro
     */
    fun removeFromMacroAt(i : Int){
        macro.removeAt(i)
        broadcastEvent(Event.ACTION_REMOVED)
    }

    /**
     * @return A sequential list of the macro's actions
     */
    fun getMacro() : ArrayList<Action<Any>>{
        val arrayList = arrayListOf<Action<Any>>()
        arrayList.addAll(macro)
        return arrayList
    }

    /**
     * @return A sequential list of the macro's actions to necessarily be run. For example, an empty list if a conditional is false
     */
    abstract fun getActualMacro() : ArrayList<Action<Any>>

    /**
     * Executes the macro
     * @param level The current level to execute the macro on
     * @param robot The robot to run the macro on
     */
    internal fun runMacro(level: Level, robot: RobotPlayer) {
        for (action in macro){
            action.function(level, robot, action.parameter)
        }
    }

    /**
     * Runs a specific action in the macro
     * @param i The index of the action to run
     */
    internal fun runMacroAt(i : Int, level: Level, robot: RobotPlayer){
        macro[i].function(level, robot, macro[i].parameter)
    }
}
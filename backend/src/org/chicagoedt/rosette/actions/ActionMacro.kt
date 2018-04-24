package org.chicagoedt.rosette.actions

import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer

/**
 * An action with a macro of actions to be run
 * @property macro The list of actions describing the macro
 */
abstract class ActionMacro<T> : Action<T>() {
    internal val macro = mutableListOf<Action<Any>>()

    /**
     * Adds an action to the macro
     * @param action The action to add to the macro's list
     */
    internal fun addToMacro(action: Action<*>){
        macro.add(action as Action<Any>)
    }

    /**
     * Removes an action from the macro
     * @param action The action to remove from the macro's list
     */
    internal fun removeFromMacro(action: Action<*>){
        macro.remove(action)
    }

    /**
     * Removes an action from the macro
     * @param i The index of the action to remove from the macro
     */
    internal fun removeFromMacroAt(i : Int){
        macro.removeAt(i)
    }

    /**
     * @return A sequential list of the macro's actions
     */
    internal fun getMacro() : ArrayList<Action<*>>{
        val arrayList = arrayListOf<Action<*>>()
        arrayList.addAll(macro)
        return arrayList
    }

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
package org.chicagoedt.rosette.actions

import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer

/**
 * An instruction with a list of sub-instructions to be run
 * @property list The list of instructions contained in this instruction
 */
abstract class ActionWithList<T> : Action<T>() {
    internal val list = mutableListOf<Action<Any>>()

    /**
     * Adds an action to the list
     * @param action The action to add to the list
     */
    internal fun addToList(action: Action<*>){
        list.add(action as Action<Any>)
    }

    /**
     * Removes an action from the list
     * @param action The action to remove from the list
     */
    internal fun removeFromList(action: Action<*>){
        list.remove(action)
    }

    /**
     * Removes an instruction from the list
     * @param i The index of the instruction to remove from the list
     */
    internal fun removeFromListAt(i : Int){
        list.removeAt(i)
    }

    /**
     * @return The list of instructions
     */
    internal fun getList() : ArrayList<Action<*>>{
        val arrayList = arrayListOf<Action<*>>()
        arrayList.addAll(list)
        return arrayList
    }

    /**
     * Executes the list of instructions
     * @param level The current level to execute the instructions on
     * @param robot The robot to run the instructions on
     */
    internal fun runList(level: Level, robot: RobotPlayer) {
        for (instruction in list){
            instruction.function(level, robot, instruction.parameter)
        }
    }
}
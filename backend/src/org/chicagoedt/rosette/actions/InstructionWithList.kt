package org.chicagoedt.rosette.actions

import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer

/**
 * An instruction with a list of sub-instructions to be run
 * @property list The list of instructions contained in this instruction
 */
abstract class InstructionWithList<T> : Instruction<T>() {
    internal val list = mutableListOf<Instruction<Any>>()

    /**
     * Adds an instruction to the list
     * @param instruction The instruction to add to the list
     */
    internal fun addToList(instruction: Instruction<*>){
        list.add(instruction as Instruction<Any>)
    }

    /**
     * Removes an instruction from the list
     * @param instruction The instruction to remove from the list
     */
    internal fun removeFromList(instruction: Instruction<*>){
        list.remove(instruction)
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
    internal fun getList() : ArrayList<Instruction<*>>{
        val arrayList = arrayListOf<Instruction<*>>()
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
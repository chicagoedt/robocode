package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer

abstract class InstructionWithList<T> : Instruction<T>() {
    val list = mutableListOf<Instruction<*>>()

    fun addToList(instruction: Instruction<*>){
        list.add(instruction)
    }

    fun removeFromList(instruction: Instruction<*>){
        list.remove(instruction)
    }

    fun removeFromListAt(i : Int){
        list.removeAt(i)
    }

    fun getList() : ArrayList<Instruction<*>>{
        val arrayList = arrayListOf<Instruction<*>>()
        arrayList.addAll(list)
        return arrayList
    }

    fun runList(level: Level, robot: RobotPlayer) {
        for (instruction in list){
            instruction.function(level, robot, instruction.parameter!!)
        }
    }
}
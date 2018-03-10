package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer

abstract class InstructionWithList : Instruction {
    val list = mutableListOf<Instruction>()

    fun addToList(instruction: Instruction){
        list.add(instruction)
    }

    fun removeFromList(instruction: Instruction){
        list.remove(instruction)
    }

    fun removeFromListAt(i : Int){
        list.removeAt(i)
    }

    fun getList() : ArrayList<Instruction>{
        val arrayList = arrayListOf<Instruction>()
        arrayList.addAll(list)
        return arrayList
    }

    override fun function(level: Level, robot: RobotPlayer, parameter: Any) {
        for (instruction in list){
            instruction.function(level, robot, parameter)
        }
    }
}
package org.chicagoedt.rosette.Robots

import org.chicagoedt.rosette.Instructions.Instruction

class RobotPlayer(val name: String,
                  var x: Int,
                  var y: Int,
                  var direction: RobotOrientation){

    val instructions = arrayListOf<Instruction>()
}
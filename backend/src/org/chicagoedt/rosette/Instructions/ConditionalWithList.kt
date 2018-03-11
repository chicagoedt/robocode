package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Instructions.Operations.Comparison
import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer

class ConditionalWithList() : InstructionWithList(){
    lateinit override var parameter: Any
    override val name = "Conditional"

    override fun function(level: Level, robot: RobotPlayer, parameter: Any) {
        if (parameter is Comparison<*,*>){
            if (parameter.result()){
                runList(level, robot)
            }
        }
    }
}
package org.chicagoedt.rosette.Instructions

import org.chicagoedt.rosette.Instructions.Operations.Comparison
import org.chicagoedt.rosette.Levels.Level
import org.chicagoedt.rosette.Robots.RobotPlayer

/**
 * Executes the list of instructions if the Comparison [parameter] results in true
 */
class ConditionalWithList() : InstructionWithList<Comparison<*,*>>(){
    lateinit override var parameter : Comparison<*,*>
    override val name = "Conditional"

    override fun function(level: Level, robot: RobotPlayer, parameter: Comparison<*,*>) {
        if (parameter.result()) {
            runList(level, robot)
        }
    }
}
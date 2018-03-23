package org.chicagoedt.rosette.actions

import org.chicagoedt.rosette.actions.operations.Comparison
import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer

/**
 * Executes the list of instructions if the Comparison [parameter] results in true
 */
class ConditionalWithList() : ActionWithList<Comparison<*,*>>(){
    lateinit override var parameter : Comparison<*,*>
    override val name = "Conditional"

    override fun function(level: Level, robot: RobotPlayer, parameter: Comparison<*,*>) {
        if (parameter.result()) {
            runList(level, robot)
        }
    }
}
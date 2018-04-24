package org.chicagoedt.rosette.actions

import org.chicagoedt.rosette.actions.operations.Comparison
import org.chicagoedt.rosette.levels.Level
import org.chicagoedt.rosette.robots.RobotPlayer

/**
 * Executes the action macro if the Comparison [parameter] results in true
 */
class ConditionalWithList: ActionMacro<Comparison<*,*>>(){
    lateinit override var parameter : Comparison<*,*>
    override val name = "Conditional"

    override fun getActualMacro() : ArrayList<Action<Any>>{
    	if (parameter.result()){
    		return getMacro()
    	}
    	else return arrayListOf<Action<Any>>()
    }
}
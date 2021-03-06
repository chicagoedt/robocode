package org.chicagoedt.robocode.actions

import org.chicagoedt.robocode.actions.operations.Comparison

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
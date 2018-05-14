package org.chicagoedt.robocode.actions.robotActions

import org.chicagoedt.robocode.actions.Action
import org.chicagoedt.robocode.actions.ActionMacro

/**
 * Executes the action macro [parameter] times
 */
class ForLoopAction: ActionMacro<Int>(){
    override var parameter = 0
    override val name = "For Loop"

    override fun getActualMacro() : ArrayList<Action<Any>>{
        val list = arrayListOf<Action<Any>>()
        for (i in 0 until parameter){
            for (action in getMacro()){
                if (action is ActionMacro){
                    list.addAll(action.getActualMacro())
                }
                else list.add(action)
            }
        }
        return list
    }
}
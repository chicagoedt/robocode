package org.chicagoedt.robocode.actions.robotActions

import org.chicagoedt.robocode.actions.*
import org.chicagoedt.robocode.levels.Level
import org.chicagoedt.robocode.robots.RobotOrientation
import org.chicagoedt.robocode.tiles.TileType

/**
 * Moves the robot by [parameter] tiles in the direction that it's facing
 */
class MoveActionMacro : ActionMacro<Int>() {
    override val name: String = "Move"
    override var parameter = 1
        set(value){
            field = value
            configureMoveMacro()
        }

    init{
        addToMacro(MoveAction(), -1)
    }

    override fun getActualMacro() : ArrayList<Action<Any>>{
        return getMacro()
    }

    private fun configureMoveMacro(){
        val count = parameter - 1
        if (count < getMacro().size){
            val size = getMacro().size
            for (i in size - 1 downTo count + 1){
                removeFromMacroAt(i)
            }
        }
        else if (count >= getMacro().size){
            for (i in count downTo getMacro().size){
                addToMacro(MoveAction(), -1)
            }
        }
    }
}
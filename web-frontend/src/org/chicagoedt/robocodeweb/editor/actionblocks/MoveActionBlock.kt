package org.chicagoedt.robocodeweb.editor.actionblocks

import org.chicagoedt.robocode.actions.robotActions.*
import org.chicagoedt.robocodeweb.editor.*

/**
 * The ActionBlock representing the MoveAction
 */
class MoveActionBlock() : ActionBlock<MoveActionMacro>(){
	override val action = MoveActionMacro()

	init{
		parameterType = BlockParameterType.NUMBER_INPUT
		blockClass = "movementBlock"
		for (i in 1..10){
			insertDropdownParameter(i.toString(), i.toInt())
		}
	}
}

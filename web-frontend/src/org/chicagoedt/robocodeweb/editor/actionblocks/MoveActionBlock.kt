package org.chicagoedt.robocodeweb.editor.actionblocks

import org.chicagoedt.robocode.actions.robotActions.*
import org.chicagoedt.robocodeweb.editor.*

/**
 * The ActionBlock representing the MoveAction
 */
class MoveActionBlock() : ActionBlock<MoveActionMacro>(){
	override val action = MoveActionMacro()
	override val hasParameters = true;

	init{
		for (i in 1..10){
			insertParameter(i.toString(), i.toInt())
		}
	}
}

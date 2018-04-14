package org.chicagoedt.rosetteweb.editor.actionblocks

import org.chicagoedt.rosette.actions.robotActions.*
import org.chicagoedt.rosetteweb.editor.*

/**
 * The ActionBlock representing the TurnAction
 */
class TurnActionBlock() : ActionBlock<TurnAction>(){
	override val action = TurnAction()

	init{
		element.appendChild(getName())
	}
}

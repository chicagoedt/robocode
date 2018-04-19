package org.chicagoedt.rosetteweb.editor.actionblocks

import org.chicagoedt.rosette.actions.robotActions.*
import org.chicagoedt.rosetteweb.editor.*

/**
 * The ActionBlock representing the MoveAction
 */
class MoveActionBlock() : ActionBlock<MoveAction>(){
	override val action = MoveAction()
}

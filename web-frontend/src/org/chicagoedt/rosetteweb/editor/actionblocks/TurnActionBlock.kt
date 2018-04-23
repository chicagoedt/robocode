package org.chicagoedt.rosetteweb.editor.actionblocks

import org.chicagoedt.rosette.actions.robotActions.*
import org.chicagoedt.rosette.robots.RobotRotation
import org.chicagoedt.rosetteweb.editor.*

/**
 * The ActionBlock representing the TurnAction
 */
class TurnActionBlock() : ActionBlock<TurnAction>(){
	override val action = TurnAction()
	override val hasParameters = true

	init{
		insertParameter("Clockwise", RobotRotation.CLOCKWISE)
		insertParameter("Counter-Clockwise", RobotRotation.COUNTERCLOCKWISE)
	}
}

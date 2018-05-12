package org.chicagoedt.robocodeweb.editor.actionblocks

import org.chicagoedt.robocode.actions.robotActions.*
import org.chicagoedt.robocode.robots.RobotRotation
import org.chicagoedt.robocodeweb.editor.*

/**
 * The ActionBlock representing the TurnAction
 */
class TurnActionBlock() : ActionBlock<TurnAction>(){
	override val action = TurnAction()
	override val hasParameters = true

	init{
		element.classList.add("movementBlock")
		insertParameter("Clockwise", RobotRotation.CLOCKWISE)
		insertParameter("Counter-Clockwise", RobotRotation.COUNTERCLOCKWISE)
	}
}

package org.chicagoedt.robocodeweb.editor.actionblocks

import org.chicagoedt.robocode.actions.robotActions.*
import org.chicagoedt.robocode.robots.RobotRotation
import org.chicagoedt.robocodeweb.editor.*

/**
 * The ActionBlock representing the TurnAction
 */
class TurnActionBlock() : ActionBlock<TurnAction>(){
	override val action = TurnAction()

	init{
		parameterType = BlockParameterType.DROPDOWN
		element.classList.add("movementBlock")
		insertDropdownParameter("Clockwise", RobotRotation.CLOCKWISE)
		insertDropdownParameter("Counter-Clockwise", RobotRotation.COUNTERCLOCKWISE)
	}
}

package org.chicagoedt.robocodeweb.editor.actionblocks

import org.chicagoedt.robocode.actions.robotActions.*
import org.chicagoedt.robocode.robots.RobotRotation
import org.chicagoedt.robocodeweb.editor.*
import kotlin.dom.addClass

/**
 * The ActionBlock representing the TurnAction
 */
class TurnActionBlock() : ActionBlock<TurnAction>(){
	override val action = TurnAction()

	init{
		parameterType = BlockParameterType.DROPDOWN
		blockClass = "movementBlock"
		element.addClass("turnActionBlock")
		insertDropdownParameter("Clockwise", RobotRotation.CLOCKWISE)
		insertDropdownParameter("Counter-Clockwise", RobotRotation.COUNTERCLOCKWISE)
	}
}

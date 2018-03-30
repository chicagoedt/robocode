package org.chicagoedt.rosetteweb.editor.actionblocks

import org.w3c.dom.CanvasRenderingContext2D
import org.chicagoedt.rosette.actions.robotActions.*
import org.chicagoedt.rosette.robots.RobotRotation
import org.chicagoedt.rosetteweb.canvas.*
import org.chicagoedt.rosetteweb.editor.*
import org.chicagoedt.rosetteweb.*

/**
 * The instruction block that represents the Move instruction
 */
class TurnActionBlock(manager : InteractionManager,
					  context : CanvasRenderingContext2D,
					  dropzone : Dropzone) : ActionBlock<TurnAction>(manager, context, dropzone){
	override var action = TurnAction()
	override var color = COLOR_TURN
	override var text = "Turn"
	override val hasParameter = true

	init{
		menu.text = "Clockwise"
		menu.addItem("Clockwise", {action.parameter = RobotRotation.CLOCKWISE})
		menu.addItem("Counterclockwise", {action.parameter = RobotRotation.COUNTERCLOCKWISE})
	}
	
}
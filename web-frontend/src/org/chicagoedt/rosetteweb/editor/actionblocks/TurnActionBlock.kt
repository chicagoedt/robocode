package org.chicagoedt.rosetteweb.editor.actionblocks

import org.w3c.dom.CanvasRenderingContext2D
import org.chicagoedt.rosette.actions.robotActions.*
import org.chicagoedt.rosetteweb.canvas.*
import org.chicagoedt.rosetteweb.editor.*

/**
 * The instruction block that represents the Move instruction
 */
class TurnActionBlock(manager : InteractionManager,
					  context : CanvasRenderingContext2D,
					  dropzone : Dropzone) : ActionBlock<TurnAction>(manager, context, dropzone){
	override var height = 50.0
    override var width = 100.0
	override var action = TurnAction()
	override var color = "#ff6659"
	override var x = 0.0
	override var y = 0.0
	override var text = "Turn"
	
}
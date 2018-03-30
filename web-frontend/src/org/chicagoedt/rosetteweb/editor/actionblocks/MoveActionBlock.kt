package org.chicagoedt.rosetteweb.editor.actionblocks

import org.w3c.dom.CanvasRenderingContext2D
import org.chicagoedt.rosette.actions.robotActions.*
import org.chicagoedt.rosetteweb.canvas.*
import org.chicagoedt.rosetteweb.editor.*
import org.chicagoedt.rosetteweb.*

/**
 * The instruction block that represents the Move instruction
 */
class MoveActionBlock(manager : InteractionManager,
					  context : CanvasRenderingContext2D,
					  dropzone : Dropzone) : ActionBlock<MoveAction>(manager, context, dropzone){
	override var action = MoveAction()
	override var color = COLOR_MOVE
	override var text = "Move"
	
}
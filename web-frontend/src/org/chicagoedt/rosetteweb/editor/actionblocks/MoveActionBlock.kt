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
	override var height = 50.0
    override var width = 100.0
	override var action = MoveAction()
	override var color = colors.moveActionBlock
	override var x = 0.0
	override var y = 0.0
	override var text = "Move"
	
}
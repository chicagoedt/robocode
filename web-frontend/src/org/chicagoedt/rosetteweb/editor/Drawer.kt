package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.chicagoedt.rosetteweb.canvas.*
import org.chicagoedt.rosetteweb.editor.actionblocks.*
import org.chicagoedt.rosetteweb.*

/**
 * The class to represent the instruction drawers at the bottom of the screen
 */
class Drawer(manager : InteractionManager, context : CanvasRenderingContext2D) : Dropzone(manager, context){
	override var x = 0.0
	override var y = (context.canvas.height * (5.0/6.0))
	override var width = context.canvas.width.toDouble()
	override var height = (context.canvas.height * (1.0/6.0))
	override var color = colors.drawer

	val actions = arrayOf<ActionBlock<*>>(
		MoveActionBlock(manager, context, this),
		TurnActionBlock(manager, context, this)
		)

	override fun removeDraggable(draggable : Draggable){
		if (draggable is MoveActionBlock) 
			actions[actions.indexOf(draggable as ActionBlock<*>)] = MoveActionBlock(manager, context, this)
		else if (draggable is TurnActionBlock)
			actions[actions.indexOf(draggable as ActionBlock<*>)] = TurnActionBlock(manager, context, this)

		calculate(x, y, width, height, color)
	}

	override fun calculate(newX : Double, newY : Double, newWidth : Double, newHeight : Double, newColor : String){
		super.calculate(newX, newY, newWidth, newHeight, newColor)

		var blockY = y
		for (block in actions){
			block.setContainerInfo(context, x, blockY, width / 3.0, 30.0)
			blockY += block.height
		}
	}

	override fun draw(){
		super.draw()
		for (action in actions){
			action.draw()
		}
	}
}
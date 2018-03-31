package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.chicagoedt.rosetteweb.canvas.*
import org.chicagoedt.rosetteweb.editor.actionblocks.*
import org.chicagoedt.rosetteweb.*

/**
 * The class to represent the instruction drawers at the bottom of the screen
 * @param manager The interaction manager for the canvas
 * @param context The context in which the canvas is drawn
 * @property actions The list of actions stored in the drawer (i.e. not in a panel yet)
 */
class Drawer(manager : InteractionManager, context : CanvasRenderingContext2D) : Dropzone(manager, context){
	override var color = COLOR_DRAWER

	val actions = arrayOf<ActionBlock<*>>(
		MoveActionBlock(manager, context, this),
		TurnActionBlock(manager, context, this)
		)

	override fun removeDraggable(draggable : Draggable){
		if (draggable is MoveActionBlock) 
			actions[actions.indexOf(draggable as ActionBlock<*>)] = MoveActionBlock(manager, context, this)
		else if (draggable is TurnActionBlock)
			actions[actions.indexOf(draggable as ActionBlock<*>)] = TurnActionBlock(manager, context, this)

		recalculate()
	}

	override fun calculate(){
		var blockY = y
		for (block in actions){
			block.x = x
			block.y = blockY
			block.width = BLOCK_WIDTH
			block.height = BLOCK_HEIGHT
			block.recalculate()

			blockY += block.height
		}
	}

	override fun drop(draggable : Draggable){
		super.drop(draggable)
		manager.draggables.remove(draggable)
		if (draggable is ActionBlock<*>){
			manager.onClicks.remove(draggable.menu)
			for (button in draggable.menu.items){
				manager.onClicks.remove(button)
			}
		}
	}

	override fun afterDraw(){
		for (action in actions){
			action.draw()
		}
	}

	override fun onHover(mx : Double, my : Double, draggable : Draggable){
		
	}
}
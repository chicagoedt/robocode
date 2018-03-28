package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosetteweb.canvas.*
import org.chicagoedt.rosetteweb.editor.*

/**
 * The class to represent the instruction drawers at the bottom of the screen
 */
class Drawer(manager : InteractionManager, context : CanvasRenderingContext2D) : Dropzone(manager, context){
	override var x = 0.0
	override var y = (context.canvas.height * (5.0/6.0))
	override var width = context.canvas.width.toDouble()
	override var height = (context.canvas.height * (1.0/6.0))
	override var color = "black"

	val instructions = arrayListOf<InstructionBlock<*>>()

	init{
		addInstruction(MoveInstructionBlock(manager, context, this))
	}

	fun addInstruction(instruction : InstructionBlock<*>){
		instructions.add(instruction)
	}

	override fun removeDraggable(draggable : Draggable){
		instructions.remove(draggable as InstructionBlock<*>)

		if (draggable is MoveInstructionBlock) addInstruction(MoveInstructionBlock(manager, context, this))

		calculate(x, y, width, height, color)
	}

	override fun calculate(newX : Double, newY : Double, newWidth : Double, newHeight : Double, newColor : String){
		super.calculate(newX, newY, newWidth, newHeight, newColor)

		var blockY = y
		for (block in instructions){
			block.setContainerInfo(context, x, blockY, width / 3.0, 30.0)
			blockY += block.height
		}
	}

	override fun draw(){
		super.draw()
		for (instruction in instructions){
			instruction.draw()
		}
	}
}
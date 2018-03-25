package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.robots.*
import org.chicagoedt.rosetteweb.canvas.*
import org.chicagoedt.rosetteweb.editor.InstructionBlock

/**
 * A space to contain all of the instructions for each robot
 * @param context The context to draw this panel on
 * @param manager The interaction managert to handle this canvas
 * @param player The player that this panel will correspond to
 * @property height The height of this panel
 * @property width The width of this panel
 * @property y The X position of the top left corner of this panel
 * @property x The Y postiion of the top left corner of this panel
 * @property headerHeight The height of the header at the top of the panel
 * @property textHeaderHeightRatio The ratio of the header text height to the header height
 * @property textHeaderHeight The size of the text in the header
 * @property blockHeight The standard height of the blocks
 * @property instructions The instruction blocks contained in this panel
 */
class Panel(context: CanvasRenderingContext2D, 
			var player: RobotPlayer,
			manager : InteractionManager) : Dropzone(manager, context){
	override var height = 0.0
	override var width = 0.0
	override var x = 0.0
	override var y = 0.0
	override var color = "#9E9E9E"
	private var headerHeight = 50.0
	private val textHeaderHeightRatio = (2.0/3.0)
	private var textHeaderHeight = headerHeight * textHeaderHeightRatio
	private val blockHeight = 30.0
	private var instructions = arrayListOf<InstructionBlock<*>>()

	init{
		addInstruction(MoveInstructionBlock(manager, context))
		addInstruction(MoveInstructionBlock(manager, context))
	}

	/**
	 * Adds an instruction to this panel. The instruction is added to the robot player automatically
	 * @param block The instruction block to add
	 */
	fun addInstruction(block : InstructionBlock<*>){
		block.dropzone = this
		instructions.add(block)
		player.attachInstruction(block.action)
	}

	/**
	 * Draws the header on the panel
	 */
	fun drawHeader(){
		context.fillStyle = "#5C6BC0"
		context.fillRect(x, y, width, headerHeight);


		context.fillStyle = "black"
        context.font = (textHeaderHeight).toInt().toString() + "px Arial";
        context.fillText(player.name, x, y + textHeaderHeight);
	}

	override fun calculate(newX : Double, newY : Double, newWidth : Double, newHeight : Double, newColor : String){
		super.calculate(newX, newY, newWidth, newHeight, newColor)

		var blockY = y + headerHeight
		for (block in instructions){
			block.setPanelInfo(context, x, blockY, width, blockHeight)
			blockY += blockHeight
		}

		textHeaderHeight = headerHeight * textHeaderHeightRatio
	}

	override fun draw(){
		super.draw()

        drawHeader()

        for (instruction in instructions){
        	instruction.draw()
        }
	}

	override fun drop(draggable : Draggable){
		super.drop(draggable)
		val block = draggable as InstructionBlock<*>
		instructions.add(block)
		calculate(x, y, width, height, color)
	}

	override fun removeDraggable(draggable : Draggable){
		instructions.remove(draggable as InstructionBlock<*>)
		calculate(x, y, width, height, color)
	}
}
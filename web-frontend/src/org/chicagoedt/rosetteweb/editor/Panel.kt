package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Robots.*
import org.chicagoedt.rosetteweb.InteractionManager

/**
 * A space to contain all of the instructions for each robot
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
class Panel(val context: CanvasRenderingContext2D, 
			var player: RobotPlayer,
			val manager : InteractionManager){
	private var height = 0.0
	private var width = 0.0
	private var x = 0.0
	private var y = 0.0
	private var headerHeight = 50.0
	private val textHeaderHeightRatio = (2.0/3.0)
	private var textHeaderHeight = headerHeight * textHeaderHeightRatio
	private val blockHeight = 30.0
	private var instructions = arrayListOf<InstructionBlock<*>>()

	init{
		addInstruction(MoveInstructionBlock(manager))
		addInstruction(MoveInstructionBlock(manager))
	}

	/**
	 * Adds an instruction to this panel. The instruction is added to the robot player automatically
	 * @param block The instruction block to add
	 */
	fun addInstruction(block : InstructionBlock<*>){
		instructions.add(block)
		player.attachInstruction(block.instruction)
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

	/**
	 * Updates the position of the panel, along with everything contained in the panel (such as instructions)
	 * @param newX The new X value for the panel
	 * @param newY The new Y value for the panel
	 * @param newWidth The new width for the panel
	 * @param newHeight The new height of the panel
	 */
	fun updatePosition(newX : Double, newY : Double, newWidth : Double, newHeight : Double){
		x = newX
		y = newY
		width = newWidth
		height = newHeight

		var blockY = y + headerHeight
		for (block in instructions){
			block.setPanelInfo(context, x, blockY, width, blockHeight)
			blockY += blockHeight
		}

		textHeaderHeight = headerHeight * textHeaderHeightRatio
	}

	/**
	 * Draws the panel and all of its components on the screen
	 */
	fun draw(){
		context.fillStyle = "#E1BEE7" //purple
        context.fillRect(x, y, width, height)

        drawHeader()

        for (instruction in instructions){
        	instruction.draw()
        }
	}
}
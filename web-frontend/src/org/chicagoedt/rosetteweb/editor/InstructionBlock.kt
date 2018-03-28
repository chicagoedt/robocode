package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.actions.*
import org.chicagoedt.rosetteweb.canvas.*

/**
 * A block that represents (and contains) an instruction for the robot
 * @param manager The interaction manager for the canvas that this block is on
 * @property x The X position for this block
 * @property y The y position for this block
 * @property height The height of this block
 * @property width The width of this block
 * @property instruction The instruction object that this block represents
 * @property color The color of this block
 * @property context The context in which to draw this block
 * @property name The name of this block to display on the screen
 * @property textHeight The height of the text to display on this block
 */
abstract class InstructionBlock<T : Action<*>>(manager : InteractionManager, context : CanvasRenderingContext2D) : Draggable(manager, context) {
	override abstract var x : Double
	override abstract var y : Double
	override abstract var height : Double
	override abstract var width : Double
	override var radius = 10.0
	override var shadowBlur = 5.0
	override lateinit var dropzone : Dropzone
	override abstract var color : String
	override var textColor = "black"
	override var textSize = (height * (2.0/3.0)).toInt()
	override var textAlignmentHorizontal = TextAlignmentHorizontal.LEFT
	override var textMarginLeft = 2.0

	abstract var action : T

	override fun draw(){
		super.draw()
	}

	override fun onDragStart(){
		shadowBlur = 20.0
	}

	override fun onDragEnd(){
		shadowBlur = 5.0
	}

	/**
	 * Updates the info that should be determined by the panel it comes from
	 * @param blockContext The context to draw this block
	 * @param blockX The X value for this block
	 * @param blockY The Y value for this block
	 * @param blockWidth The width for this block
	 * @param blockHeight The height for this block
	 */
	fun setContainerInfo(blockContext: CanvasRenderingContext2D,
					 blockX : Double,
					 blockY : Double,
					 blockWidth : Double,
					 blockHeight : Double){
		x = blockX
		y = blockY
		context = blockContext
		width = blockWidth
		height = blockHeight
		textSize = (height * (2.0/3.0)).toInt()
	}
}
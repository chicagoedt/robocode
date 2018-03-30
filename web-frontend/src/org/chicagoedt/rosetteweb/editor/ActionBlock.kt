package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.actions.*
import org.chicagoedt.rosetteweb.canvas.*
import org.chicagoedt.rosetteweb.*

/**
 * A block that represents (and contains) an instruction for the robot
 * @param manager The interaction manager for the canvas that this block is on
 * @property action The action object that this block represents
 */
abstract class ActionBlock<T : Action<*>>(manager : InteractionManager,
										  context : CanvasRenderingContext2D,
										  dropzone : Dropzone) : Draggable(manager, context, dropzone) {
	override var textAlignmentHorizontal = TextAlignmentHorizontal.LEFT
	override var shadowBlur = BLOCK_DOWN_SHADOW
	override var radius = BLOCK_CORNER_RADIUS
	abstract var action : T

	override fun onDragStart(){
		shadowBlur = BLOCK_LIFT_SHADOW
	}

	override fun onDragEnd() {
		this.shadowBlur = BLOCK_DOWN_SHADOW
	}

	override fun recalculate(){
		textSize = (height * (2.0/3.0)).toInt()
		radius = BLOCK_CORNER_RADIUS
	}
}
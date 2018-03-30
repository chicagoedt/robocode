package org.chicagoedt.rosetteweb.canvas

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosetteweb.canvas.Drawable
import org.chicagoedt.rosetteweb.canvas.InteractionManager
import org.chicagoedt.rosetteweb.canvas.Draggable

/**
 * An object that you can drop a Draggable into
 * @param manager The interaction manager for the canvas
 * 
 */
abstract class Dropzone(val manager : InteractionManager, context : CanvasRenderingContext2D) : Drawable(context){

	init{
		manager.dropzones.add(this)
	}

	/**
	 * To be called when a draggable is dropped into this dropzone
	 * @param draggable The draggable being dropped into this dropzone
	 */
	open fun drop(draggable : Draggable){
		draggable.dropzone.removeDraggable(draggable)
		draggable.dropzone = this
		draggable.shouldDraw = true
	}

	abstract fun removeDraggable(draggable : Draggable)
}
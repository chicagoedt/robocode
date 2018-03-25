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
abstract class Dropzone(manager : InteractionManager, context : CanvasRenderingContext2D) : Drawable(context){

	init{
		manager.dropzones.add(this)
	}

	/**
	 * To be called when an object of type T is dropped into this dropzone
	 * @param draggable The object being dropped into this dropzone
	 */
	open fun drop(draggable : Draggable){
		draggable.dropzone.removeDraggable(draggable)
		draggable.dropzone = this
	}

	/**
	 * Determines if the mouse is within this dropzone
	 * @param mouseX The X value of the mouse
	 * @param mouseY The Y value of the mouse
	 * @return True if the mouse is in this dropzone, false otherwise
	 */
	fun mouseWithin(mouseX : Double, mouseY : Double) : Boolean{
		if (mouseX > x && mouseX < (x + width)){
			if (mouseY > y && mouseY < (y + height)){
				return true
			}
		}
		return false
	}

	abstract fun removeDraggable(draggable : Draggable)
}
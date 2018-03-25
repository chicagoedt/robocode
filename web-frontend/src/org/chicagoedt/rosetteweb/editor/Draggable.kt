package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.chicagoedt.rosetteweb.InteractionManager
import org.chicagoedt.rosetteweb.Drawable

/**
 * An object that can be dragged around with the mouse
 * @param manager The interaction manager to manage this draggable
 */
abstract class Draggable(manager : InteractionManager, context : CanvasRenderingContext2D) : Drawable(context){
	override abstract var x : Double
	override abstract var y : Double
	override abstract var height : Double
	override abstract var width : Double

	init{
		manager.draggables.add(this)
	}

	override abstract fun draw()

	/**
	 * Drag to this location
	 * @param mx The X value to drag to
	 * @param my The Y value to drag to
	 */
	fun drag(mx : Double, my : Double){
		x = mx
        y = my
        draw()
	}

	/**
	 * Determines if the mouse is within this block
	 * @param mouseX The X value of the mouse
	 * @param mouseY The Y value of the mouse
	 * @return True if the mouse is in this block, false otherwise
	 */
	fun mouseWithin(mouseX : Double, mouseY : Double) : Boolean{
		if (mouseX > x && mouseX < (x + width)){
			if (mouseY > y && mouseY < (y + height)){
				return true
			}
		}
		return false
	}
}
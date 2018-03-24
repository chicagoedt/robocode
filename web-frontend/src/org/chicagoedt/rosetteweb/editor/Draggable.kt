package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.chicagoedt.rosetteweb.InteractionManager

abstract class Draggable(manager : InteractionManager){
	protected abstract var x : Double
	protected abstract var y : Double
	protected abstract var height : Double
	protected abstract var width : Double

	init{
		manager.draggables.add(this)
	}

	abstract fun draw()

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
package org.chicagoedt.rosetteweb

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.chicagoedt.rosetteweb.editor.Draggable

/**
 * Handles all of the interactions on the canvas, such as clicking/dragging
 * @param context The context for the canvas being interacted upon
 * @param refresh The callback to refresh the canvas drawing when interacting
 * @param draggables All draggable objects to be handled by this manager
 * @param offsetX The X offset of the canvas relative to the browser window
 * @param offsetY The Y offset of the canvas relative to the brwoser window
 */
class InteractionManager(val context : CanvasRenderingContext2D, val refresh: () -> Unit){
	val draggables = arrayListOf<Draggable>()
	var offsetX = 0.0
	var offsetY = 0.0

	init{
		context.canvas.onmousedown = { e ->
			val mouseE = e as MouseEvent
			for(draggable in draggables){
				val mouseX = mouseE.clientX.toDouble() - offsetX
				val mouseY = mouseE.clientY.toDouble() - offsetY
       			if (draggable.mouseWithin(mouseX, mouseY)){
       				setDrag(draggable)
        			break
       			}
			}
    		true
		}

		context.canvas.onmouseup = {e : Event ->
			clearDrag()
			true
		}
	}

	/**
	 * Updates the offset values
	 * @param newX The new X offset relative to the browser window
	 * @param newY The new Y offset relative to the browser window
	 */
	fun updateOffset(newX : Double, newY : Double){
		offsetX = newX
		offsetY = newY
	}

	/**
	 * Sets a draggable to be dragged
	 * @param draggable The draggable to be dragged
	 */
	fun setDrag(draggable : Draggable){
		context.canvas.onmousemove = { e : Event ->
			val mouseE = e as MouseEvent
			val mouseX = mouseE.clientX.toDouble() - offsetX
			val mouseY = mouseE.clientY.toDouble() - offsetY
			refresh.invoke()
			draggable.drag(mouseX, mouseY)
			
       		true
		}
	}

	/**
	 * Clears the current draggable being dragged
	 */
	fun clearDrag(){
		context.canvas.onmousemove = {e : Event ->
			false
		}
	}
}
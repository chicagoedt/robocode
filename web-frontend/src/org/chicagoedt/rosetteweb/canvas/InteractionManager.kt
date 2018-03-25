package org.chicagoedt.rosetteweb.canvas

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.chicagoedt.rosetteweb.canvas.Draggable
import org.chicagoedt.rosetteweb.canvas.Dropzone

/**
 * Handles all of the interactions on the canvas, such as clicking/dragging
 * @param context The context for the canvas being interacted upon
 * @param refresh The callback to refresh the canvas drawing when interacting
 * @property draggables All draggable objects to be handled by this manager
 * @property currentDraggable The current item beign dragged
 * @property offsetX The X offset of the canvas relative to the browser window
 * @property offsetY The Y offset of the canvas relative to the brwoser window
 */
class InteractionManager(val context : CanvasRenderingContext2D, val refresh: () -> Unit){
	val draggables = arrayListOf<Draggable>()
	lateinit var currentDraggable : Draggable
	var originalX = 0.0
	var originalY = 0.0
	val dropzones = arrayListOf<Dropzone>()
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
			val mouseE = e as MouseEvent
			var found = false
			for (dropzone in dropzones){
				val mouseX = mouseE.clientX.toDouble() - offsetX
				val mouseY = mouseE.clientY.toDouble() - offsetY
				if (dropzone.mouseWithin(mouseX, mouseY)){
					dropzone.drop(currentDraggable)
					found = true
					break
				}
			}
			if (!found){
				currentDraggable.calculate(originalX, originalY, currentDraggable.width, currentDraggable.height, currentDraggable.color)
			}
			refresh.invoke()
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
		currentDraggable = draggable
		originalX = currentDraggable.x
		originalY = currentDraggable.y
		var prevX = -1.0
		var prevY = -1.0
		context.canvas.onmousemove = { e : Event ->
			val mouseE = e as MouseEvent
			val mouseX = mouseE.clientX.toDouble() - offsetX
			val mouseY = mouseE.clientY.toDouble() - offsetY
			if (prevX == -1.0) prevX = mouseX
			if (prevY == -1.0) prevY = mouseY

			refresh.invoke()
			draggable.drag(mouseX - prevX, mouseY - prevY)

			prevX = mouseX
			prevY = mouseY	

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
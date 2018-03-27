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
 * @property currentDraggableIndex The index of the current item in [draggables] being dragged
 * @property originalX The original X value of the object being dragged
 * @property originalY The original Y value of the object being dragged
 * @property offsetX The X offset of the canvas relative to the browser window
 * @property offsetY The Y offset of the canvas relative to the browser window
 */
class InteractionManager(val context : CanvasRenderingContext2D, val refresh: () -> Unit){
	val draggables = arrayListOf<Draggable>()
	var currentDraggableIndex = -1
	val onClicks = arrayListOf<Button>()
	var originalX = 0.0
	var originalY = 0.0
	val dropzones = arrayListOf<Dropzone>()
	var offsetX = 0.0
	var offsetY = 0.0

	init{
		context.canvas.onmousedown = { e ->
			val mouseE = e as MouseEvent
			var found = false
			val mouseX = mouseE.clientX.toDouble() - offsetX
			val mouseY = mouseE.clientY.toDouble() - offsetY

			for(draggable in draggables){
       			if (draggable.mouseWithin(mouseX, mouseY)){
       				setDrag(draggable)
       				found = true
        			break
       			}
			}

			if (!found){
				for (button in onClicks){
					if (button.mouseWithin(mouseX, mouseY)){
						button.onClick()
						found = true
						break
					}
				}
			}

    		true
		}

		context.canvas.onmouseup = {e : Event ->
			if (currentDraggableIndex != -1){
				draggables[currentDraggableIndex].beingDragged = false
				val mouseE = e as MouseEvent
				var found = false
				val mouseX = mouseE.clientX.toDouble() - offsetX
				val mouseY = mouseE.clientY.toDouble() - offsetY
				for (dropzone in dropzones){
					if (dropzone.mouseWithin(mouseX, mouseY)){
						dropzone.drop(draggables[currentDraggableIndex])
						found = true
						break
					}
				}
				if (!found){
					draggables[currentDraggableIndex].calculate(originalX, 
						originalY, 
						draggables[currentDraggableIndex].width, 
						draggables[currentDraggableIndex].height, 
						draggables[currentDraggableIndex].color)
				}
				refresh.invoke()
				clearDrag()
			}
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
		currentDraggableIndex = draggables.indexOf(draggable)
		originalX = draggables[currentDraggableIndex].x
		originalY = draggables[currentDraggableIndex].y
		draggables[currentDraggableIndex].beingDragged = true
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
		currentDraggableIndex = -1
		context.canvas.onmousemove = {e : Event ->
			false
		}
	}
}
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
 * @property dropdowns All the dropdown objects in the manager
 * @property buttons A list of Buttons to check on each click
 * @property currentDraggable The current item in [draggables] being dragged
 * @property currentDropdown The current item in [dropdowns] being displayed
 * @property originalX The original X value of the object being dragged
 * @property originalY The original Y value of the object being dragged
 * @property offsetX The X offset of the canvas relative to the browser window
 * @property offsetY The Y offset of the canvas relative to the browser window
 */
class InteractionManager(val context : CanvasRenderingContext2D, val refresh: () -> Unit){
	val draggables = arrayListOf<Draggable>()
	val buttons = arrayListOf<Button>()
	val dropdowns = arrayListOf<Dropdown>()
	val drawOnTop = arrayListOf<Drawable>()

	var currentDraggable : Draggable? = null
	var currentDropdown : Dropdown? = null
	private var originalX = 0.0
	private var originalY = 0.0
	val dropzones = arrayListOf<Dropzone>()
	var offsetX = 0.0
	var offsetY = 0.0

	init{
		context.canvas.onmousedown = ::mouseDown
		context.canvas.onmouseup = ::mouseUp
	}

	/**
	 * To be called when the mouse is lifted up
	 * @param e The event fromthe mouse being pressed
	 */
	private fun mouseUp(e : Event) : Boolean{
		if (currentDraggable != null){
			(currentDraggable as Draggable).beingDragged = false
			(currentDraggable as Draggable).onDragEnd()
			val mouseE = e as MouseEvent
			var found = false
			val mouseX = mouseE.clientX.toDouble() - offsetX
			val mouseY = mouseE.clientY.toDouble() - offsetY
			for (dropzone in dropzones){
				if (dropzone.mouseWithin(mouseX, mouseY)){
					dropzone.drop((currentDraggable as Draggable))
					found = true
					break
				}
			}
			if (!found){
				(currentDraggable as Draggable).x = originalX
				(currentDraggable as Draggable).y = originalY
				(currentDraggable as Draggable).recalculate()
			}
			refreshView()
			clearDrag()
		}
		return true
	}

	/**
	 * To be called when the mouse is pressed down
	 * @param e The event from the mouse being pressed
	 */
	private fun mouseDown(e : Event) : Boolean{ 
		val mouseE = e as MouseEvent
		var found = false
		val mouseX = mouseE.clientX.toDouble() - offsetX
		val mouseY = mouseE.clientY.toDouble() - offsetY

		for (button in buttons){
			if (button.mouseWithin(mouseX, mouseY) && button.shouldDraw){
				button.onClick()
				found = true
				break
			}
		}

		if (!found){
			for (dropdown in dropdowns){
				if (dropdown.mouseWithin(mouseX, mouseY) && dropdown.shouldDraw){
					if (dropdown.expanded) removeDropdownItems(dropdown)
					else addDropdownItems(dropdown)
   					found = true
      				break
   				}
			}
		}

		if (!found){
			for(draggable in draggables){
  				if (draggable.mouseWithin(mouseX, mouseY) && draggable.shouldDraw){
  					setDrag(draggable, mouseX, mouseY)
   					found = true
      				break
   				}
			}
		}
		refreshView()
    	return true
    }

    /**
     * Adds the items from a dropdown to the scene, and sets the dropdown as the current dropdown
     * @param dropdown The dropdown to display
     */
    fun addDropdownItems(dropdown : Dropdown){
    	if (currentDropdown != null) removeDropdownItems(currentDropdown as Dropdown)
    	currentDropdown = dropdown
    	dropdown.expanded = true
		for (button in dropdown.items){
			buttons.add(0, button)
			button.shouldDraw = true
		}
		drawOnTop.add(dropdown)
    }

    /**
     * Removes the current dropdown items from the scene, but keeps the dropdown
     * @param dropdown The dropdown whose items to remove
     */
    fun removeDropdownItems(dropdown : Dropdown){
    	currentDropdown = null
    	dropdown.expanded = false
		for (button in dropdown.items){
			buttons.remove(button)
			button.shouldDraw = false
		}						
		drawOnTop.remove(dropdown)
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
	fun setDrag(draggable : Draggable, mx : Double, my : Double){
		currentDraggable = draggable
		originalX = draggable.x
		originalY = draggable.y
		draggable.beingDragged = true
		draggable.onDragStart()
		var prevX = mx
		var prevY = my
		var mouseX = mx - offsetX
		var mouseY = my - offsetY
		context.canvas.onmousemove = { e : Event ->
			mouseX = (e as MouseEvent).clientX.toDouble() - offsetX
			mouseY = (e as MouseEvent).clientY.toDouble() - offsetY

			refreshView()

			for (dropzone in dropzones){
				if (dropzone.mouseWithin(mouseX, mouseY)){
					dropzone.onHover(mouseX, mouseY, draggable)
				}
			}

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
		currentDraggable = null
		context.canvas.onmousemove = {e : Event ->
			false
		}
	}

	/**
	 * Refreshes the view
	 */
	 fun refreshView(){
		refresh.invoke()
		for (drawable in drawOnTop){
			drawable.draw()
		}
	 }
}
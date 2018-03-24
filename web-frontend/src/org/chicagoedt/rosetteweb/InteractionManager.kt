package org.chicagoedt.rosetteweb

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.chicagoedt.rosetteweb.editor.Draggable

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

	fun updateOffset(newX : Double, newY : Double){
		offsetX = newX
		offsetY = newY
	}

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

	fun clearDrag(){
		context.canvas.onmousemove = {e : Event ->
			false
		}
	}
}
package org.chicagoedt.rosetteweb.canvas

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.chicagoedt.rosetteweb.canvas.InteractionManager
import org.chicagoedt.rosetteweb.canvas.Drawable
import org.chicagoedt.rosetteweb.canvas.Dropzone

/**
 * An object that can be dragged around with the mouse
 * @param manager The interaction manager to manage this draggable
 * @property dropzone The dropzone that contains this object
 * @property beingDragged To be set by the InteractionManager if the current draggable is being dragged
 */
abstract class Draggable(manager : InteractionManager, context : CanvasRenderingContext2D) : Drawable(context){
	override abstract var x : Double
	override abstract var y : Double
	override abstract var height : Double
	override abstract var width : Double
	abstract var dropzone : Dropzone
	var beingDragged = false

	init{
		manager.draggables.add(this)
	}

	/**
	 * Drag to this location
	 * @param mx The X value to drag to
	 * @param my The Y value to drag to
	 */
	fun drag(mx : Double, my : Double){
		x += mx
        y += my
        beingDragged = false
        draw()
        beingDragged = true
	}

	override fun draw(){
		if (!beingDragged) super.draw()
	}

	abstract fun onDragStart()

	abstract fun onDragEnd()
}
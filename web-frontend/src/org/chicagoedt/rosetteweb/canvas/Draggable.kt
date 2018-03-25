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
 */
abstract class Draggable(manager : InteractionManager, context : CanvasRenderingContext2D) : Drawable(context){
	override abstract var x : Double
	override abstract var y : Double
	override abstract var height : Double
	override abstract var width : Double
	abstract var dropzone : Dropzone

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
		x += mx
        y += my
        draw()
	}
}
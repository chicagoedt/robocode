package org.chicagoedt.rosetteweb.canvas

import org.chicagoedt.rosetteweb.COLOR_BUTTON_TEXT
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.chicagoedt.rosetteweb.canvas.InteractionManager

/**
 * An object to perform an action when clicked (i.e., a button)
 * @param function A lambda to be performed when this button is clocked
 * @param label The text to show on the button
 */
open class Button(context : CanvasRenderingContext2D, 
				val manager : InteractionManager, 
				open var function : () -> Unit) : Drawable(context){

	constructor(context : CanvasRenderingContext2D, 
				manager : InteractionManager) : this(context, manager, {})

	override var textColor = COLOR_BUTTON_TEXT

	init{
		manager.onClicks.add(this)
	}

	/**
	 * Performes the function passed to this button
	 */
	fun onClick(){
		function.invoke()
	}
}
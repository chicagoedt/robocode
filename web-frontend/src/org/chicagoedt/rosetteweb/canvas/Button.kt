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
class Button(context : CanvasRenderingContext2D, 
				manager : InteractionManager, 
				var function : () -> Unit,
				label : String) : Drawable(context){
	override var textColor = COLOR_BUTTON_TEXT
	override var text = label

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
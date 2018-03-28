package org.chicagoedt.rosetteweb.canvas

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.chicagoedt.rosetteweb.canvas.InteractionManager

/**
 * An object to perform an action when clicked (i.e., a button)
 * @param function A lambda to be performed when this button is clocked
 * @param label The text to display on this button
 * @property labelColor The color of the label text
 * @property textHeightRatio How big the text should be compared to the button
 * @property textHeight The height of the text
 */
class Button(context : CanvasRenderingContext2D, 
				manager : InteractionManager, 
				var function : () -> Unit,
				val label : String) : Drawable(context){
	override var x = 0.0
	override var y = 0.0
	override var height = 0.0
	override var width = 0.0
	override var color = "white"
	override var textColor = "green"
	override var text = label

	init{
		manager.onClicks.add(this)
	}

	/**
	 * Performes the function passed to this button
	 */
	open fun onClick(){
		function.invoke()
	}
}
package org.chicagoedt.rosetteweb.canvas

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.chicagoedt.rosetteweb.canvas.InteractionManager

class Button(context : CanvasRenderingContext2D, 
				manager : InteractionManager, 
				var function : () -> Unit,
				val label : String,
				val labelColor : String) : Drawable(context){
	override var x = 0.0
	override var y = 0.0
	override var height = 0.0
	override var width = 0.0
	override var color = "white"
	private val textHeightRatio = (1.0/3.0)
	private var textHeight = height * textHeightRatio

	init{
		manager.onClicks.add(this)
	}

	override fun draw(){
		super.draw()
		context.fillStyle = labelColor
		context.font = (textHeight).toInt().toString() + "px Arial";
        context.fillText(label, x, y + textHeight)
	}

	open fun onClick(){
		function.invoke()
	}

	override fun calculate(newX : Double, newY : Double, newWidth : Double, newHeight : Double, newColor : String){
		super.calculate(newX, newY, newWidth, newHeight, newColor)

		textHeight = height * textHeightRatio
	}
}
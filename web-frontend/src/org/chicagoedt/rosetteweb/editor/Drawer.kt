package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosetteweb.canvas.Drawable

/**
 * The class to represent the instruction drawers at the bottom of the screen
 */
class Drawer(context : CanvasRenderingContext2D) : Drawable(context){
	override var x = 0.0
	override var y = (context.canvas.height * (5.0/6.0))
	override var width = context.canvas.width.toDouble()
	override var height = (context.canvas.height * (1.0/6.0))
	override var color = "black"
}
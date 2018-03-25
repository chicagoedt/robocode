package org.chicagoedt.rosetteweb

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*

/**
 * An object to be drawn on the board
 * @param context The context in which to draw this object
 * @property x The X value of the object to draw
 * @property y The Y value of the object to draw
 * @property width The width of the object to draw
 * @property height The height of the object to draw
 * @property color The color of the object to draw
 */
abstract class Drawable(protected var context : CanvasRenderingContext2D){
	abstract var x : Double
	abstract var y : Double
	abstract var width : Double
	abstract var height : Double
	abstract var color : String

	/**
	 * Draws the object on the screen
	 */
	open fun draw(){
		context.fillStyle = color
        context.fillRect(x, y, width, height)
	}
}
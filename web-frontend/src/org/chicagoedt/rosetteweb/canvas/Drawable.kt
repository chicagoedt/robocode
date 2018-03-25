package org.chicagoedt.rosetteweb.canvas

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

	/**
	 * Updates the properties of the object
	 * @param newX The new X value for the object
	 * @param newY The new Y value for the object
	 * @param newWidth The new width for the object
	 * @param newHeight The new height of the object
	 * @param newColor The new color for the object
	 */
	open fun calculate(newX : Double,
						newY : Double,
						newWidth : Double,
						newHeight : Double,
						newColor : String){
		x = newX
		y = newY
		width = newWidth
		height = newHeight
		color = newColor
	}
}
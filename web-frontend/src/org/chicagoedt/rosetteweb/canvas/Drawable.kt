package org.chicagoedt.rosetteweb.canvas

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import kotlin.math.*

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
	open var radius = 0.0
	open var shadowBlur = 0.0

	/**
	 * Draws the object on the screen
	 */
	open fun draw(){
		context.shadowBlur = shadowBlur
		context.shadowColor="black";
		context.fillStyle = color
		if (radius == 0.0){
        	context.fillRect(x, y, width, height)
    	}
    	else{
    		var posX = x
    		var posY = y + radius
    		
    		context.beginPath()
    		context.moveTo(posX, posY)
    		posX = x + radius
    		posY = y
    		context.quadraticCurveTo(x, y, posX, posY)

    		posX = x + width - radius
    		context.lineTo(posX, posY)

    		posX = x + width
    		posY = y + radius
    		context.quadraticCurveTo(x + width, y, posX, posY)

    		posY = y + height - radius
    		context.lineTo(posX, posY)

    		posX = x + width - radius
    		posY = y + height
    		context.quadraticCurveTo(x + width, y + height, posX, posY)

    		posX = x + radius
    		context.lineTo(posX, posY)

    		posX = x
    		posY = y + height - radius
    		context.quadraticCurveTo(x, y + height, posX, posY)

    		posY = y + radius
    		context.lineTo(posX, posY)

    		context.closePath()
    		context.fill()
    	}
    	context.shadowBlur = 0.0
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

	/**
	 * Determines if the mouse is within this drawable
	 * @param mouseX The X value of the mouse
	 * @param mouseY The Y value of the mouse
	 * @return True if the mouse is in this drawable, false otherwise
	 */
	fun mouseWithin(mouseX : Double, mouseY : Double) : Boolean{
		if (mouseX > x && mouseX < (x + width)){
			if (mouseY > y && mouseY < (y + height)){
				return true
			}
		}
		return false
	}
}
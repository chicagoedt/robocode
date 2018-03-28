package org.chicagoedt.rosetteweb.canvas

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.CanvasTextAlign
import kotlin.browser.*
import kotlin.math.*

enum class TextAlignmentVertical{
	CENTER,
	TOP,
	BOTTOM
}

enum class TextAlignmentHorizontal{
	CENTER,
	RIGHT,
	LEFT
}

/**
 * An object to be drawn on the board
 * @param context The context in which to draw this object
 * @property x The X value of the object to draw
 * @property y The Y value of the object to draw
 * @property width The width of the object to draw
 * @property height The height of the object to draw
 * @property color The color of the object to draw
 * @property text The text to draw over this drawable
 * @property textVerticalAlignment The text alignment in the vertical space
 * @property textHorizontalAlignment The text alignment in the horizontal space
 * @property textColor The color of the text
 * @property textFont The font to display the text in
 * @property textSize The size to display the text in
 * @property textMarginTop The top margin for the text
 * @property textMarginLeft The left margin for the text
 * @property textMarginRight The right margin for the text
 * @property textMarginBottom The bottom margin for the text
 */
abstract class Drawable(protected var context : CanvasRenderingContext2D){
	abstract var x : Double
	abstract var y : Double
	abstract var width : Double
	abstract var height : Double
	abstract var color : String
	open var radius = 0.0
	open var shadowBlur = 0.0

	//I know there's quite a few properties for the text, but most use cases will probably just leave these as default
	open var text = ""
	open var textAlignmentVertical = TextAlignmentVertical.CENTER
	open var textAlignmentHorizontal = TextAlignmentHorizontal.CENTER
	open var textColor = "black"
	open var textFont = "Arial"
	open var textSize = 20
	open var textMarginTop = 0.0
	open var textMarginLeft = 0.0
	open var textMarginRight = 0.0
	open var textMarginBottom = 0.0

	/**
	 * Draws the text on screen according to the class parameters
	 */
	fun drawText(){
		if (text != ""){
			var textY = y + textMarginTop
			var textX = x + textMarginLeft

			if (textAlignmentVertical == TextAlignmentVertical.CENTER){
				textY += (height / 2.4) + (textSize / 2.0)
			}
			else if (textAlignmentVertical == TextAlignmentVertical.TOP){
				textY += textSize.toDouble()
			}
			else if (textAlignmentVertical == TextAlignmentVertical.BOTTOM){
				textY += height
			}

			if (textAlignmentHorizontal == TextAlignmentHorizontal.CENTER){
				context.textAlign =  "center".asDynamic().unsafeCast<CanvasTextAlign>()
				textX += (width / 2.0)
			}
			else if (textAlignmentHorizontal == TextAlignmentHorizontal.LEFT){
				context.textAlign = "left".asDynamic().unsafeCast<CanvasTextAlign>()
			}
			else if (textAlignmentHorizontal == TextAlignmentHorizontal.RIGHT){
				context.textAlign =  "right".asDynamic().unsafeCast<CanvasTextAlign>()
				textX += width
			}

			textY -= textMarginBottom
			textX -= textMarginRight

			context.fillStyle = textColor
			context.font = textSize.toString() + "px " + textFont;
        	context.fillText(text, textX, textY, width);

        	context.textAlign =  "start".asDynamic().unsafeCast<CanvasTextAlign>()

		}
	}

	/**
	 * Draws the background of the object on the screen
	 */
	fun drawBackground(){
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
	 * Draws the object on the screen
	 */
	open fun draw(){
		drawBackground()
    	drawText()
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
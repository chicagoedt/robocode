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
 * @property drawWidth The width adjusted for DPI scaling
 * @property drawHeight The height adjusted for DPI scaling
 * @propoerty drawX The X value adjusted for DPI scaling
 * @property drawY The Y value adjusted for DPI scaling
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
open class Drawable(protected var context : CanvasRenderingContext2D){
	open var x = 0.0
	open var y = 0.0
	open var width = 0.0
	open var height = 0.0
	open var color = "white"
	internal var drawWidth = width / pixelRatio(context)
	internal var drawHeight = height / pixelRatio(context)
	internal var drawX = x / pixelRatio(context)
	internal var drawY = y / pixelRatio(context)

	open var radius = 0.0
	open var shadowBlur = 0.0

	//I know there's quite a few properties for the text, but most use cases will probably just leave these as default
	open var text = ""
	open var textAlignmentVertical = TextAlignmentVertical.CENTER
	open var textAlignmentHorizontal = TextAlignmentHorizontal.CENTER
	open var textColor = "black"
	open var textFont = "Arial"
	open var textSize = 40
	open var textMarginTop = 0.0
	open var textMarginLeft = 0.0
	open var textMarginRight = 0.0
	open var textMarginBottom = 0.0

	/**
	 * Draws the text on screen according to the class parameters
	 */
	fun drawText(){
		if (text != ""){
			calculateDPI()
			val drawTextSize = textSize / pixelRatio(context)

			var textY = drawY + textMarginTop
			var textX = drawX + textMarginLeft

			if (textAlignmentVertical == TextAlignmentVertical.CENTER){
				textY += (drawHeight / 2.4) + (drawTextSize / 2.0)
			}
			else if (textAlignmentVertical == TextAlignmentVertical.TOP){
				textY += drawTextSize.toDouble()
			}
			else if (textAlignmentVertical == TextAlignmentVertical.BOTTOM){
				textY += drawHeight
			}

			if (textAlignmentHorizontal == TextAlignmentHorizontal.CENTER){
				context.textAlign =  "center".asDynamic().unsafeCast<CanvasTextAlign>()
				textX += (drawWidth / 2.0)
			}
			else if (textAlignmentHorizontal == TextAlignmentHorizontal.LEFT){
				context.textAlign = "left".asDynamic().unsafeCast<CanvasTextAlign>()
			}
			else if (textAlignmentHorizontal == TextAlignmentHorizontal.RIGHT){
				context.textAlign =  "right".asDynamic().unsafeCast<CanvasTextAlign>()
				textX += drawWidth
			}

			textY -= textMarginBottom
			textX -= textMarginRight

			context.fillStyle = textColor
			context.font = drawTextSize.toString() + "px " + textFont;
        	context.fillText(text, textX, textY, drawWidth);

        	context.textAlign =  "start".asDynamic().unsafeCast<CanvasTextAlign>()

		}
	}

	/**
	 * Draws the background of the object on the screen
	 */
	fun drawBackground(){
		calculateDPI()
		context.shadowBlur = shadowBlur
		context.shadowColor="black";
		context.fillStyle = color
		if (radius == 0.0){
        	context.fillRect(drawX, drawY, drawWidth, drawHeight)
    	}
    	else{
    		val drawRadius = radius / pixelRatio(context)
    		var posX = drawX
    		var posY = drawY + drawRadius
    		
    		context.beginPath()
    		context.moveTo(posX, posY)
    		posX = drawX + drawRadius
    		posY = drawY
    		context.quadraticCurveTo(drawX, drawY, posX, posY)

    		posX = drawX + drawWidth - drawRadius
    		context.lineTo(posX, posY)

    		posX = drawX + drawWidth
    		posY = drawY + drawRadius
    		context.quadraticCurveTo(drawX + drawWidth, drawY, posX, posY)

    		posY = drawY + drawHeight - drawRadius
    		context.lineTo(posX, posY)

    		posX = drawX + drawWidth - drawRadius
    		posY = drawY + drawHeight
    		context.quadraticCurveTo(drawX + drawWidth, drawY + drawHeight, posX, posY)

    		posX = drawX + drawRadius
    		context.lineTo(posX, posY)

    		posX = drawX
    		posY = drawY + drawHeight - drawRadius
    		context.quadraticCurveTo(drawX, drawY + drawHeight, posX, posY)

    		posY = drawY + drawRadius
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
		calculateDPI()
	}

	private fun calculateDPI(){
		drawWidth = width / pixelRatio(context)
		drawHeight = height / pixelRatio(context)
		drawX = x / pixelRatio(context)
		drawY = y / pixelRatio(context)
	}

	/**
	 * Determines if the mouse is within this drawable
	 * @param mouseX The X value of the mouse
	 * @param mouseY The Y value of the mouse
	 * @return True if the mouse is in this drawable, false otherwise
	 */
	fun mouseWithin(mouseX : Double, mouseY : Double) : Boolean{
		if (mouseX > drawX && mouseX < (drawX + drawWidth)){
			if (mouseY > drawY && mouseY < (drawY + drawHeight)){
				return true
			}
		}
		return false
	}
}

fun pixelRatio(context : CanvasRenderingContext2D) : Double{
   	var ratio = 1.0
   	js("var dpr = window.devicePixelRatio || 1," + 
   	    "bsr = context.webkitBackingStorePixelRatio ||" + 
   	            "context.mozBackingStorePixelRatio ||" + 
   	            "context.msBackingStorePixelRatio ||" + 
   	            "context.oBackingStorePixelRatio ||" + 
   	            "context.backingStorePixelRatio || 1;" +
   	    "ratio = dpr / bsr")
   	return ratio
}
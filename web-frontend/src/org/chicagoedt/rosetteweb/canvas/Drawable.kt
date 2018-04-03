package org.chicagoedt.rosetteweb.canvas

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextAlign

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
 * @property radius The radius of the rounded corners
 * @property shadowBlur The amount of blur on the shadow underneath this object
 * @property borderColor The color for the border on this object
 * @property borderWidth The width for the border on this object
 * @property hasBorder Select this if the object should be drawn with a border
 * @property text The text to draw over this drawable
 * @property textAlignmentVertical The text alignment in the vertical space
 * @property textAlignmentHorizontal The text alignment in the horizontal space
 * @property textColor The color of the text
 * @property textFont The font to display the text in
 * @property textSize The size to display the text in
 * @property shouldDraw Determines if drawing should happen when draw() is called
 */
open class Drawable(protected var context : CanvasRenderingContext2D) {
	open var x = 0.0
	open var y = 0.0
	open var width = 0.0
	open var height = 0.0
	open var color = "white"
	open var radius = 0.0
	open var shadowBlur = 0.0
	open var borderColor = "black"
	open var borderWidth = 0.0
	open var hasBorder = false

	//I know there's quite a few properties for the text, but most use cases will probably just leave these as default
	open var text = ""
	open var textAlignmentVertical = TextAlignmentVertical.CENTER
	open var textAlignmentHorizontal = TextAlignmentHorizontal.CENTER
	open var textColor = "black"
	open var textFont = "Arial"
	open var textSize = 40
	private var drawingTextWidth = 0.0
	private var drawingTextSize = 0
	private var textX = 0.0
	private var textY = 0.0
	private var textAlign = "start".asDynamic().unsafeCast<CanvasTextAlign>()

	var shouldDraw = true

	/**
	 * Draws the border for this object
	 */
	fun drawBorder(){
		traceOutline()
		context.strokeStyle = borderColor
		context.lineWidth = borderWidth
		context.stroke()
	}

	/**
	 * Traces the outline of this object, to be used with fill(), stroke(), etc
	 */
	private fun traceOutline(){
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
	}

	fun calculateText(){
		if (text != ""){
			textY = y
			textX = x

			if (textAlignmentVertical == TextAlignmentVertical.CENTER) {
				textY += (height / 2.0) + (textSize / 2.5)
			} else if (textAlignmentVertical == TextAlignmentVertical.TOP) {
				textY += textSize.toDouble() + radius

			} else if (textAlignmentVertical == TextAlignmentVertical.BOTTOM) {
				textY += height - radius
			}

			if (textAlignmentHorizontal == TextAlignmentHorizontal.CENTER) {
				textAlign = "center".asDynamic().unsafeCast<CanvasTextAlign>()
				textX += (width / 2.0)
			} else if (textAlignmentHorizontal == TextAlignmentHorizontal.LEFT) {
				textAlign = "left".asDynamic().unsafeCast<CanvasTextAlign>()
				textX += radius
			} else if (textAlignmentHorizontal == TextAlignmentHorizontal.RIGHT) {
				textAlign = "right".asDynamic().unsafeCast<CanvasTextAlign>()
				textX += width - radius
			}

			context.fillStyle = textColor
			context.font = textSize.toString() + "px " + textFont;

			drawingTextWidth = width - (radius * 2.0)

			val textWidth = context.measureText(text).width
			if (textWidth > drawingTextWidth){
				val textRatio = drawingTextWidth / textWidth.toDouble()
				drawingTextSize = (textSize.toDouble() * textRatio).toInt()
			}
			else drawingTextSize = textSize
		}
	}

	/**
	 * Draws the text on screen according to the class parameters
	 */
	fun drawText() {
		if (text != "") {
			context.fillStyle = textColor
			context.font = drawingTextSize.toString() + "px " + textFont;
			context.textAlign = textAlign

			context.fillText(text, textX, textY, drawingTextWidth);

			context.textAlign = "start".asDynamic().unsafeCast<CanvasTextAlign>()
		}
	}

	/**
	 * Draws the background of the object on the screen
	 */
	fun drawBackground() {
		context.shadowBlur = shadowBlur
		context.shadowColor = "black";
		context.fillStyle = color
		if (radius == 0.0) {
			context.fillRect(x, y, width, height)
		} else {
			traceOutline()
			context.closePath()
			context.fill()
		}
		context.shadowBlur = 0.0
	}

	/**
	 * Draws the object on the screen
	 */
	fun draw() {
		if (shouldDraw) {
			beforeDraw()
			drawBackground()
			drawText()
			if (hasBorder) drawBorder()
			afterDraw()
		}
	}

	/**
	 * Called before drawing the object
	 */
	protected open fun afterDraw() {

	}

	/**
	 * Called after drawing the objects
	 */
	protected open fun beforeDraw() {

	}

	fun recalculate(){
		calculate()
		calculateText()
	}

	/**
	 * Called to calculate the parameters of the object
	 * This is meant to be implemented in a subclass
	 */
	protected open fun calculate() {

	}

	/**
	 * Determines if the mouse is within this drawable
	 * @param mouseX The X value of the mouse
	 * @param mouseY The Y value of the mouse
	 * @return True if the mouse is in this drawable, false otherwise
	 */
	fun mouseWithin(mouseX: Double, mouseY: Double): Boolean {
		if (mouseX > x && mouseX < (x + width)) {
			if (mouseY > y && mouseY < (y + height)) {
				return true
			}
		}
		return false
	}
}
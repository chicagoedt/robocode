package org.chicagoedt.rosetteweb.canvas

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextAlign

/**
 * A list of clickable dropdown buttons
 * @param manager The interaction manager to handle this dropdown
 * @property items The buttons in this dropdown list
 * @property expanded True if the dropdown is currently displaying all items, false otherwise
 */
class Dropdown(context : CanvasRenderingContext2D, 
				val manager : InteractionManager) : Drawable(context){
	val items = arrayListOf<Button>()
	internal var expanded = false
	override var text = "Select..."
	override var textAlignmentHorizontal = TextAlignmentHorizontal.LEFT
	override var textColor = "black"

	init{
		manager.dropdowns.add(this)
	}

	/**
	 * Adds an item to this dropdown list
	 * @param itemText The label for this item
	 * @param function The function to call when this item is selected
	 */
	fun addItem(itemText : String, function : () -> Unit){
		val button = Button(context, manager, {
			text = itemText
			manager.refresh.invoke()
			function.invoke()
			manager.removeDropdownItems(this)
		})
		manager.buttons.remove(button)
		button.shouldDraw = false
		button.text = itemText
		button.textColor = "gray"
		items.add(button)
	}

	override fun calculate(){
		var buttonY = y + height
		for (button in items){
			button.textAlignmentHorizontal = TextAlignmentHorizontal.LEFT
			button.width = width
			button.height = height
			button.x = x
			button.y = buttonY
			button.recalculate()

			buttonY += button.height
		}
	}

	override fun afterDraw(){
		for (button in items){
			button.draw()
		}
	}

}
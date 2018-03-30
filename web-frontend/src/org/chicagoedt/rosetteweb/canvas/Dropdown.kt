package org.chicagoedt.rosetteweb.canvas

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextAlign

class Dropdown(context : CanvasRenderingContext2D, 
				manager : InteractionManager) : Button(context, manager){
	val items = arrayListOf<Button>()
	internal var expanded = false
	override var text = "Select..."
	override var textAlignmentHorizontal = TextAlignmentHorizontal.LEFT
	override var textColor = "black"
	override var function = {switchView()}

	fun addItem(itemText : String, function : () -> Unit){
		val button = Button(context, manager, {
			text = itemText
			switchView()
			function.invoke()
		})
		button.shouldDraw = false
		button.text = itemText
		button.textColor = "gray"
		items.add(button)
	}

	fun switchView(){
		if (expanded){
			expanded = false
			for (button in items){
				button.shouldDraw = false
			}
		}
		else if (!expanded){
			expanded = true
			for (button in items){
				button.shouldDraw = true
			}
		}
		manager.refresh.invoke()
		draw()
	}

	override fun recalculate(){
		var buttonY = y + height
		for (button in items){
			button.textAlignmentHorizontal = TextAlignmentHorizontal.LEFT
			button.width = width
			button.height = height
			button.x = x
			button.y = buttonY

			buttonY += button.height
		}
	}

	override fun afterDraw(){
		for (button in items){
			button.draw()
		}
	}

}
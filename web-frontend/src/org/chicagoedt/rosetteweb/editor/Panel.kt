package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.chicagoedt.rosette.robots.*
import org.chicagoedt.rosetteweb.canvas.*
import org.chicagoedt.rosetteweb.*

/**
 * A space to contain all of the actions for each robot
 * @param player The player that this panel will correspond to
 * @property actions The action blocks contained in this panel
 * @property runButton The button to run the procedure
 */
class Panel(context: CanvasRenderingContext2D, 
			var player: RobotPlayer,
			manager : InteractionManager) : Dropzone(manager, context){
	override var color = COLOR_PANEL_BACKGROUND
	override var borderWidth = PANEL_HOVER_BORDER_WIDTH
	override var borderColor = COLOR_PANEL_HOVER_BORDER
	private var header = Drawable(context)
	private var actions = arrayListOf<ActionBlock<*>>()
	private val runButton = Button(context, manager, ::runActions)

	init{
		runButton.text = "Run"
		header.textSize = (PANEL_HEADER_HEIGHT * (2.0/3.0)).toInt()
		header.color = COLOR_PANEL_HEADER
		header.text = player.name
		header.textAlignmentHorizontal = TextAlignmentHorizontal.LEFT
	}

	/**
	 * Runs the procedure assigned to the player
	 */
	fun runActions(){
		player.runInstructions()
		fullRefresh()
	}

	/**
	 * Adds an action to this panel. The action is added to the robot player automatically
	 * @param block The action block to add
	 */
	fun addAction(block : ActionBlock<*>){
		actions.add(block)
		player.appendAction(block.action)
	}


	override fun calculate(){
		var blockY = y + PANEL_HEADER_HEIGHT
		for (block in actions){
			block.x = x
			block.y = blockY
			block.width = BLOCK_WIDTH
			block.height = BLOCK_HEIGHT
			block.recalculate()

			blockY += block.height
		}

		runButton.x = x + PANEL_HEADER_WIDTH - PANEL_RUN_BUTTON_WIDTH
		runButton.y = y
		runButton.width = PANEL_RUN_BUTTON_WIDTH
		runButton.height = PANEL_HEADER_HEIGHT
		runButton.recalculate()

		header.x = x
		header.y = y
		header.width = PANEL_HEADER_WIDTH
		header.height = PANEL_HEADER_HEIGHT
		header.recalculate()

		borderWidth = PANEL_HOVER_BORDER_WIDTH
	}

	override fun afterDraw(){
		header.draw()

        runButton.draw()

        for (action in actions){
        	action.draw()
        }
	}

	override fun drop(draggable : Draggable){
		super.drop(draggable)
		val block = draggable as ActionBlock<*>
		block.menu.shouldDraw = true
		addAction(block)
		recalculate()
	}

	override fun removeDraggable(draggable : Draggable){
		actions.remove(draggable as ActionBlock<*>)
		player.removeAction((draggable as ActionBlock<*>).action)
		recalculate()
	}

	override fun onHover(mx : Double, my : Double, draggable : Draggable){
		drawBorder()
	}
}
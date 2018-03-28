package org.chicagoedt.rosetteweb.editor

import org.w3c.dom.CanvasRenderingContext2D
import org.chicagoedt.rosette.robots.*
import org.chicagoedt.rosetteweb.canvas.*
import org.chicagoedt.rosetteweb.*

/**
 * A space to contain all of the actions for each robot
 * @param player The player that this panel will correspond to
 * @property headerHeight The height of the header at the top of the panel
 * @property blockHeight The standard height of the blocks
 * @property actions The action blocks contained in this panel
 * @property runButton The button to run the procedure
 * @property actionPaddingVertical The spacing between the actions
 * @property actionPaddingHorizontal The spacing between the sides of the panel and the actions
 */
class Panel(context: CanvasRenderingContext2D, 
			var player: RobotPlayer,
			manager : InteractionManager) : Dropzone(manager, context){
	override var height = 0.0
	override var width = 0.0
	override var x = 0.0
	override var y = 0.0
	override var color = "#E0E0E0"
	override var radius = 1.0
	override var shadowBlur = 5.0

	private var headerHeight = 50.0

	override var textSize = (headerHeight * (2.0/3.0)).toInt()
	override var text = player.name
	override var textAlignmentVertical = TextAlignmentVertical.TOP
	override var textAlignmentHorizontal = TextAlignmentHorizontal.LEFT
	override var textMarginTop = 5.0

	private val blockHeight = 30.0
	private var actions = arrayListOf<ActionBlock<*>>()
	private val runButton = Button(context, manager, ::runActions, "Run")
	private val actionPaddingVertical = 5.0
	private val actionPaddingHorizontal = 20.0

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

	/**
	 * Draws the header on the panel
	 */
	fun drawHeader(){
		context.fillStyle = "#64B5F6"
		context.fillRect(x, y, width, headerHeight);
	}

	override fun calculate(newX : Double, newY : Double, newWidth : Double, newHeight : Double, newColor : String){
		super.calculate(newX, newY, newWidth, newHeight, newColor)

		var blockY = y + headerHeight + actionPaddingVertical
		for (block in actions){
			block.setContainerInfo(context, x + actionPaddingHorizontal, blockY, width - (2.0 * actionPaddingHorizontal), blockHeight)
			blockY += blockHeight + actionPaddingVertical
		}

		runButton.calculate(x + width * (5.0/6.0), y, width * (1.0/6.0), headerHeight, runButton.color)

		textSize = (headerHeight * (2.0/3.0)).toInt()
	}

	override fun draw(){
		super.drawBackground()

        drawHeader()

        super.drawText()

        runButton.draw()

        for (action in actions){
        	action.draw()
        }
	}

	override fun drop(draggable : Draggable){
		super.drop(draggable)
		val block = draggable as ActionBlock<*>
		addAction(block)
		calculate(x, y, width, height, color)
	}

	override fun removeDraggable(draggable : Draggable){
		actions.remove(draggable as ActionBlock<*>)
		player.removeAction((draggable as ActionBlock<*>).action)
		calculate(x, y, width, height, color)
	}
}
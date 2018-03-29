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
	override var color = colors.panelBackground
	override var radius = 1.0
	override var shadowBlur = 5.0

	private var headerHeight = 50.0
	private var header = Drawable(context)
	init{
		header.textSize = (headerHeight * (2.0/3.0)).toInt()
		header.color = colors.panelHeader
		header.text = player.name
		header.textAlignmentHorizontal = TextAlignmentHorizontal.LEFT
	}

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


	override fun calculate(newX : Double, newY : Double, newWidth : Double, newHeight : Double, newColor : String){
		super.calculate(newX, newY, newWidth, newHeight, newColor)

		var blockY = y + headerHeight + actionPaddingVertical
		for (block in actions){
			block.setContainerInfo(context, x + actionPaddingHorizontal, blockY, width - (2.0 * actionPaddingHorizontal), blockHeight)
			blockY += blockHeight + actionPaddingVertical
		}

		runButton.calculate(x + width * (5.0/6.0), y, width * (1.0/6.0), headerHeight, runButton.color)

		textSize = (headerHeight * (2.0/3.0)).toInt()
		header.calculate(x, y, width, headerHeight, header.color)
	}

	override fun draw(){
		super.drawBackground()

		header.draw()

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
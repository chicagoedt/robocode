package org.chicagoedt.rosetteweb

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Robots.*
import org.chicagoedt.rosetteweb.editor.*
/**
 * The driver to run an Editor canvas for the current game
 * @param game The game that the program is running
 * @param context The context for the canvas to run the editor on
 * @property globalPanelWidth The width of each panel in the editor. This varies as window size varies
 * @property globalPanelHeightRatio The height:width aspect ratio for the panels
 * @property globalPanelMarginPercent The percentage of screen that each margin should take up (e.g. set this to 5% and each margin width will be 5% of the canvas width)
 * @property globalMaxHeight The maximum height of a panel. This value will override the globalPanelHeightRatio
 * @property panels All of the panels in the level
 */

class EditorDriver(val game: Game, val context: CanvasRenderingContext2D){
	private var globalPanelWidth = 0.0
	private var globalPanelHeightRatio = 3
	private var globalPanelMarginPercent = 5.0
	private var globalMaxHeight = 600.0
	private val panels = ArrayList<Panel>()
	private var interactionManager  = InteractionManager(context, {draw()})

	init{
		calculateNewLevel()
	}

	fun setOffset(offsetX : Double, offsetY : Double){
		interactionManager.updateOffset(offsetX, offsetY)
	}

	/**
	 * Calculates all of the information necessary after a level change
	 */
	fun calculateNewLevel(){
		panels.clear()
		for ((name, player) in game.currentLevel.players){
			val panel = Panel(context, player, interactionManager)
			panels.add(panel)
        }
	}

	/**
	 * Calculates the positions of the panels in the canvas. This should be called when the screen size changes
	 */
	fun calculatePanels(){
		var panelX = 0.0
		var panelY = 0.0

		val panelMargin = context.canvas.width.toDouble() * (globalPanelMarginPercent / 100.0)
		globalPanelWidth = context.canvas.width.toDouble() - (game.currentLevel.players.size * panelMargin).toDouble()
		globalPanelWidth /= game.currentLevel.players.size
		for (panel in panels){
			var screenHeight = globalPanelHeightRatio * globalPanelWidth
			if (screenHeight > globalMaxHeight) screenHeight = globalMaxHeight

			panel.updatePosition(panelX, panelY, globalPanelWidth, screenHeight)

			panelX += globalPanelWidth + panelMargin

        }
	}

	/**
	 * Draws all of the elements in this editor
	 */
	fun drawEditor(){
		//draw background white
		context.fillStyle = "white"
        context.fillRect(0.0, 0.0, context.canvas.width.toDouble(), context.canvas.height.toDouble())

		for (panel in panels){
			panel.draw()
		}
	}
}
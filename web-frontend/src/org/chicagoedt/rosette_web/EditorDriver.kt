package org.chicagoedt.rosette_web

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Robots.*
import org.chicagoedt.rosette_web.Editor.*
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

	init{
		calculateNewLevel()
	}

	/**
	 * Calculates all of the information necessary after a level change
	 */
	fun calculateNewLevel(){
		panels.clear()
		for (player in game.currentLevel.playersList){
			val panel = Panel(context, player)
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
		globalPanelWidth = context.canvas.width.toDouble() - (game.currentLevel.playersList.size * panelMargin).toDouble()
		globalPanelWidth /= game.currentLevel.playersList.size
		for (panel in panels){
			panel.screenWidth = globalPanelWidth
			panel.screenHeight = globalPanelHeightRatio * globalPanelWidth
			if (panel.screenHeight > globalMaxHeight) panel.screenHeight = globalMaxHeight
			panel.screenX = panelX
			panel.screenY = panelY
			panelX += panel.screenWidth + panelMargin
        }
	}

	/**
	 * Draws all of the elements in this editor
	 */
	fun drawEditor(){
		for (panel in panels){
			panel.draw()
		}
	}
}
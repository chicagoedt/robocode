package org.chicagoedt.rosetteweb

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.robots.*
import org.chicagoedt.rosetteweb.editor.*
import org.chicagoedt.rosetteweb.canvas.*
/**
 * The driver to run an Editor canvas for the current game
 * @param game The game that the program is running
 * @param context The context for the canvas to run the editor on
 * @property globalPanelWidth The width of each panel in the editor. This varies as window size varies
 * @property globalPanelHeightRatio The height:width aspect ratio for the panels
 * @property globalPanelMarginPercent The percentage of screen that each margin should take up (e.g. set this to 5% and each margin width will be 5% of the canvas width)
 * @property globalMaxHeight The maximum height of a panel. This value will override the globalPanelHeightRatio
 * @property panels All of the panels in the level
 * @property interactionManager The manager for all interactions, such as clicking and dragging and dropping
 * @property drawer The drawer to house the actions to choose
 * @property panelPaddingVertical The vertical padding for each panel
 * @property panelPaddingHorizontal The horizontal padding for each panel. Not to be confused with the [globalPanelMarginPercent]
 */

class EditorDriver(val game: Game, val context: CanvasRenderingContext2D){
	private var globalPanelWidth = 0.0
	private var globalPanelHeightRatio = 3
	private var globalPanelMarginPercent = 5.0
	private var globalMaxHeight = 600.0
	private val panels = ArrayList<Panel>()
	private var interactionManager  = InteractionManager(context, {drawEditor()})
	private var drawer = Drawer(interactionManager, context)
	private var panelPaddingVertical = 10.0
	private var panelPaddingHorizontal = 10.0

	/**
	 * Set the offset of this canvas relative to the browser window
	 * @param offsetX The X offset of the canvas relative to the browser window
	 * @param offsetY The Y offset of the canvas relative to the browser window
	 */
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
		var panelX = panelPaddingHorizontal
		var panelY = panelPaddingVertical

		var panelMargin = context.canvas.width.toDouble() * (globalPanelMarginPercent / 100.0)
		if (panelMargin - panelX >= 0) panelMargin = panelMargin - panelX
		globalPanelWidth = context.canvas.width.toDouble() - (game.currentLevel.players.size * panelMargin).toDouble()
		globalPanelWidth /= game.currentLevel.players.size
		for (panel in panels){
			var screenHeight = globalPanelHeightRatio * globalPanelWidth
			if (screenHeight > globalMaxHeight) screenHeight = globalMaxHeight

			panel.calculate(panelX, panelY, globalPanelWidth, screenHeight, panel.color)

			panelX += globalPanelWidth + panelMargin
        }

        drawer.calculate(0.0, 
        		context.canvas.height.toDouble() * (5.0/6.0),
        		context.canvas.width.toDouble(),
        		context.canvas.height.toDouble() * (1.0/6.0),
        		drawer.color)
	}

	/**
	 * Draws all of the elements in this editor
	 */
	fun drawEditor(){
		context.fillStyle = "#424242"
        context.fillRect(0.0, 0.0, context.canvas.width.toDouble(), context.canvas.height.toDouble())

		for (panel in panels){
			panel.draw()
		}

		drawer.draw()
	}
}
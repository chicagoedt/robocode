package org.chicagoedt.rosetteweb

import org.w3c.dom.CanvasRenderingContext2D
import org.chicagoedt.rosette.*
import org.chicagoedt.rosetteweb.editor.*
import org.chicagoedt.rosetteweb.canvas.*
/**
 * The driver to run an Editor canvas for the current game
 * @param game The game that the program is running
 * @param context The context for the canvas to run the editor on
 * @property panels All of the panels in the level
 * @property interactionManager The manager for all interactions, such as clicking and dragging and dropping
 * @property drawer The COLOR_DRAWER to house the actions to choose
 * @property background The drawable for the background color
 */

class EditorDriver(val game: Game, val context: CanvasRenderingContext2D){
	private val panels = ArrayList<Panel>()
	private var interactionManager  = InteractionManager(context, {drawEditor()})
	private var drawer = Drawer(interactionManager, context)
	private val background = Drawable(context)

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
		calculatePanels()
	}

	/**
	 * Calculates the positions of the panels in the canvas. This should be called when the screen size changes
	 */
	fun calculatePanels(){
		var panelX = 0.0
		var panelY = 0.0

		for (panel in panels){
			panel.x = panelX
			panel.y = panelY
			panel.width = PANEL_WIDTH
			panel.height = PANEL_HEIGHT
			panel.recalculate()

			panelX += PANEL_WIDTH
        }
		drawer.x = 0.0
		drawer.y = PANEL_HEIGHT
		drawer.width = DRAWER_WIDTH
		drawer.height = DRAWER_HEIGHT
		drawer.recalculate()


		background.x = 0.0
		background.y = 0.0
		background.width = context.canvas.width.toDouble()
		background.height = context.canvas.height.toDouble()
		background.color = COLOR_BACKGROUND
		background.recalculate()
	}

	/**
	 * Draws all of the elements in this editor
	 */
	fun drawEditor(){
		background.draw()

		for (panel in panels){
			panel.draw()
		}

		drawer.draw()
	}
}
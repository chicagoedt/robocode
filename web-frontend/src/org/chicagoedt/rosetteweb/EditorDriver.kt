package org.chicagoedt.rosetteweb

import org.chicagoedt.rosette.*
import org.chicagoedt.rosetteweb.editor.*
import org.w3c.dom.*

/**
 * The driver for the editor section of the game
 * @param game The game corresponding to this editor
 * @param editor The editor HTML element corresponding to this driver
 * @property panelsTable The container for the panels
 * @property drawer The drawer element for the editor
 */
class EditorDriver(val game : Game, val editor : HTMLElement){
	val panelsTable = editor.children.get("panels")!!.children.get(0)!!.children.get("panelsRow") as HTMLElement
	val drawer = Drawer(editor)

	/**
	 * Calculates everything necessary to switch levels
	 */
	fun calculateNewLevel(){
		val width = ((1.0  / game.currentLevel.players.size.toDouble()) * 100).toString() + "%"
		for ((name, robot) in game.currentLevel.players){
			val panel = Panel(panelsTable, robot, drawer)
			(panel.element.parentElement!! as HTMLElement).style.width = width
		}

		drawer.populate()
	}
}
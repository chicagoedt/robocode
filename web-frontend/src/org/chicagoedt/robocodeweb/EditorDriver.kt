package org.chicagoedt.robocodeweb

import jQuery
import org.chicagoedt.robocode.*
import org.chicagoedt.robocodeweb.editor.*
import org.w3c.dom.*
import kotlin.browser.*
import kotlin.dom.addClass
import kotlin.dom.removeClass

/**
 * The driver for the editor section of the game
 * @param game The game corresponding to this editor
 * @param editor The editor HTML element corresponding to this driver
 * @property panelsTable The container for the panels
 * @property drawer The drawer element for the editor
 */
class EditorDriver(val game : Game, val editor : HTMLElement){
	var panelsTable = document.getElementById("panelsRow") as HTMLElement
	val drawer = Drawer(editor)
	val panels = arrayListOf<Panel>()

	/**
	 * Calculates everything necessary to switch levels
	 */
	fun calculateNewLevel(){
		for (panel in panels){
			panel.removeListeners()
		}
		panels.clear()

		val cNode = panelsTable.cloneNode(false);
        panelsTable.parentNode!!.replaceChild(cNode, panelsTable);
        panelsTable = cNode as HTMLElement

		val width = ((1.0  / game.currentLevel.players.size.toDouble()) * 100).toString() + "%"
		for ((name, robot) in game.currentLevel.players){
			val panel = Panel(panelsTable, robot, drawer)
			panels.add(panel)
			(panel.element.parentElement!! as HTMLElement).style.width = width
		}

		if (game.currentLevel.hasIntro) showIntro()

		drawer.setDroppable()

		drawer.refresh()
	}

	/**
	 * Shows the intro for the new level
	 */
	fun showIntro(){
		val introArea = document.querySelector("#introContainer") as HTMLElement
		val intro = document.querySelector("#intro") as HTMLElement
		val closeIntro = document.querySelector("#closeIntro") as HTMLElement
		intro.innerHTML = game.currentLevel.intro
		jQuery(introArea).fadeIn("slow")

		closeIntro.onclick = {
			jQuery(introArea).fadeOut("fast")
		}


	}

	/**
	 * Checks all hints in the Panels, and shows them if necessary
	 */
	fun checkAllPanelHints(){
		for (panel in panels){
			panel.checkAndShowHint()
		}
	}

	fun disableAllRunButtons(){
		for (panel in panels){
			panel.runButton.disabled = true
			panel.runButton.addClass("disabledPanelHeaderButton")
		}
	}

	fun enableAllRunButtons(){
		for (panel in panels){
			panel.runButton.disabled = false
			panel.runButton.removeClass("disabledPanelHeaderButton")
		}
	}
}
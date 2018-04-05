package org.chicagoedt.rosetteweb

import jQuery
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosetteweb.editor.*
import org.w3c.dom.*
import kotlin.dom.addClass

class EditorDriver(val game : Game, val editor : HTMLElement){
	val panelTable = editor.children.get("panels")!! as HTMLTableElement
	val drawer = Drawer(editor)

	fun calculateNewLevel(){
		for (robot in game.currentLevel.players){
			val panel = Panel(panelTable)
		}

		populateDrawer()
	}

	fun populateDrawer(){
		val moveActionBlock = ActionBlock(drawer.element)
	}
}
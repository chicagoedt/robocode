package org.chicagoedt.rosetteweb

import org.chicagoedt.rosette.*
import org.chicagoedt.rosetteweb.editor.*
import org.w3c.dom.*

class EditorDriver(val game : Game, val editor : HTMLElement){
	val panelsTable = editor.children.get("panels")!!.children.get(0)!!.children.get("panelsRow") as HTMLElement
	val drawer = Drawer(editor)

	fun calculateNewLevel(){
		for ((name, robot) in game.currentLevel.players){
			val panel = Panel(panelsTable, robot)
		}

		populateDrawer()
	}

	fun populateDrawer(){
		val moveActionBlock = ActionBlock(drawer.element)
	}
}
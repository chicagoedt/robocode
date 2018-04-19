package org.chicagoedt.rosetteweb.grid

import org.w3c.dom.HTMLElement
import kotlin.browser.*
import org.chicagoedt.rosette.levels.*
import org.chicagoedt.rosette.tiles.*
import org.chicagoedt.rosette.robots.*

/**
 * The class to hold the tiles on the grid
 * @param level The level in which this tile lives
 * @param gridX The X position of this tile on the grid
 * @param gridY The Y position of this tile on the grid
 * @property tableElement The td element for this tile
 * @property element The div element to be contained in the td element
 */
open class GridTile(var level : Level, var gridX : Int, var gridY : Int){
	var tableElement = document.createElement("td") as HTMLElement
	var element  = document.createElement("div") as HTMLElement
	var player : RobotPlayer? = null

	init{
		tableElement.classList.add("gridTileCell")
		tableElement.appendChild(element)
		element.classList.add("gridTile")
		setWidth()
		refresh()
	}

	/**
	 * Sets the width of the tile based on the level
	 */
	fun setWidth(){
		val width = ((1.0 / level.properties.width.toDouble()) * 100.0).toString() + "%"
		tableElement.style.width = width
	}

	/**
	 * Refreshed the content in this tile
	 */
	open fun refresh(){
		element.classList.remove("obstacleTile")
		element.classList.remove("neutralTile")
		element.classList.remove("victoryTile")
		element.classList.remove("gridPlayer")

		if (player != null){
			element.classList.add("gridPlayer")
		}
		else if (level.tileAt(gridX, gridY) is ObstacleTile){
			element.classList.add("obstacleTile")
		}
		else if (level.tileAt(gridX, gridY) is NeutralTile){
			element.classList.add("neutralTile")
		}
		else if (level.tileAt(gridX, gridY) is VictoryTile){
			element.classList.add("victoryTile")
		}
	}
}
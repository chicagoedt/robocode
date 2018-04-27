package org.chicagoedt.robocodeweb.grid

import org.w3c.dom.HTMLElement
import kotlin.browser.*
import org.chicagoedt.robocode.levels.*
import org.chicagoedt.robocode.tiles.*
import org.chicagoedt.robocode.robots.*

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
	var playerElement  = document.createElement("div") as HTMLElement
	val itemCountElement = document.createElement("div") as HTMLElement
	var player : RobotPlayer? = null
	val playerItemCountElement = document.createElement("div") as HTMLElement

	init{
		tableElement.classList.add("gridTileCell")
		tableElement.appendChild(element)
		itemCountElement.classList.add("tileItemCount")
		element.classList.add("gridTile")

		playerElement.classList.add("gridPlayer")
		playerElement.classList.add("gridTile")
		playerItemCountElement.classList.add("tileItemCount")
		playerElement.appendChild(playerItemCountElement)

		element.appendChild(itemCountElement)
		element.appendChild(playerElement)
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
	fun refresh(){
		displayItemCount()
		element.classList.remove("obstacleTile")
		element.classList.remove("neutralTile")
		element.classList.remove("victoryTile")
		element.classList.remove("gridPlayer")

		if (level.tileAt(gridX, gridY) is ObstacleTile){
			element.classList.add("obstacleTile")
		}
		else if (level.tileAt(gridX, gridY) is NeutralTile){
			element.classList.add("neutralTile")
		}
		else if (level.tileAt(gridX, gridY) is VictoryTile){
			element.classList.add("victoryTile")
		}

		if (player != null){
			playerElement.style.display = "block"
			itemCountElement.style.display = "none"
		}
		else{
			playerElement.style.display = "none"
			itemCountElement.style.display = "block"
		}
	}

	fun displayItemCount(){
		val totalCount = level.tileAt(gridX, gridY).items.totalItemQuantity()
		if (totalCount == 0)itemCountElement.innerText = ""
		else itemCountElement.innerText = totalCount.toString()

		if (player != null){
			val totalPlayerCount = player!!.itemInventory.totalItemQuantity()
			if (totalPlayerCount == 0)playerItemCountElement.innerText = ""
			else playerItemCountElement.innerText = totalPlayerCount.toString()
		}

	}
}
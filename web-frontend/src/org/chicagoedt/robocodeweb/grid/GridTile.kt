package org.chicagoedt.robocodeweb.grid

import org.chicagoedt.robocode.collectibles.ItemManager
import org.w3c.dom.HTMLElement
import kotlin.browser.*
import org.chicagoedt.robocode.levels.*
import org.chicagoedt.robocode.tiles.*
import org.chicagoedt.robocode.robots.*
import org.chicagoedt.robocodeweb.game
import org.w3c.dom.HTMLImageElement
import kotlin.dom.addClass

/**
 * The class to hold the tiles on the grid
 * @param level The level in which this tile lives
 * @param gridX The X position of this tile on the grid
 * @param gridY The Y position of this tile on the grid
 * @property tableElement The td element for this tile
 * @property element The div element to be contained in the td element
 * @property playerElement The image to indicate if a player is on this tile
 * @property itemQuantityImage The image to indicate if an item is on this tile
 * @property player The player that is current only this tile, or null if there is none
 */
open class GridTile(var level : Level, var gridX : Int, var gridY : Int){
	var tableElement = document.createElement("td") as HTMLElement
	var element  = document.createElement("div") as HTMLElement
	var playerElement  = document.createElement("img") as HTMLImageElement
	val itemQuantityImage = document.createElement("img") as HTMLImageElement
	var player : RobotPlayer? = null

	init{
		tableElement.classList.add("gridTileCell")
		tableElement.appendChild(element)
		itemQuantityImage.addClass("tileItemImage")
		itemQuantityImage.src = "res/items/item.png"
		element.classList.add("gridTile")

		playerElement.classList.add("gridPlayer")
		playerElement.classList.add("gridTile")

		element.appendChild(itemQuantityImage)
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
		displayItemIndicator()
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
			playerElement.src = getPlayerImage(game.robots[player!!.name]!!.graphic)
		}
		else{
			playerElement.style.display = "none"
			playerElement.src = "";
		}
	}

	/**
	 * Displays the item indicator on the tile if at least one item is present and the player is not present
	 */
	fun displayItemIndicator(){
		val hasItem = level.tileAt(gridX, gridY).items.allItemTypes().size > 0
		if (player == null && hasItem){
			itemQuantityImage.style.display = "block"
		}
		else{
			itemQuantityImage.style.display = "none"
		}
	}

	/**
	 * Gets an image with the correct direction from a path
	 * @param path The folder containing left.png, right.png, up.png, down.png
	 * @return The path to the correct image
	 */
	fun getPlayerImage(path : String) : String{
		if (player!!.direction == RobotOrientation.DIRECTION_UP) return path + "up.png"
		else if (player!!.direction == RobotOrientation.DIRECTION_DOWN) return path + "down.png"
		else if (player!!.direction == RobotOrientation.DIRECTION_RIGHT) return path + "right.png"
		else return path + "left.png"
	}
}
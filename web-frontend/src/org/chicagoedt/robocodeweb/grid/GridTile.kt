package org.chicagoedt.robocodeweb.grid

import org.chicagoedt.robocode.collectibles.ItemManager
import org.w3c.dom.HTMLElement
import kotlin.browser.*
import org.chicagoedt.robocode.levels.*
import org.chicagoedt.robocode.tiles.*
import org.chicagoedt.robocode.robots.*
import org.chicagoedt.robocodeweb.game
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLImageElement
import kotlin.dom.addClass

/**
 * The class to hold the tiles on the grid
 * @param level The level in which this tile lives
 * @param gridX The X position of this tile on the grid
 * @param gridY The Y position of this tile on the grid
 * @property tableElement The td element for this tile
 * @property element The div element to be contained in the td element
 * @property itemQuantityImage The image to indicate if an item is on this tile
 */
open class GridTile(var level : Level, var gridX : Int, var gridY : Int){
	var tableElement = document.createElement("td") as HTMLElement
	var element  = document.createElement("div") as HTMLElement

	val itemQuantityImage = document.createElement("img") as HTMLImageElement
	val itemQuantityText = document.createElement("div") as HTMLDivElement
	val itemQuantityContainer = document.createElement("div") as HTMLDivElement

	init{
		tableElement.classList.add("gridTileCell")
		tableElement.appendChild(element)

		itemQuantityImage.addClass("itemQuantityImage")
		itemQuantityText.addClass("itemQuantityText")
		itemQuantityContainer.addClass("itemQuantityContainer")
		itemQuantityContainer.appendChild(itemQuantityImage)
		itemQuantityContainer.appendChild(itemQuantityText)

		element.classList.add("gridTile")

		element.appendChild(itemQuantityContainer)

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
	}

	/**
	 * Displays the item indicator on the tile if at least one item is present and the player is not present
	 */
	fun displayItemIndicator(){
		val hasItem = level.tileAt(gridX, gridY).items.allItemTypes().size > 0
		if (hasItem){
			val hasMultipleItemTypes = level.tileAt(gridX, gridY).items.allItemTypes().size > 1
			if (hasMultipleItemTypes){

			}
			else{
				val typeID = level.tileAt(gridX, gridY).items.allItemTypes()[0]
				val typeName = ItemManager.getItem(typeID)!!.name

				itemQuantityImage.src = "res/items/" + typeName + ".png"

				val hasMultipleItems = level.tileAt(gridX, gridY).items.itemQuantity(typeID) > 1
				if (hasMultipleItems){
					val itemCount = level.tileAt(gridX, gridY).items.itemQuantity(typeID)
					itemQuantityText.innerHTML = itemCount.toString()
				}
			}
			itemQuantityContainer.style.display = "block"
		}
		else{
			itemQuantityContainer.style.display = "none"
		}
	}
}
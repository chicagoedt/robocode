package org.chicagoedt.robocodeweb

import org.w3c.xhr.*
import org.w3c.dom.events.*
import org.w3c.dom.*
import org.chicagoedt.robocode.robots.*
import org.chicagoedt.robocode.levels.*
import org.chicagoedt.robocode.tiles.*
import jQuery
import org.chicagoedt.robocode.collectibles.ItemManager
import org.chicagoedt.robocode.collectibles.etc.Sand
import org.chicagoedt.robocode.collectibles.etc.Water

/**
 * Retrieves and parses the configuration file
 * @param name The name of the configuration file
 */
class ConfigDriver(val name : String, val callback : (ArrayList<Robot>, ArrayList<Level>) -> Unit){
	val request = XMLHttpRequest()

	init{
		request.open("GET", name, true)
		request.onreadystatechange = ::readXML
		request.send()
	}

	/**
	 * Parses the return XML
	 * @param e The event object from the HTTP request
	 */
	fun readXML(e : Event){
		if (e.target.asDynamic().readyState == 4){
			val xml = jQuery.parseXML(e.target.asDynamic().responseText)

			val game = xml.querySelector("game")!!
			val robots = game.querySelector("robots")!!
			val levels = game.querySelector("levels")!!
			
			val robotsList = readRobots(robots)
			val levelsList = readLevels(levels)

			callback.invoke(robotsList, levelsList)
		}
	}

	/**
	 * Reads the levels from a <levels> element
	 * @param robotsElement The <levels> element to read from
	 * @return The arraylist of Level objects
	 */
	fun readLevels(levelsElement : Element) : ArrayList<Level>{
		val levels = arrayListOf<Level>()

		val levelsElementList = levelsElement.querySelectorAll("level")
		for (i in 0 until levelsElementList.length){
			val levelData = levelsElementList.item(i) as Element

			val name = levelData.attributes.getNamedItem("name")!!.value
			val difficulty = levelData.attributes.getNamedItem("difficulty")!!.value.toInt()

			val gridData = levelData.querySelector("grid")!!
			val height = gridData.querySelectorAll("gridRow").length
			val width = (gridData.querySelectorAll("gridRow").item(0) as Element).querySelectorAll("gridTile").length
			val grid = readGrid(gridData)

			val level = Level(Level.Properties(name, difficulty, width, height))
			level.makeGrid(grid)

			val robots = arrayListOf<RobotPlayer>()
			val robotsData = levelData.querySelectorAll("robot")
			for (j in 0 until robotsData.length){
				val robotData = robotsData.item(j) as Element
				val robotName = robotData.attributes.getNamedItem("name")!!.value
				val x = robotData.attributes.getNamedItem("x")!!.value.toInt()
				val y = robotData.attributes.getNamedItem("y")!!.value.toInt()
				val directionString = robotData.attributes.getNamedItem("direction")!!.value

				var direction = RobotOrientation.DIRECTION_UP

				if (directionString == "up") direction = RobotOrientation.DIRECTION_UP
				else if (directionString == "down") direction = RobotOrientation.DIRECTION_DOWN
				else if (directionString == "left") direction = RobotOrientation.DIRECTION_LEFT
				else if (directionString == "right") direction = RobotOrientation.DIRECTION_RIGHT

				robots.add(RobotPlayer(robotName, x, y, direction, level))
			}

			level.setPlayers(robots)
			levels.add(level)
		}
		return levels
	}

	/**
	 * Reads the grid from a <grid> element
	 * @param gridData The <grid> element to read from
	 * @return The arraylist of arraylists of Tile objects
	 */
	fun readGrid(gridData : Element) : ArrayList<ArrayList<Tile>>{
		val grid = arrayListOf<ArrayList<Tile>>()

		val gridDataRows = gridData.querySelectorAll("gridRow")

		val height = gridDataRows.length
		val width = (gridDataRows.item(0) as Element).querySelectorAll("gridTile").length

		for (j in 0 until gridDataRows.length){
			val rowData = gridDataRows.item(j) as Element
			val rowDataTiles = rowData.querySelectorAll("gridTile")
			val row = arrayListOf<Tile>()

			for (k in 0 until rowDataTiles.length){
				val type = (rowDataTiles.item(k) as Element).attributes.getNamedItem("type")!!.value
				var tile : Tile? = null
				when(type){
					"obstacle" -> tile = ObstacleTile()
					"neutral" -> tile = NeutralTile()
					"victory" -> tile = VictoryTile()
				}

				row.add(tile!!)

				val items = (rowDataTiles.item(k) as Element).querySelectorAll("item")
				for (t in 0 until items.length){
					val itemName = (items[t] as Element).attributes["name"]!!.value
					val itemCount = (items[t] as Element).attributes["count"]!!.value.toInt()
					val id = getItemIDFromName(itemName)
					for (y in 0 until itemCount) tile.items.addItem(id)
				}
			}
			grid.add(row)
		}

		return grid;
	}

	/**
	 * Reads the robots from a <robots> element
	 * @param robotsElement The <robots> element to read from
	 * @return The arraylist of Robot objects
	 */
	fun readRobots(robotsElement : Element) : ArrayList<Robot>{
		val robots = arrayListOf<Robot>()

		val robotsElementList = robotsElement.querySelectorAll("robot")
		for (i in 0 until robotsElementList.length){
			val robotData = robotsElementList.item(i) as Element
			val name = robotData.attributes.getNamedItem("name")!!.value
			val graphic = robotData.attributes.getNamedItem("graphic")!!.value

			val robot = Robot(name, graphic)
			robots.add(robot)
		}
		return robots
	}

	/**
	 * Gets the ID of an item from the name
	 * @return The ID of an item with the name [name]
	 */
	fun getItemIDFromName(name : String) : Int{
		for (item in ItemManager.getAllItems()){
			if (name.toLowerCase() == item.name.toLowerCase())
				return item.id
		}
		return -1
	}
}
package org.chicagoedt.robocodeweb

import org.w3c.xhr.*
import org.w3c.dom.events.*
import org.w3c.dom.*
import org.chicagoedt.robocode.robots.*
import org.chicagoedt.robocode.levels.*
import org.chicagoedt.robocode.tiles.*
import jQuery
import org.chicagoedt.robocode.collectibles.Collectible
import org.chicagoedt.robocode.collectibles.ItemInventory
import org.chicagoedt.robocode.collectibles.ItemManager

/**
 * Retrieves and parses the configuration file
 * @param name The name of the configuration file
 */
class ConfigDriver(val name : String, val callback : (ArrayList<Robot>, ArrayList<Level>, ArrayList<Level.Theme>) -> Unit){
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
			val themes = game.querySelector("themes")!!
			val items = game.querySelector("items")!!

			val robotsList = readRobots(robots)
			val themesList = readThemes(themes)
			val itemsList = readItems(items)

			itemsList.forEach {item ->
				ItemManager.addItem(item)
			}

			val levelsList = readLevels(levels, themesList)

			callback.invoke(robotsList, levelsList, themesList)
		}
	}

	/**
	 * Reads the levels from a <levels> element
	 * @param robotsElement The <levels> element to read from
	 * @return The arraylist of Level objects
	 */
	fun readLevels(levelsElement : Element, themes : ArrayList<Level.Theme>) : ArrayList<Level>{
		val levels = arrayListOf<Level>()

		val levelsElementList = levelsElement.querySelectorAll("level")
		for (i in 0 until levelsElementList.length){
			val levelData = levelsElementList.item(i) as Element

			val name = levelData.attributes.getNamedItem("name")!!.value
			val difficulty = levelData.attributes.getNamedItem("difficulty")!!.value.toInt()
			val themeName = levelData.attributes.getNamedItem("theme")!!.value
			val instructions = levelData.querySelector("instructions")!!.innerHTML

			var hasIntro = false
			var intro = ""
			val introData = levelData.querySelector("intro")
			if (introData != null){
				hasIntro = true
				intro = introData.innerHTML
			}

			val gridData = levelData.querySelector("grid")!!
			val height = gridData.querySelectorAll("gridRow").length
			val width = (gridData.querySelectorAll("gridRow").item(0) as Element).querySelectorAll("gridTile").length
			val grid = readGrid(gridData)

			val level = Level(Level.Properties(name, difficulty, instructions, width, height))

			for (theme in themes){
				if (theme.name == themeName){
					level.theme = theme
					break
				}
			}

			readLevelConditions(levelData, level)
			readLevelVictoryConditions(levelData, level)

			level.makeGrid(grid)

			val robots = arrayListOf<RobotPlayer>()
			val robotsData = levelData.querySelectorAll("robot")
			for (j in 0 until robotsData.length){
				val robotData = robotsData.item(j) as Element
				val robotName = robotData.attributes.getNamedItem("name")!!.value
				val x = robotData.attributes.getNamedItem("x")!!.value.toInt()
				val y = robotData.attributes.getNamedItem("y")!!.value.toInt()
				val directionString = robotData.attributes.getNamedItem("direction")!!.value
				var actionLimit : Int
				try{
					actionLimit = robotData.attributes.getNamedItem("actionLimit")!!.value.toInt()
				}
				catch (e : NullPointerException){
					actionLimit = -1
				}

				var direction = RobotOrientation.DIRECTION_UP

				if (directionString == "up") direction = RobotOrientation.DIRECTION_UP
				else if (directionString == "down") direction = RobotOrientation.DIRECTION_DOWN
				else if (directionString == "left") direction = RobotOrientation.DIRECTION_LEFT
				else if (directionString == "right") direction = RobotOrientation.DIRECTION_RIGHT

				val player = RobotPlayer(robotName, x, y, direction, level)
				player.actionLimit = actionLimit

				robots.add(player)
			}

			level.setPlayers(robots)
			levels.add(level)
		}
		return levels
	}

	/**
	 * Reads which features should be enabled for the levels
	 * @param levelData The XML element containing config data for the level
	 * @param level The Level to set the conditions for
	 */
	fun readLevelConditions(levelData : Element, level : Level){
		var useTopic = true
		var useSensors = true
		var useMove = true
		var useTurn = true
		var usePickUpItem = true
		var useDropItem = true
		var useForLoop = true
		var useReadSensor = true
		var topicOnlyForMove = false

		if (levelData.hasAttribute("useTopic")){
			if (levelData.getAttribute("useTopic")!!.equals("false", true))
				useTopic = false
		}

		if (levelData.hasAttribute("useSensors")){
			if (levelData.getAttribute("useSensors")!!.equals("false", true))
				useSensors = false
		}

		if (levelData.hasAttribute("useMove")){
			if (levelData.getAttribute("useMove")!!.equals("false", true))
				useMove = false
		}

		if (levelData.hasAttribute("useTurn")){
			if (levelData.getAttribute("useTurn")!!.equals("false", true))
				useTurn = false
		}

		if (levelData.hasAttribute("usePickUpItem")){
			if (levelData.getAttribute("usePickUpItem")!!.equals("false", true))
				usePickUpItem = false
		}

		if (levelData.hasAttribute("useDropItem")){
			if (levelData.getAttribute("useDropItem")!!.equals("false", true))
				useDropItem = false
		}

		if (levelData.hasAttribute("useForLoop")){
			if (levelData.getAttribute("useForLoop")!!.equals("false", true))
				useForLoop = false
		}

		if (levelData.hasAttribute("useReadSensor")){
			if (levelData.getAttribute("useReadSensor")!!.equals("false", true))
				useReadSensor = false
		}

		if (levelData.hasAttribute("topicOnlyForMove")){
			if (levelData.getAttribute("topicOnlyForMove")!!.equals("true", true))
				topicOnlyForMove = true
		}

		level.conditions = Level.Conditions(useTopic,
				useSensors,
				useMove,
				useTurn,
				usePickUpItem,
				useDropItem,
				useForLoop,
				useReadSensor,
				topicOnlyForMove)
	}

	/**
	 * Reads the type of victory that this level should have
	 * @param levelData The XML element containing victory data for the level
	 * @param level The Level to set the victory condition for
	 */
	fun readLevelVictoryConditions(levelData : Element, level : Level){
		val victoryData = levelData.querySelector("victory")!!
		val type = victoryData.getAttribute("type")!!

		if (type.equals("tile", true)) level.victoryType = VictoryType.TILE
		if (type.equals("itemposition", true)){

			val inventoryData = levelData.querySelector("inventory")!!
			val x = inventoryData.getAttribute("x")!!.toInt()
			val y = inventoryData.getAttribute("y")!!.toInt()
			val inventory = ItemInventory()

			val items = inventoryData.querySelectorAll("item")!!
			for (i in 0 until items.length){
				val itemName = (items[i] as Element).attributes["name"]!!.value
				val itemCount = (items[i] as Element).attributes["count"]!!.value.toInt()
				val id = getItemIDFromName(itemName)
				for (j in 0 until itemCount) inventory.addItem(id)
			}

			level.itemPositionData = Level.ItemPositionData(x, y, inventory)
			level.victoryType = VictoryType.ITEM_POSITION
		}
	}

	/**
	 * Reads the global themes for the game
	 * @param themesElement The element containing the theme XML
	 * @return An arraylist of Level.Theme objects
	 */
	fun readThemes(themesElement : Element) : ArrayList<Level.Theme>{
		val themes = arrayListOf<Level.Theme>()
		val themesElementList = themesElement.querySelectorAll("theme").asList() as List<Element>
		for (theme in themesElementList){
			val name = theme.getAttribute("name")!!
			var victoryImg = ""
			var victoryString = "Victory"
			var obstacleImg = ""
			var obstacleString = "Obstacle"
			var neutralImg = ""

			val blocks = theme.querySelectorAll("block").asList() as List<Element>
			for (block in blocks){
				val type = block.getAttribute("type")
				if (type == "victory"){
					if (block.getAttribute("img") != null) victoryImg = block.getAttribute("img")!!
					if (block.getAttribute("name") != null) victoryString = block.getAttribute("name")!!
				}
				else if (type == "obstacle"){
					if (block.getAttribute("img") != null) obstacleImg = block.getAttribute("img")!!
					if (block.getAttribute("name") != null) obstacleString = block.getAttribute("name")!!
				}
				else if (type == "neutral"){
					if (block.getAttribute("img") != null) neutralImg = block.getAttribute("img")!!
				}
			}

			themes.add(Level.Theme(name, victoryImg, victoryString, obstacleImg, obstacleString, neutralImg))
		}

		return themes
	}

	/**
	 * Reads the global items for the game
	 * @param itemsElement The element containing the theme XML
	 * @return An arraylist of Level.Theme objects
	 */
	fun readItems(itemsElement : Element) : ArrayList<Collectible>{
		val items = arrayListOf<Collectible>()
		val itemElementList = itemsElement.querySelectorAll("item").asList() as List<Element>
		for (item in itemElementList){
			val name = item.getAttribute("name")!!
			val id = item.getAttribute("id")!!.toInt()
			val graphic = item.getAttribute("graphic")!!.toString()

			items.add(Collectible(id, name, graphic))
		}
		return items
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
					val itemID = (items[t] as Element).attributes["id"]!!.value.toInt()
					val itemCount = (items[t] as Element).attributes["count"]!!.value.toInt()
					for (y in 0 until itemCount) tile.items.addItem(itemID)
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
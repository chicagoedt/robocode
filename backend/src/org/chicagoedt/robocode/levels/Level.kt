package org.chicagoedt.robocode.levels

import org.chicagoedt.robocode.tiles.Tile
import org.chicagoedt.robocode.robots.RobotPlayer
import org.chicagoedt.robocode.tiles.NeutralTile
import org.chicagoedt.robocode.*
import org.chicagoedt.robocode.collectibles.ItemInventory
import org.chicagoedt.robocode.tiles.VictoryTile

enum class VictoryType{
    TILE,
    ITEM_POSITION
}

/**
 * A single challenge for the user to complete
 * @param properties The properties object for the level
 * @param playersList A list of all robot players in the level. The names should correspond to names of robots passed to the Game object
 * @property grid A 2D-Arraylist of all tiles in the level
 * @property players All of the RobotPlayers in the level
 * @property victoryType The type of victory in this level
 * @property itemPositionData The items and position using for Victory.ITEM_POSITION victory condition. Null if other victory.
 */
class Level(var properties: Properties) {
    /**
     * The properties to be stored in a Level
     * @param name The name of the level
     * @param difficulty The difficulty of the level. This is just for categorization, it will not affect the level at all.
     * @param width The width of the grid on the level
     * @param height The height of the grid on the level
     */
    data class Properties(val name: String,
                          val difficulty : Int,
                          val width : Int,
                          val height : Int)

    /**
     * The conditions for what should be displayed in a Level
     * @param useTopic True if topic features should be used, false otherwise
     * @param useSensors True if sensor features should be used, false otherwise
     * @param useMove True if Move block should be enabled, false otherwise
     * @param useTurn True if Turn block should be enabled, false otherwise
     * @param usePickUpItem True if Pick Up Item block should be enabled, false otherwise
     * @param useDropItem True if Drop Item block should be enabled, false otherwise
     * @param useForLoop True if For Loop block should be enabled, false otherwise
     * @param useReadSensor True if Read Sensor block should be enabled, false otherwise
     */
    data class Conditions(val useTopic : Boolean,
                          val useSensors : Boolean,
                          val useMove : Boolean,
                          val useTurn : Boolean,
                          val usePickUpItem : Boolean,
                          val useDropItem : Boolean,
                          val useForLoop : Boolean,
                          val useReadSensor : Boolean,
                          val topicOnlyForMove : Boolean)

    /**
     * The theme to display for the level
     * @param victoryImg The path to the image to display on a victory tile
     * @param victoryString The name to call the victory tile
     * @param obstacleImg The path to the image to display on an obstacle tile
     * @param obstacleString The name to call the obstacle tile
     * @param neutralImg The path to the image to display on a neutral tile
     */
    data class Theme(val name : String,
                     val victoryImg : String,
                     val victoryString : String,
                     val obstacleImg : String,
                     val obstacleString : String,
                     val neutralImg : String)

    /**
     * A position to check for items at. Used for Victory.ITEM_POSITION victory condition
     * @param x The x position of the tile to check
     * @param y The y position of the tile to check
     * @param itemInventory The inventory representing the items that should be there
     */
    data class ItemPositionData(val x : Int,
                                val y : Int,
                                val itemInventory: ItemInventory)

    var conditions = Level.Conditions(true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false)

    var theme = Level.Theme("Default",
            "",
            "Victory",
            "",
            "Obstacle",
            "")

    var victoryType = VictoryType.TILE
    var itemPositionData : ItemPositionData? = null

    private var grid = arrayListOf<ArrayList<Tile>>()
    val players: HashMap<String, RobotPlayer> = hashMapOf()

    init{
        val blankGrid = arrayListOf<ArrayList<Tile>>()
        for (i in 0..properties.height - 1){
            val list = ArrayList<Tile>()
            for (j in 0..properties.width - 1){
                list.add(NeutralTile())
            }
            blankGrid.add(list)
        }
        makeGrid(blankGrid)
    }

    fun setPlayers(playersList: ArrayList<RobotPlayer>){
        for (player in playersList){
            players[player.name] = player
        }
    }

    /**
     * Creates a grid with 0,0 in the bottom left corner
     *
     * This method exists because it's easier to create a 2D array visually with 0,0 in the top left
     * @param newGrid A grid with 0,0 in the top left corner
     */
    fun makeGrid(newGrid: ArrayList<ArrayList<Tile>>){
        grid = arrayListOf<ArrayList<Tile>>()
        for (i in 0..properties.height - 1){
            val list = ArrayList<Tile>()
            for (j in 0..properties.width - 1){
                newGrid[properties.height - 1 - i][j].items.oneTypeOnly = true
                list.add(newGrid[properties.height - 1 - i][j])
            }
            grid.add(list)
        }
    }

    /**
     * Saves the items in all the tiles to be restored at a later time
     */
    fun saveCheckpoint(){
        for (i in 0 until properties.height){
            for (j in 0 until properties.width){
                grid[i][j].items.saveCheckpoint()
            }
        }
        for ((name, player) in players){
            player.saveCheckpoint()
        }
    }
    
    /**
     * Restores the items in the tiles to the last time [saveLevelItemsState] was called
     */
    fun restoreCheckpoint(){
        for (i in 0 until properties.height){
            for (j in 0 until properties.width){
                grid[i][j].items.restoreCheckpoint()
            }
        }
        for ((name, player) in players){
            player.restoreCheckpoint()
        }
        broadcastEvent(Event.LEVEL_UPDATE)
    }

    /**
     * @param x The X value of the requested point
     * @param y the Y value of the requested point
     * @return The tile at a specific point
     */
    fun tileAt(x: Int, y: Int): Tile {
        return grid[y][x]
    }

    /**
     * Checks if the player has won the level according to the victory condition
     * @param player The player to check the conditions for
     * @return True if the player has won, false if they have not
     */
    fun checkForVictory(player: RobotPlayer) : Boolean{
        if (victoryType == VictoryType.TILE){
            return isOnVictoryTile(player)
        }
        else if (victoryType == VictoryType.ITEM_POSITION){
            return isItemPositionDataMatched()
        }
        return false
    }

    /**
     * Checks if the player has won the level in a Victory.TILE victory condition scenario
     * @param player The player to check the position for
     * @return True if the player has won, false if they have not
     */
    private fun isOnVictoryTile(player: RobotPlayer) : Boolean{
        if (this.tileAt(player.x, player.y) is VictoryTile) return true

        return false
    }

    /**
     * Checks if the player has won the level in a Victory.ITEM_POSITION victory condition scenario
     * @return True if the player has won, false if they have not
     */
    private fun isItemPositionDataMatched() : Boolean{
        val inv1 = itemPositionData!!.itemInventory
        val inv2 = this.tileAt(itemPositionData!!.x, itemPositionData!!.y).items

        val inv1Types = inv1.allItemTypes()

        for (type in inv1Types){
            if (inv1.itemQuantity(type) != inv2.itemQuantity(type)){
                return false
            }
        }

        return true
    }
}
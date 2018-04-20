package org.chicagoedt.rosette.levels

import org.chicagoedt.rosette.tiles.Tile
import org.chicagoedt.rosette.robots.RobotPlayer
import org.chicagoedt.rosette.tiles.NeutralTile
import org.chicagoedt.rosette.*

/**
 * A single challenge for the user to complete
 * @param properties The properties object for the level
 * @param playersList A list of all robot players in the level. The names should correspond to names of robots passed to the Game object
 * @property grid A 2D-Arraylist of all tiles in the level
 * @property players All of the RobotPlayers in the level
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
    internal fun makeGrid(newGrid: ArrayList<ArrayList<Tile>>){
        grid = arrayListOf<ArrayList<Tile>>()
        for (i in 0..properties.height - 1){
            val list = ArrayList<Tile>()
            for (j in 0..properties.width - 1){
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
        eventListener.invoke(Event.LEVEL_UPDATE)
    }

    /**
     * @param x The X value of the requested point
     * @param y the Y value of the requested point
     * @return The tile at a specific point
     */
    fun tileAt(x: Int, y: Int): Tile {
        return grid[y][x]
    }
}
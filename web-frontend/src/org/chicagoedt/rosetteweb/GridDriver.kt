package org.chicagoedt.rosetteweb

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.tiles.*
import org.chicagoedt.rosetteweb.grid.*

/**
 * The driver to run a Grid canvas for the current game
 * @param game The game that the program is running
 * @param context The context for the canvas to run the grid on
 * @property widthInterval The width for each tile on the grid
 * @property heightInterval The height for each tile on the grid
 * @property separation The spacing between each tile on the grid
 * @property tileLocations TileLocation objects corresponding to each Tile on the grid
 * @property playerLocations TileLocation obejcts corresponding to each RobotPlayer in the level
 */

class GridDriver(val game: Game, val context: CanvasRenderingContext2D){
    var widthInterval = 0.0
    var heightInterval = 0.0
    val separation = 0
    val tileLocations = ArrayList<ArrayList<TileLocation>>()
    val playerLocations = ArrayList<PlayerLocation>()

    /**
     * Calculates all of the necessary information when switching levels
     */
    fun calculateNewLevel(){
        for (x in 0..game.currentLevel.properties.width-1){
            val list = arrayListOf<TileLocation>()
            tileLocations.add(list)
            for (y in 0..game.currentLevel.properties.height-1){
                list.add(TileLocation(x.toDouble(), y.toDouble(), context, game.currentLevel))
            }
        }

        for ((name, player) in game.currentLevel.players){
            playerLocations.add(PlayerLocation(player.x.toDouble(), player.y.toDouble(), context, game.currentLevel, player))
        }

        calculateTiles()
        calculatePlayers()
    }

    /**
     * Calculates all of the tile locations and sizes
     */
    fun calculateTiles(){
        widthInterval = (context.canvas.width.toDouble()) / (game.currentLevel.properties.width.toDouble())
        heightInterval = (context.canvas.height.toDouble()) / (game.currentLevel.properties.height.toDouble())
        var tileX = 0
        var tileY = 0

        for (x in 0..game.currentLevel.properties.width-1){
            for (y in 0..game.currentLevel.properties.height-1){
                val loc = tileLocations[x][y]
                loc.x = (tileX + (separation / 4)).toDouble()
                loc.y = (tileY + (separation / 4)).toDouble()
                loc.height = (heightInterval - (separation / 2)).toDouble()
                loc.width = (widthInterval - (separation / 2)).toDouble()

                tileY += heightInterval.toInt()
            }
            tileX += widthInterval.toInt()
            tileY = 0
        }
    }

    /**
     * Calculates all of the player locations and sizes
     */
    fun calculatePlayers(){
        for (player in playerLocations){
            player.gridX = player.player.x.toDouble()
            player.gridY = player.player.y.toDouble()
            player.x = tileLocations[player.gridX.toInt()][player.gridY.toInt()].x
            player.y = tileLocations[player.gridX.toInt()][player.gridY.toInt()].y
            player.height = tileLocations[player.gridX.toInt()][player.gridY.toInt()].height
            player.width = tileLocations[player.gridX.toInt()][player.gridY.toInt()].width
        }
    }

    /**
     * Draws all tiles and players using the calculated values
     */
    fun drawGrid(){
        for (list in tileLocations){
            for (tile in list){
                tile.draw()
            }
        }

        for (player in playerLocations){
            player.draw()
        } 
    }
}
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
 * @property tileLocations TileLocation objects corresponding to each Tile on the grid
 * @property playerLocations TileLocation obejcts corresponding to each RobotPlayer in the level
 */

class GridDriver(val game: Game, val context: CanvasRenderingContext2D){
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
            playerLocations.add(PlayerLocation(context, player))
        }

        calculateTiles()
    }

    /**
     * Calculates all of the tile locations and sizes
     */
    fun calculateTiles(){
        var tileX = 0.0
        var tileY = 0.0

        for (x in 0..game.currentLevel.properties.width-1){
            for (y in 0..game.currentLevel.properties.height-1){
                val loc = tileLocations[x][y]
                loc.x = tileX
                loc.y = tileY
                loc.height = TILE_HEIGHT
                loc.width = TILE_WIDTH
                loc.recalculate()

                tileY += TILE_HEIGHT
            }
            tileX += TILE_WIDTH
            tileY = 0.0
        }
        calculatePlayers()
    }

    /**
     * Calculates all of the player locations and sizes
     */
    fun calculatePlayers(){
        for (player in playerLocations){
            val tile = tileLocations[player.gridX.toInt()][player.gridY.toInt()]

            player.x = tile.x
            player.y = tile.y
            player.width = tile.width
            player.height = tile.height
            player.recalculate()
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
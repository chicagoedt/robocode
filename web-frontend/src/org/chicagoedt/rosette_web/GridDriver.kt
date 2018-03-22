package org.chicagoedt.rosette_web

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Tiles.*
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

    /**
     * The class for storing the boundaries of tiles so they don't have to be recalculated often
     * @param x The X location of the tile corresponding to the level grid
     * @param y The Y location of the tile corresponding to the level grid
     * @property screenX The location on the canvas of the tile
     * @property screenY The location on the canvas of the tile
     * @property screenHeight The height of the tile on the screen
     * @property screenWidth The width of the tile on the screen
     */
    data class TileLocation(var x : Double, var y : Double){
        var screenX = 0.0
        var screenY = 0.0
        var screenHeight = 0.0
        var screenWidth = 0.0
    }

    var widthInterval = 0.0
    var heightInterval = 0.0
    val separation = 0
    val tileLocations = ArrayList<ArrayList<TileLocation>>()
    val playerLocations = ArrayList<TileLocation>()

    init{
        calculateNewLevel()
    }

    /**
     * Calculates all of the necessary information when switching levels
     */
    fun calculateNewLevel(){
        for (x in 0..game.currentLevel.properties.width-1){
            val list = arrayListOf<TileLocation>()
            tileLocations.add(list)
            for (y in 0..game.currentLevel.properties.height-1){
                list.add(TileLocation(x.toDouble(), y.toDouble()))
            }
        }

        for (player in game.currentLevel.playersList){
            playerLocations.add(TileLocation(player.x.toDouble(), player.y.toDouble()))
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
                loc.screenX = (tileX + (separation / 4)).toDouble()
                loc.screenY = (tileY + (separation / 4)).toDouble()
                loc.screenHeight = (heightInterval - (separation / 2)).toDouble()
                loc.screenWidth = (widthInterval - (separation / 2)).toDouble()

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
            player.screenX = tileLocations[player.x.toInt()][player.y.toInt()].screenX
            player.screenY = tileLocations[player.x.toInt()][player.y.toInt()].screenY
            player.screenHeight = tileLocations[player.x.toInt()][player.y.toInt()].screenHeight
            player.screenWidth = tileLocations[player.x.toInt()][player.y.toInt()].screenWidth
        }
    }

    /**
     * Draws all tiles and players using the calculated values
     */
    fun drawGrid(){
        for (x in 0..game.currentLevel.properties.width-1){
            for (y in 0..game.currentLevel.properties.height-1){
                if (game.currentLevel.tileAt(x,y) is NeutralTile) context.fillStyle = "#64B5F6" //blue
                else if (game.currentLevel.tileAt(x,y) is ObstacleTile) context.fillStyle = "#616161" //gray
                else if (game.currentLevel.tileAt(x,y) is VictoryTile) context.fillStyle = "#FFF176" //yellow

                val loc = tileLocations[x][y]

                context.fillRect(loc.screenX, loc.screenY, loc.screenWidth, loc.screenHeight)
            }
        }

        for (player in playerLocations){
            context.fillStyle = "#D32F2F" //red
            context.fillRect(player.screenX, player.screenY, player.screenWidth, player.screenHeight)
        }
        
    }
}
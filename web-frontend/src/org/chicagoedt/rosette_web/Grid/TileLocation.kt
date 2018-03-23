package org.chicagoedt.rosette_web.Grid

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Tiles.*
import org.chicagoedt.rosette_web.Grid.*
import org.chicagoedt.rosette.Levels.*

/**
 * The class for storing the boundaries of tiles so they don't have to be recalculated often
 * @param x The X location of the tile corresponding to the level grid
 * @param y The Y location of the tile corresponding to the level grid
 * @param context The context to draw the tile on
 * @param level The level that the tile is a part of
 * @property screenX The location on the canvas of the tile
 * @property screenY The location on the canvas of the tile
 * @property screenHeight The height of the tile on the screen
 * @property screenWidth The width of the tile on the screen
 */
open class TileLocation(var x : Double, var y : Double, var context : CanvasRenderingContext2D, var level : Level){
    var screenX = 0.0
    var screenY = 0.0
    var screenHeight = 0.0
    var screenWidth = 0.0

    /**
     * Draws the tile on the screen
     */
    open fun draw(){
        if (level.tileAt(x.toInt(),y.toInt()) is NeutralTile) context.fillStyle = "#64B5F6" //blue
        else if (level.tileAt(x.toInt(),y.toInt()) is ObstacleTile) context.fillStyle = "#212121" //gray
        else if (level.tileAt(x.toInt(),y.toInt()) is VictoryTile) context.fillStyle = "#FFEB3B" //yellow

        context.fillRect(screenX, screenY, screenWidth, screenHeight)
    }
}
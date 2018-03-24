package org.chicagoedt.rosetteweb.grid

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Tiles.*
import org.chicagoedt.rosetteweb.grid.*
import org.chicagoedt.rosette.Levels.*

/**
 * The class for storing the boundaries of tiles so they don't have to be recalculated often
 * @param gridX The X location of the tile corresponding to the level grid
 * @param gridY The Y location of the tile corresponding to the level grid
 * @param context The context to draw the tile on
 * @param level The level that the tile is a part of
 * @property x The location on the canvas of the tile
 * @property y The location on the canvas of the tile
 * @property height The height of the tile on the screen
 * @property width The width of the tile on the screen
 */
open class TileLocation(var gridX : Double, var gridY : Double, var context : CanvasRenderingContext2D, var level : Level){
    var x = 0.0
    var y = 0.0
    var height = 0.0
    var width = 0.0

    /**
     * Draws the tile on the screen
     */
    open fun draw(){
        if (level.tileAt(gridX.toInt(),gridY.toInt()) is NeutralTile) context.fillStyle = "#64B5F6" //blue
        else if (level.tileAt(gridX.toInt(),gridY.toInt()) is ObstacleTile) context.fillStyle = "#212121" //gray
        else if (level.tileAt(gridX.toInt(),gridY.toInt()) is VictoryTile) context.fillStyle = "#FFEB3B" //yellow

        context.fillRect(x, y, width, height)
    }
}
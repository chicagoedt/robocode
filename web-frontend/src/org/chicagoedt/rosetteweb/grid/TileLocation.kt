package org.chicagoedt.rosetteweb.grid

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.tiles.*
import org.chicagoedt.rosetteweb.grid.*
import org.chicagoedt.rosette.levels.*
import org.chicagoedt.rosetteweb.canvas.Drawable

/**
 * The class for storing the boundaries of tiles so they don't have to be recalculated often
 * @param gridX The X location of the tile corresponding to the level grid
 * @param gridY The Y location of the tile corresponding to the level grid
 * @param context The context to draw this tile on
 * @param level The level that the tile is a part of
 */
open class TileLocation(var gridX : Double, var gridY : Double, context : CanvasRenderingContext2D, var level : Level) : Drawable(context){
    override var x = 0.0
    override var y = 0.0
    override var height = 0.0
    override var width = 0.0
    override var color = "white"

    override open fun draw(){
        if (level.tileAt(gridX.toInt(),gridY.toInt()) is NeutralTile) color = "#64B5F6" //blue
        else if (level.tileAt(gridX.toInt(),gridY.toInt()) is ObstacleTile) color = "#212121" //gray
        else if (level.tileAt(gridX.toInt(),gridY.toInt()) is VictoryTile) color = "#FFEB3B" //yellow

        super.draw()
    }
}
package org.chicagoedt.rosetteweb.grid

import org.w3c.dom.CanvasRenderingContext2D
import org.chicagoedt.rosette.tiles.*
import org.chicagoedt.rosette.levels.*
import org.chicagoedt.rosetteweb.canvas.Drawable
import org.chicagoedt.rosetteweb.*

/**
 * The class for storing the boundaries of tiles so they don't have to be recalculated often
 * @param gridX The X location of the tile corresponding to the level grid
 * @param gridY The Y location of the tile corresponding to the level grid
 * @param context The context to draw this tile on
 * @param level The level that the tile is a part of
 */
open class TileLocation(var gridX : Double, var gridY : Double, context : CanvasRenderingContext2D, var level : Level) : Drawable(context){

    override fun beforeDraw(){
        if (level.tileAt(gridX.toInt(),gridY.toInt()) is NeutralTile) color = COLOR_TILE_NEUTRAL //blue
        else if (level.tileAt(gridX.toInt(),gridY.toInt()) is ObstacleTile) color = COLOR_TILE_OBSTACLE //gray
        else if (level.tileAt(gridX.toInt(),gridY.toInt()) is VictoryTile) color = COLOR_TILE_VICTORY //yellow
    }
}
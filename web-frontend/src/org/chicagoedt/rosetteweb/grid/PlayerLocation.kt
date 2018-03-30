package org.chicagoedt.rosetteweb.grid

import org.w3c.dom.CanvasRenderingContext2D
import org.chicagoedt.rosetteweb.canvas.*
import org.chicagoedt.rosette.robots.*
import org.chicagoedt.rosetteweb.*

/**
 * @param player The player contained in this location
 * @property gridX The X location of the tile corresponding to the level grid
 * @property gridY The Y location of the tile corresponding to the level grid
 */
class PlayerLocation(context : CanvasRenderingContext2D,
                        val player : RobotPlayer) : Drawable(context){
    override var text = player.name
    override var color = COLOR_TILE_PLAYER
    override var textSize = 20

    val gridX get() = player.x.toDouble()
    val gridY get() = player.y.toDouble()
}
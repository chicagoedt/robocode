package org.chicagoedt.rosetteweb.grid

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import kotlin.math.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.tiles.*
import org.chicagoedt.rosetteweb.grid.*
import org.chicagoedt.rosetteweb.canvas.*
import org.chicagoedt.rosette.levels.*
import org.chicagoedt.rosette.robots.*

/**
 * @param gridX The X location of the tile corresponding to the level grid
 * @param gridY The Y location of the tile corresponding to the level grid
 * @param context The context to draw this player on
 * @param player The player contained in this location
 */
class PlayerLocation(   var gridX : Double, 
                        var gridY : Double, 
                        context : CanvasRenderingContext2D, 
                        val player : RobotPlayer) : Drawable(context){
    override var text = player.name
    override var color = "#F44336"
    override var x = 0.0
    override var y = 0.0
    override var height = 0.0
    override var width = 0.0
    override var textSize = 10

    /**
     * Draw the indicator of which direction it's facing. This will likely be removing before release
     */
	fun drawDirection(){
		val radius = 5.0
		context.beginPath()
		context.fillStyle = "black"
        if (player.direction == RobotOrientation.DIRECTION_UP){
        	context.arc(x + (width / 2.0), y, radius, 0.0, 2*PI);
        }
        else if (player.direction == RobotOrientation.DIRECTION_DOWN){
        	context.arc(x + (width / 2.0), y + height, radius, 0.0, 2*PI);
        }

        else if (player.direction == RobotOrientation.DIRECTION_RIGHT){
        	context.arc(x + width, y + (height / 2.0), radius, 0.0, 2*PI);
        }
        else if (player.direction == RobotOrientation.DIRECTION_RIGHT){
        	context.arc(x, y + (height / 2.0), radius, 0.0, 2*PI);
        }
        context.stroke();
        context.closePath()
	}

    override fun draw(){
        super.draw()

        drawDirection()
    }
}
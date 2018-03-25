package org.chicagoedt.rosetteweb.grid

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import kotlin.math.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Tiles.*
import org.chicagoedt.rosetteweb.grid.*
import org.chicagoedt.rosette.Levels.*
import org.chicagoedt.rosette.Robots.*

/**
 * @param player The player contained in this location
 */
class PlayerLocation(   gridX : Double, 
                        gridY : Double, 
                        context : CanvasRenderingContext2D, 
                        level : Level,
                        val player : RobotPlayer) : TileLocation(gridX, gridY, context, level){

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
        context.fillStyle = "#F44336" //red
        context.fillRect(x, y, width, height)

        drawDirection()

        context.fillStyle = "black"
        context.font = "10px Arial";
        context.fillText(player.name, x, y + 10.0);
    }
}
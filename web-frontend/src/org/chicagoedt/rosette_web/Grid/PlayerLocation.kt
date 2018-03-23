package org.chicagoedt.rosette_web.Grid

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Tiles.*
import org.chicagoedt.rosette_web.Grid.*
import org.chicagoedt.rosette.Levels.*
import org.chicagoedt.rosette.Robots.*

/**
 * @param player The player contained in this location
 */
class PlayerLocation(   x : Double, 
                        y : Double, 
                        context : CanvasRenderingContext2D, 
                        level : Level,
                        val player : RobotPlayer) : TileLocation(x, y, context, level){
    override fun draw(){
        context.fillStyle = "#F44336" //red
        context.fillRect(screenX, screenY, screenWidth, screenHeight)

        context.fillStyle = "black"
        context.font = "10px Arial";
        context.fillText(player.name, screenX, screenY + 10.0); 
    }
}
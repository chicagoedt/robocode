package org.chicagoedt.rosette_web

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.Tiles.*

class GridDriver(val game: Game, val context: CanvasRenderingContext2D){

    fun drawGrid(){
        val widthInterval = (context.canvas.width) / (game.currentLevel.properties.width)
        val heightInterval = (context.canvas.height) / (game.currentLevel.properties.height)
        var tileX = 0
        var tileY = 0
        var separation = 10

        for (x in 0..game.currentLevel.properties.width-1){
            for (y in 0..game.currentLevel.properties.height-1){
                if (game.currentLevel.tileAt(x,y) is NeutralTile) context.fillStyle = "blue"
                else if (game.currentLevel.tileAt(x,y) is ObstacleTile) context.fillStyle = "black"
                else if (game.currentLevel.tileAt(x,y) is VictoryTile) context.fillStyle = "yellow"
                context.fillRect((tileX + (separation / 2)).toDouble(), (tileY + (separation / 2)).toDouble(), (widthInterval - (separation / 2)).toDouble(), (heightInterval - (separation / 2)).toDouble())
                tileY += heightInterval
            }
            tileX += widthInterval
            tileY = 0
        }

        for (player in game.currentLevel.playersList){
            val playerX = player.x * widthInterval
            val playerY = player.y * heightInterval

            context.fillStyle = "red"
            context.fillRect((playerX + (separation / 2)).toDouble(), (playerY + (separation / 2)).toDouble(), (widthInterval - (separation / 2)).toDouble(), (heightInterval - (separation / 2)).toDouble())
        }
    }
}
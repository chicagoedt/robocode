package org.chicagoedt.robocodeweb.grid

import jQuery
import org.chicagoedt.robocode.robots.RobotOrientation
import org.chicagoedt.robocode.robots.RobotPlayer
import org.chicagoedt.robocodeweb.game
import org.w3c.dom.HTMLImageElement
import kotlin.browser.document
import kotlin.dom.addClass

/**
 * A screen element representing a player
 * @param player The player that this object represents
 * @param grid The grid that this player is on
 * @param x The starting X value of this player
 * @param y The starting Y value of this player
 */
class PlayerTile(var player : RobotPlayer, val grid : ArrayList<ArrayList<GridTile>>) {
    val element = document.createElement("img") as HTMLImageElement
    private var currentX = player.x
    private var currentY = player.y

    init{
        element.addClass("gridPlayer")
        element.style.display = "block"
        element.src = getPlayerImage(game.robots[player.name]!!.graphic)

        document.getElementById("grid")!!.appendChild(element)

        this.refresh()
    }

    /**
     * Refreshes the position of this player on the screen
     */
    fun refresh(){
        val position = jQuery(grid[player.y][player.x].tableElement).position()

        var changed = false
        if (player.x != currentX || player.y != currentY) changed = true

        if (changed){
            this.currentX = player.x
            this.currentY = player.y

            val properties : dynamic = {}
            properties.top = position.top
            properties.left = position.left

            jQuery(element).animate(properties, 100)
        }
        else{
            jQuery(element).css("top", position.top)
            jQuery(element).css("left", position.left)
        }
    }

    /**
     * Sets the width that this player should display at
     * @param width The new width to add, in px
     */
    fun setWidth(width : Int){
        element.style.width = width.toString() + "px"
        element.style.height = width.toString() + "px"
        element.style.fontSize = (width - 5).toString() + "px"
    }

    /**
     * Gets an image with the correct direction from a path
     * @param path The folder containing left.png, right.png, up.png, down.png
     * @return The path to the correct image
     */
    fun getPlayerImage(path : String) : String{
        if (player.direction == RobotOrientation.DIRECTION_UP) return path + "up.png"
        else if (player.direction == RobotOrientation.DIRECTION_DOWN) return path + "down.png"
        else if (player.direction == RobotOrientation.DIRECTION_RIGHT) return path + "right.png"
        else return path + "left.png"
    }

}
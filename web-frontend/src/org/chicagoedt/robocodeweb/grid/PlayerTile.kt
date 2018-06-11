package org.chicagoedt.robocodeweb.grid

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
class PlayerTile(var player : RobotPlayer, val grid : ArrayList<ArrayList<GridTile>>, var x : Int, var y : Int) {
    val element = document.createElement("img") as HTMLImageElement

    init{
        element.addClass("playerElement")
        element.style.display = "block"
        element.src = getPlayerImage(game.robots[player.name]!!.graphic)

        document.getElementById("grid")!!.appendChild(element)
    }

    /**
     * Moves the player to a new location
     * @param x The new X location for the player
     * @param y The new Y location for the player
     */
    fun moveTo(x : Int, y : Int){
        //TODO Make an animation to move players
        this.x = x
        this.y = y
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
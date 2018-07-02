package org.chicagoedt.robocodeweb.grid

import jQuery
import org.chicagoedt.robocode.robots.RobotOrientation
import org.chicagoedt.robocode.robots.RobotPlayer
import org.chicagoedt.robocodeweb.game
import org.chicagoedt.robocodeweb.sensorconfig.SensorConfigurator
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.addClass

/**
 * A screen element representing a player
 * @param player The player that this object represents
 * @param grid The grid that this player is on
 * @param x The starting X value of this player
 * @param y The starting Y value of this player
 */
class PlayerTile(var player : RobotPlayer, val grid : ArrayList<ArrayList<GridTile>>) {
    val element = document.createElement("div") as HTMLDivElement
    val imageElement = document.createElement("img") as HTMLImageElement
    val sensorConfigurator = SensorConfigurator(this)
    private var currentX = player.x
    private var currentY = player.y
    private var currentDirection = RobotOrientation.DIRECTION_UP

    init{
        element.addClass("gridPlayer")
        imageElement.addClass("gridPlayerImage")
        imageElement.style.display = "block"
        imageElement.src = game.robots[player.name]!!.graphic
        sensorConfigurator.imageElement.src = imageElement.src

        element.appendChild(imageElement)

        document.getElementById("grid")!!.appendChild(element)

        this.refresh()
    }

    /**
     * Refreshes the position of this player on the screen
     */
    fun refresh(){
        var positionChanged = false
        if (player.x != currentX || player.y != currentY) positionChanged = true

        var directionChanged = false
        if (player.direction != currentDirection) directionChanged = true

        if (positionChanged){
            animateToNewPosition()
        }
        else{
            val position = jQuery(grid[player.y][player.x].tableElement).position()
            jQuery(element).css("top", position.top)
            jQuery(element).css("left", position.left)
        }

        if (directionChanged){
            changeDirection()
        }

        sensorConfigurator.element.style.top = element.getBoundingClientRect().bottom.toString() + "px"
        sensorConfigurator.element.style.left = element.getBoundingClientRect().left.toString() + "px"
        val areaToBottom = window.innerHeight - element.getBoundingClientRect().bottom
        sensorConfigurator.element.style.maxHeight = (areaToBottom - 5.0).toString() + "px"

    }

    /**
     * Sets the direction displayed to the direction that the robot is facing
     */
    fun changeDirection(){
        val direction = player.direction
        if (direction == RobotOrientation.DIRECTION_RIGHT) jQuery(imageElement).css("transform", "rotate(90deg)")
        else if (direction == RobotOrientation.DIRECTION_DOWN) jQuery(imageElement).css("transform", "rotate(180deg)")
        else if (direction == RobotOrientation.DIRECTION_LEFT) jQuery(imageElement).css("transform", "rotate(270deg)")
        else if (direction == RobotOrientation.DIRECTION_UP) jQuery(imageElement).css("transform", "rotate(0deg)")

        currentDirection = direction
    }

    /**
     * Moves the player to the new position with an animation
     */
    private fun animateToNewPosition(){
        val position = jQuery(grid[player.y][player.x].tableElement).position()
        this.currentX = player.x
        this.currentY = player.y

        val properties : dynamic = {}
        properties.top = position.top
        properties.left = position.left

        jQuery(element).animate(properties, 100)
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

}
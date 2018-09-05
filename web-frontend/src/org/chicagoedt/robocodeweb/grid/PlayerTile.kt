package org.chicagoedt.robocodeweb.grid

import jQuery
import org.chicagoedt.robocode.robots.RobotOrientation
import org.chicagoedt.robocode.robots.RobotPlayer
import org.chicagoedt.robocodeweb.currentLevelConditions
import org.chicagoedt.robocodeweb.game
import org.chicagoedt.robocodeweb.sensorconfig.SensorConfigurator
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.addClass
import kotlin.dom.removeClass

/**
 * A screen element representing a player
 * @param player The player that this object represents
 * @param grid The grid that this player is on
 * @property element The main HTML element for this player
 * @property imageElement The HTML element showing the image for this player
 * @property sensorConfigurator The sensor configurator attached to this player
 * @property currentX The X value of this player
 * @property currentY The starting Y value of this player
 * @property currentDirection The current direction that this player is facing
 * @property startMovingListener The listener that sets the necessary properties on this robot to start when any robot is moving
 * @property stopMovingListener The listener that sets the necessary properties on this robot to stop when any robot is moving
 */
class PlayerTile(var player : RobotPlayer, val grid : ArrayList<ArrayList<GridTile>>) {
    val element = document.createElement("div") as HTMLDivElement
    val imageElement = document.createElement("img") as HTMLImageElement
    val sensorConfigurator = SensorConfigurator()
    private var currentX = player.x
    private var currentY = player.y
    private var currentDirection = RobotOrientation.DIRECTION_UP
    private var startMovingListener : (org.chicagoedt.robocode.Event) -> Unit = {
        if (it == org.chicagoedt.robocode.Event.ROBOT_RUN_START){
            if (currentLevelConditions.useSensors){
                val shown = sensorConfigurator.shown
                if (shown){
                    sensorConfigurator.toggleShowHide()
                }
                element.addClass("gridPlayerNoClick")
            }
        }
    }
    private var stopMovingListener : (org.chicagoedt.robocode.Event) -> Unit = {
        if (it == org.chicagoedt.robocode.Event.ROBOT_RUN_END){
            if (currentLevelConditions.useSensors){
                element.removeClass("gridPlayerNoClick")
            }
        }
    }

    init{
        element.addClass("gridPlayer")
        imageElement.addClass("gridPlayerImage")
        imageElement.style.display = "block"
        imageElement.src = game.robots[player.name]!!.graphic

        if (currentLevelConditions.useSensors){
            sensorConfigurator.imageElement.src = imageElement.src
            sensorConfigurator.attachTo(this)
        }
        else{
            element.addClass("gridPlayerNoClick")
        }

        element.appendChild(imageElement)

        document.getElementById("grid")!!.appendChild(element)

        setStartMovingListener()
        setStopMovingListener()

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
            animateToNewPosition({
                if (directionChanged){
                    changeDirection()
                }
            })
        }
        else{
            val position = jQuery(grid[player.y][player.x].tableElement).position()
            jQuery(element).css("top", position.top)
            jQuery(element).css("left", position.left)
            refreshSensorConfigPosition()

            if (directionChanged){
                changeDirection()
            }
        }

        if (element.getAttribute("gridX") != null){
            element.setAttribute("previousGridX", element.getAttribute("gridX")!!)
            element.setAttribute("previousGridY", element.getAttribute("gridY")!!)
            element.setAttribute("previousDirection", element.getAttribute("direction")!!)
        }

        element.setAttribute("gridX", player.x.toString())
        element.setAttribute("gridY", player.y.toString())
        val elementString : String
        when(player.direction){
            RobotOrientation.DIRECTION_UP -> elementString = "up"
            RobotOrientation.DIRECTION_DOWN -> elementString = "down"
            RobotOrientation.DIRECTION_LEFT -> elementString = "left"
            RobotOrientation.DIRECTION_RIGHT -> elementString = "right"
            else -> elementString = "none"
        }
        element.setAttribute("direction", elementString)


    }

    /**
     * Removes this player from the grid
     */
    fun remove(){
        removeStartMovingListener()
        removeStopMovingListener()
        element.parentElement!!.removeChild(element)
        imageElement.parentElement!!.removeChild(imageElement)
        sensorConfigurator.element.parentElement!!.removeChild(sensorConfigurator.element)
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
    private fun animateToNewPosition(callback : () -> Unit){
        val position = jQuery(grid[player.y][player.x].tableElement).position()
        this.currentX = player.x
        this.currentY = player.y

        val properties : dynamic = {}
        properties.top = position.top
        properties.left = position.left

        jQuery(element).animate(properties, 100, {
            refreshSensorConfigPosition()
            callback()
        })
    }

    /**
     * Sets the position of the sensor configurator relative to the position of this player
     */
    private fun refreshSensorConfigPosition(){
        sensorConfigurator.element.style.top = element.getBoundingClientRect().bottom.toString() + "px"
        sensorConfigurator.element.style.left = element.getBoundingClientRect().left.toString() + "px"
        val areaToBottom = window.innerHeight - element.getBoundingClientRect().bottom
        sensorConfigurator.element.style.maxHeight = (areaToBottom - 5.0).toString() + "px"
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
     * Sets the sensor configurator to minimize when any robot is run
     */
    private fun setStartMovingListener(){
        game.attachEventListener(startMovingListener)
    }

    /**
     * Removes the listener set in [setStartMovingListener]
     */
    private fun removeStartMovingListener(){
        game.removeEventListener(startMovingListener)
    }

    /**
     * Sets the sensor configurator to minimize when any robot is run
     */
    private fun setStopMovingListener(){
        game.attachEventListener(stopMovingListener)
    }

    /**
     * Removes the listener set in [setWhileMovingListener]
     */
    private fun removeStopMovingListener(){
        game.removeEventListener(stopMovingListener)
    }

}
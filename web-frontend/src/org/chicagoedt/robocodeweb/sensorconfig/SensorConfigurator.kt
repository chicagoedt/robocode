package org.chicagoedt.robocodeweb.sensorconfig

import jQuery
import org.chicagoedt.robocode.robots.RobotPosition
import org.chicagoedt.robocodeweb.grid.PlayerTile
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.addClass

/**
 * The window that allows the user to configure the sensors on a robot
 * @param playerTile The player that this configurator manages
 * @property element The main element of the configurator
 * @property drawer The drawer element in this configurator
 * @property initialHeight The height that [element] returns to after shrinking
 * @property initialWidth The width that [element] returns to after shrinking
 * @property backSensorPanel The sensor back to insert sensors in the back position
 * @property frontSensorPanel The sensor back to insert sensors in the front position
 * @property leftSensorPanel The sensor back to insert sensors in the left position
 * @property rightSensorPanel The sensor back to insert sensors in the right position
 */
class SensorConfigurator (val playerTile : PlayerTile){
    val element = document.createElement("div") as HTMLElement
    val drawer = SensorDrawer()
    var initialHeight = "0px"
    var initialWidth = "0px"

    val backSensorPanel = SensorPanel(RobotPosition.BACK, playerTile, drawer)
    val frontSensorPanel = SensorPanel(RobotPosition.FRONT, playerTile, drawer)
    val leftSensorPanel = SensorPanel(RobotPosition.LEFT, playerTile, drawer)
    val rightSensorPanel = SensorPanel(RobotPosition.RIGHT, playerTile, drawer)


    init{
        document.body!!.appendChild(element)
        element.addClass("sensorConfigurator")
        setHeightAndWidth()

        jQuery(element).hide()
        element.style.height = "0"
        element.style.width = "0"

        playerTile.imageElement.onclick = {toggleShowHide()}

        element.appendChild(backSensorPanel.element)
        element.appendChild(frontSensorPanel.element)
        element.appendChild(leftSensorPanel.element)
        element.appendChild(rightSensorPanel.element)

        element.appendChild(drawer.element)

        drawer.populate()
    }

    /**
     * If the configurator is shown, this method hides it. If it's hidden, this shows it
     */
    fun toggleShowHide(){
        val shown = jQuery(element).`is`(":visible")
        if (shown){
            val properties : dynamic = {}
            properties.height = "0"
            properties.width = "0"

            jQuery(element).animate(properties, 150, "swing", {
                jQuery(element).hide()
            })
        }
        else {
            jQuery(element).show()

            val properties : dynamic = {}
            properties.height = initialHeight
            properties.width = initialWidth

            jQuery(element).animate(properties, 150)
        }
    }

    /**
     * Sets the height and width to reset to after being hidden
     */
    private fun setHeightAndWidth(){
        val heightString = jQuery(element).css("height")
        val height = heightString.substring(0, heightString.length - 2).toDouble()
        val widthString = jQuery(element).css("width")
        val width = widthString.substring(0, widthString.length - 2).toDouble()

        val parentHeight = window.innerHeight
        val parentWidth = window.innerWidth

        initialHeight = ((height / parentHeight) * 100.0).toString() + "%"
        initialWidth = ((width / parentWidth) * 100.0).toString() + "%"
    }
}
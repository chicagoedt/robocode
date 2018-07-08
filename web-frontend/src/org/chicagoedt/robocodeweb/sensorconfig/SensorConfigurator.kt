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
 * @property imageElement The image in the middle of the configurator
 * @property drawer The drawer element in this configurator
 * @property initialHeight The height that [element] returns to after shrinking
 * @property initialWidth The width that [element] returns to after shrinking
 **/
class SensorConfigurator (){
    val element = document.createElement("div") as HTMLElement
    val imageElement = document.createElement("img") as HTMLImageElement
    val drawer = SensorDrawer()
    var initialHeight = "0px"
    var initialWidth = "0px"

    lateinit var backSensorPanel : SensorPanel
    lateinit var frontSensorPanel : SensorPanel
    lateinit var leftSensorPanel : SensorPanel
    lateinit var rightSensorPanel : SensorPanel


    init{
        imageElement.addClass("sensorConfigImage")

        element.appendChild(imageElement  )

        document.body!!.appendChild(element)
        element.addClass("sensorConfigurator")
        setHeightAndWidth()

        jQuery(element).hide()
        element.style.height = "0"
        element.style.width = "0"
    }

    /**
     * Attaches this sensor configurator to a PlayerTile
     * @param playerTile The PlayerTile to attach to
     */
    fun attachTo(playerTile: PlayerTile){
        backSensorPanel = SensorPanel(RobotPosition.BACK, playerTile, drawer)
        frontSensorPanel = SensorPanel(RobotPosition.FRONT, playerTile, drawer)
        leftSensorPanel = SensorPanel(RobotPosition.LEFT, playerTile, drawer)
        rightSensorPanel = SensorPanel(RobotPosition.RIGHT, playerTile, drawer)

        playerTile.imageElement.onclick = {toggleShowHide()}
        playerTile.element.style.boxShadow = "0px 0px 40px grey"
        playerTile.imageElement.style.cursor = "pointer"

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

            jQuery(imageElement).hide()

            jQuery(element).animate(properties, 150, "swing", {
                jQuery(element).hide()
            })
        }
        else {
            jQuery(element).show()

            val properties : dynamic = {}
            properties.height = initialHeight
            properties.width = initialWidth

            jQuery(element).animate(properties, 150, "swing", {
                jQuery(imageElement).show()
                resizeImage()
            })
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

    /**
     * Resizes the image in the center to always be square
     */
    fun resizeImage(){
        imageElement.style.width = ""
        imageElement.style.height = ""
        imageElement.style.top = ""
        imageElement.style.left = ""

        val top = imageElement.getBoundingClientRect().top - element.getBoundingClientRect().top
        val left = imageElement.getBoundingClientRect().left - element.getBoundingClientRect().left

        val width = imageElement.clientWidth
        val height = imageElement.clientHeight

        if (height < width){
            imageElement.style.width = height.toString() + "px"
            imageElement.style.left = (left + ((width - height) / 2)).toString() + "px"
        }
        else{
            imageElement.style.height = width.toString() + "px"
            imageElement.style.top = (top + ((height - width) / 2)).toString() + "px"
        }
    }
}
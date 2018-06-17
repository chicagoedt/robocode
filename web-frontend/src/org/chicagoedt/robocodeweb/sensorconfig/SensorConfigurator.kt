package org.chicagoedt.robocodeweb.sensorconfig

import jQuery
import org.chicagoedt.robocodeweb.grid.PlayerTile
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.addClass

class SensorConfigurator (val playerTile : PlayerTile){
    val element = document.createElement("div") as HTMLElement
    val drawer = SensorDrawer()
    var initialHeight = "0px"
    var initialWidth = "0px"


    init{
        document.body!!.appendChild(element)
        element.addClass("sensorConfigurator")
        setHeightAndWidth()

        jQuery(element).hide()
        element.style.height = "0"
        element.style.width = "0"

        playerTile.imageElement.onclick = {toggleShowHide()}

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
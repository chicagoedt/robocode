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
    }

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

    fun setHeightAndWidth(){
        val heightString = jQuery(element).css("height")
        val height = heightString.substring(0, heightString.length - 2).toDouble()
        val widthString = jQuery(element).css("width")
        val width = widthString.substring(0, widthString.length - 2).toDouble()

        println("child height is $height, width is $width")

        val parentHeight = window.innerHeight
        val parentWidth = window.innerWidth

        println("parent height is $parentHeight, width is $parentWidth")

        initialHeight = ((height / parentHeight) * 100.0).toString() + "%"
        initialWidth = ((width / parentWidth) * 100.0).toString() + "%"

        println("initial height is $initialHeight, width is $initialWidth")
    }
}
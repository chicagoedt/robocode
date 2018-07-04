package org.chicagoedt.robocodeweb.sensorconfig

import jQuery
import JQueryEventObject
import org.chicagoedt.robocodeweb.editor.ActionBlock
import org.chicagoedt.robocodeweb.sensorconfig.sensorblocks.DistanceSensorBlock
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.dom.addClass

/**
 * The drawer that sensors can be dragged from
 * @property element The main element of the drawer
 */
class SensorDrawer {
    val element = document.createElement("div") as HTMLElement
    var numDistanceSensorBlocks = 1

    init{
        element.addClass("sensorDrawer")
    }

    /**
     * Checks to make sure the drawer population is updates
     */
    fun populate() {
        checkDistanceSensorBlock(0)
    }

    //there is a more elegant way to check these blocks, but kotlinJS doesn't support the necessary reflection methods yet
    fun checkDistanceSensorBlock(index: Int) {
        if (element.children.length <= index ||
                !(element.children.item(index).asDynamic().block is DistanceSensorBlock)) {
            val block = DistanceSensorBlock(numDistanceSensorBlocks, this)
            numDistanceSensorBlocks++
            block.addDraggable()

            try {
                element.insertBefore(block.element, element.children.item(index))
            } catch (e: Exception) {
                element.appendChild(block.element)
            }
        }
    }
}
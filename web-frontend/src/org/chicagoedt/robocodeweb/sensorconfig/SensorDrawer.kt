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
        setDroppable()
    }

    /**
     * Sets the droppable properties for this drawer
     */
    fun setDroppable() {
        val drag = jQuery(element).asDynamic()
        drag.droppable()
        drag.droppable("option", "tolerance", "pointer")
        drag.droppable("option", "scope", "sensors")
        drag.droppable("option", "drop", ::drop)
    }

    /**
     * Called when a draggable is dropped over this drawer
     * @param event The Jquery event corresponding to the drop
     * @param ui The element being dropped
     */
    fun drop(event: JQueryEventObject, ui: dynamic) {
        val uiElement = ui.draggable[0] as HTMLElement
        uiElement.parentElement!!.removeChild(uiElement)
        val block : SensorBlock<*> = uiElement.asDynamic().block

        if (block.sensorPanel != null){
            block.sensor.player!!.removeSensorFrom(block.sensor.sensorPosition!!, block.sensor)
        }

        populate()
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
            val block = DistanceSensorBlock(numDistanceSensorBlocks)
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
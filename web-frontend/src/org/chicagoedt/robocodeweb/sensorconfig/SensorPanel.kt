package org.chicagoedt.robocodeweb.sensorconfig

import jQuery
import org.chicagoedt.robocode.robots.RobotPosition
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.addClass

class SensorPanel (val position : RobotPosition, val drawer : SensorDrawer){
    val element = document.createElement("div") as HTMLElement

    init{
        element.addClass("sensorList")

        when(position){
            RobotPosition.FRONT -> element.addClass("frontSensorList")
            RobotPosition.BACK -> element.addClass("backSensorList")
            RobotPosition.LEFT -> element.addClass("leftSensorList")
            RobotPosition.RIGHT -> element.addClass("rightSensorList")
        }

        if (position == RobotPosition.LEFT || position == RobotPosition.RIGHT){
            element.addClass("sideSensorList")
        }

        addDroppable()
    }

    private fun addDroppable(){
        val drop = jQuery(element).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "scope", "sensors")
        drop.droppable("option", "drop", ::onDrop)
        drop.droppable("option", "over", ::onOver)
        drop.droppable("option", "out", ::onOverOut)
    }

    /**
     * Called when a sensor is dropped into this panel
     * @param event The over event
     * @param ui The element being hovered
     */
    private fun onDrop(event : Event, ui : dynamic){
        val blockElement : HTMLElement = ui.draggable[0]
        element.appendChild(blockElement)
        drawer.populate()
        element.style.boxShadow = ""
    }

    /**
     * Called when a sensor is hovered over this panel
     * @param event The over event
     * @param ui The element being hovered
     */
    private fun onOver(event : Event, ui : dynamic){
        element.style.boxShadow = "0px 0px 10px grey"
    }

    /**
     * Called when a sensor is hovered out from over this panel
     * @param event The over event
     * @param ui The element being hovered
     */
    private fun onOverOut(event : Event, ui : dynamic){
        element.style.boxShadow = ""
    }
}
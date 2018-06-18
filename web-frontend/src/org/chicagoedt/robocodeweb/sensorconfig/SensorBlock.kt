package org.chicagoedt.robocodeweb.sensorconfig

import jQuery
import org.chicagoedt.robocode.sensors.Sensor
import org.chicagoedt.robocodeweb.editor.ActionBlockMacro
import org.chicagoedt.robocodeweb.editor.Panel
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.addClass
import kotlin.dom.removeClass

abstract class SensorBlock<T : Sensor> {
    abstract val sensor : T
    val element = document.createElement("div") as HTMLElement
    var blockClass = ""
        set(value) {
            element.removeClass(field)
            field = value
            element.addClass(value)
        }

    init{
        element.asDynamic().block = this
        element.addClass("sensorBlock")
    }

    fun addDraggable(){
        val drag = jQuery(element).asDynamic()
        drag.draggable()
        drag.draggable("option", "helper", "clone")
        drag.draggable("option", "scope", "sensors")
        drag.draggable("option", "appendTo", "body")
        drag.draggable("option", "zIndex", 499)
        drag.draggable("option", "opacity", 0.8)
        drag.on("dragstart", ::onDrag)
        drag.on("dragstop", ::onDragStop)

        element.appendChild(document.createTextNode(sensor.name))
    }

    /**
     * Called when drag starts
     * @param event The event which started the drag
     * @param ui The ui being dragged
     */
    fun onDrag(event : Event, ui : dynamic){
        element.style.backgroundColor = "grey"
        ui.helper[0].style.width = element.clientWidth.toString() + "px"
        ui.helper[0].style.left = ui.position.left.toString() + "px"

        ui.helper[0].style.boxShadow = "0px 0px 50px grey"
    }

    /**
     * Called when drag ends
     * @param event The event which ended the drag
     * @param ui The ui being dragged
     */
    fun onDragStop(event : Event, ui : dynamic){
        element.style.backgroundColor = ""
        element.style.boxShadow = ""
    }
}
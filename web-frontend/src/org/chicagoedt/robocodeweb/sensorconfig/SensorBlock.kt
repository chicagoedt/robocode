package org.chicagoedt.robocodeweb.sensorconfig

import jQuery
import org.chicagoedt.robocode.sensors.Sensor
import org.chicagoedt.robocodeweb.editor.ActionBlockMacro
import org.chicagoedt.robocodeweb.editor.Drawer
import org.chicagoedt.robocodeweb.editor.Panel
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.addClass
import kotlin.dom.removeClass

/**
 * A block, representing a sensor, that can be dragged and dropped into various places
 * @property sensor The sensor that this block represents
 * @property element The element of this block being dragged
 * @property blockClass The class to add to this block (for visual differences between blocks)
 * @property sensorDeleteDrawer The "delete" drawer that shows up when dragging this block
 * @property sensorDeleteFadeTime The time (in ms) for the drawer to fade
 */
abstract class SensorBlock<T : Sensor>(val sensorNum : Int, val drawer : SensorDrawer) {
    abstract val sensor : T
    var sensorPanel : SensorPanel? = null
    var name = ""
    val element = document.createElement("div") as HTMLElement
    var blockClass = ""
        set(value) {
            element.removeClass(field)
            field = value
            element.addClass(value)
        }
    val sensorDeleteDrawer = document.getElementById("sensorDelete") as HTMLElement
    val sensorDeleteFadeTime = 100
    val actionSensorChildren = arrayListOf<HTMLElement>()


    init{
        //@property actionSensor True if this sensor is only to be used among actions, false if it can be used in the sensor window
        element.asDynamic().actionSensor = false
        element.asDynamic().block = this
        element.addClass("sensorBlock")

        element.asDynamic().sensorDeleteDrawer = sensorDeleteDrawer
        element.asDynamic().sensorDeleteFadeTime = sensorDeleteFadeTime
    }

    /**
     * Adds the necessary draggable properties to this block
     */
    fun addDraggable(){
        this.name = sensor.name + " " + sensorNum.toString()
        val drag = jQuery(element).asDynamic()
        drag.draggable()
        drag.draggable("option", "helper", "clone")
        drag.draggable("option", "scope", "sensors")
        drag.draggable("option", "appendTo", "body")
        drag.draggable("option", "zIndex", 499)
        drag.draggable("option", "opacity", 0.8)
        drag.on("dragstart", ::onDrag)
        drag.on("dragstop", ::onDragStop)

        element.appendChild(document.createTextNode(this.name))
    }

    /**
     * Called when drag starts
     * @param event The event which started the drag
     * @param ui The ui being dragged
     */
    fun onDrag(event : Event, ui : dynamic){
        jQuery(sensorDeleteDrawer).fadeIn(sensorDeleteFadeTime)
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
        jQuery(sensorDeleteDrawer).fadeOut(sensorDeleteFadeTime)
        element.style.backgroundColor = ""
        element.style.boxShadow = ""
    }

    /**
     * Removes all action sensor children. To be used when deleting this sensor
     */
    fun removeAllChildren(){
        val drawerElement = document.getElementById("drawer")
        val globalDrawer : Drawer = drawerElement.asDynamic().drawer

        for (child in actionSensorChildren){
            val ui : dynamic = {}
            ui.draggable = arrayListOf<HTMLElement>()
            ui.draggable[0] = child
            globalDrawer.sensorDrop(ui, ui)
        }
    }
}
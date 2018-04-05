package org.chicagoedt.rosetteweb.editor

import jQuery
import org.chicagoedt.rosette.robots.RobotPlayer
import org.chicagoedt.rosetteweb.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.dom.addClass

class Panel(val parent : HTMLElement, val robot : RobotPlayer){
    val element = document.createElement("td") as HTMLElement

    init {
        element.addClass("panel")
        element.id = robot.name + "Panel"
        parent.appendChild(element)
        val drag = jQuery("#" + element.id).asDynamic()
        drag.droppable()
        drag.droppable("option", "tolerance", "pointer")
        drag.droppable("option", "drop", ::drop)
        //drag.droppable("option", "over", ::over)
    }

    //@JsName("drop")
    fun drop(event : Event, ui : dynamic){
        val block : HTMLElement = ui.draggable.context
        block.style.top = "0px"
        block.style.left = "0px"
        block.style.width = "100%"
    	element.appendChild(block)
    }

    fun over(event : Event, ui : dynamic){
        element.style.border = "thick solid #000000"
    }

}
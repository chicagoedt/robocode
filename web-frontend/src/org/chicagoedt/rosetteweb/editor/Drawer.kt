package org.chicagoedt.rosetteweb.editor

import jQuery
import JQueryEventObject
import JQuery
import org.chicagoedt.rosetteweb.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLObjectElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.addClass

class Drawer(val parent : HTMLElement){
    val element = document.getElementById("drawer") as HTMLElement

    init {
        val drag = jQuery("#drawer").asDynamic()
        drag.droppable()
        drag.droppable("option", "tolerance", "pointer")
        drag.droppable("option", "drop", ::drop)
    }

    //@JsName("drop")
    fun drop(event : JQueryEventObject, ui : dynamic){
        ui.draggable.context.parentNode.removeChild(ui.draggable.context)
    }

}
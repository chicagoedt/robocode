package org.chicagoedt.rosetteweb.editor

import jQuery
import org.chicagoedt.rosetteweb.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.addClass

class Panel(val parent : HTMLElement){
    val element = document.createElement("td")

    init {
        element.addClass("panel")
        parent.appendChild(element)
        val drag = jQuery(".panel").asDynamic()
        drag.droppable()
        //js("drag.droppable(\"option\", \"drop\", function(event, ui){drop()})")
        drag.droppable("option", "drop", ::drop)
    }

    //@JsName("drop")
    fun drop(event : Event, ui : Any?){
    	console.log("Hello World!")
    }

}
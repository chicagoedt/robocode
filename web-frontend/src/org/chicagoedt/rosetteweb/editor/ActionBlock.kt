package org.chicagoedt.rosetteweb.editor

import jQuery
import org.chicagoedt.rosetteweb.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.dom.addClass

class ActionBlock(val parent : HTMLElement){
    val element = document.createElement("div")

    init {
        element.addClass("actionBlock")
        parent.appendChild(element)
        val drag = jQuery(".actionBlock").asDynamic()
        drag.draggable()
        drag.draggable("option", "containment", "#editor")
    }

}
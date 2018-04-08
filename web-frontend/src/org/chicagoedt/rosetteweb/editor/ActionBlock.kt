package org.chicagoedt.rosetteweb.editor

import jQuery
import org.chicagoedt.rosetteweb.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.dom.addClass

/**
 * The blocks that the user is dragging around
 * @property element The HTML element that this block corresponds to
 */
class ActionBlock(){
    val element = document.createElement("div")

    init {
        element.addClass("actionBlock")
    }

    /**
     * Adds the necessary options to make this block a draggable. This must be called after the element is appended onto another element
     */
    fun addDraggable(){
    	val drag = jQuery(element).asDynamic()
        drag.draggable()
        drag.draggable("option", "containment", "#editor")
    }

}
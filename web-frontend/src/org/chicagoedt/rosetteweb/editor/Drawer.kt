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

/**
 * The section of the screen where users can select blocks
 * @param parent The parent element for this drawer
 * @property element The HTML element for this drawer
 */
class Drawer(val parent : HTMLElement){
    val element = document.getElementById("drawer") as HTMLElement

    init {
        val drag = jQuery("#drawer").asDynamic()
        drag.droppable()
        drag.droppable("option", "tolerance", "pointer")
        drag.droppable("option", "drop", ::drop)
    }

    /**
     * Called when a draggable is dropped over this drawer
     * @param event The Jquery event corresponding to the drop
     * @param ui The element being dropped
     */
    fun drop(event : JQueryEventObject, ui : dynamic){
        ui.draggable.context.parentNode.removeChild(ui.draggable.context)
        populate()
    }

    /**
     * Checks to make sure the drawer population is updates
     */
    fun populate(){
        if (element.children.length == 0){
            val actionBlock = ActionBlock()
            this.element.appendChild(actionBlock.element)
            actionBlock.addDraggable()
        }
    }

}
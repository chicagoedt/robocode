package org.chicagoedt.rosetteweb.editor

import jQuery
import org.chicagoedt.rosetteweb.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.addClass
import org.chicagoedt.rosette.actions.robotActions.*
import org.chicagoedt.rosette.actions.*

/**
 * The blocks that the user is dragging around
 * @property element The HTML element that this block corresponds to
 * @property action The action 
 */
abstract class ActionBlock<T : Action<*>>(){
    val element = document.createElement("div") as HTMLElement
    abstract val action : T
    private lateinit var originalShadow : String

    init {
        element.addClass("actionBlock")
        element.asDynamic().block = this
        originalShadow = element.style.boxShadow
    }

    /**
     * Adds the necessary options to make this block a draggable. This must be called after the element is appended onto another element
     */
    fun addDraggable(){
    	val drag = jQuery(element).asDynamic()
        drag.draggable()
        drag.draggable("option", "stack", ".actionBlock")
        drag.draggable("option", "helper", "clone")
        drag.draggable("option", "appendTo", "#editor")
        drag.draggable("option", "zIndex", 99)
        drag.draggable("option", "opacity", 0.8)
        drag.on("dragstart", ::onDrag)
        drag.on("dragstop", ::onDragStop)
    }

    /**
     * Generates the name <p> for the block
     * @return The HTMLElement for the name
     */
    fun getName() : HTMLElement{
    	val nameElement = document.createElement("p") as HTMLElement
    	nameElement.addClass("actionName")
    	nameElement.innerHTML = action.name
    	return nameElement
    }

    /**
     * Called when drag starts
     * @param event The event which started the drag
     * @param ui The ui being dragged
     */
    fun onDrag(event : Event, ui : dynamic){
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
        element.style.boxShadow = originalShadow
    }
}
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
        drag.draggable("option", "containment", "#editor")
        drag.draggable("option", "stack", ".actionBlock")
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

    fun onDrag(event : Event, ui : dynamic){
        element.style.boxShadow = "0px 0px 50px grey"
    }

    fun onDragStop(event : Event, ui : dynamic){
        element.style.boxShadow = originalShadow
    }
}
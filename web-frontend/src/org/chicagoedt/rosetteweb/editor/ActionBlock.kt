package org.chicagoedt.rosetteweb.editor

import jQuery
import org.chicagoedt.rosetteweb.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
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
    val element = document.createElement("div")
    abstract val action : T

    init {
        element.addClass("actionBlock")
        element.asDynamic().block = this
    }

    /**
     * Adds the necessary options to make this block a draggable. This must be called after the element is appended onto another element
     */
    fun addDraggable(){
    	val drag = jQuery(element).asDynamic()
        drag.draggable()
        drag.draggable("option", "containment", "#editor")
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
}
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
    val parameterElement = document.createElement("select") as HTMLElement
    abstract val action : T
    protected abstract val hasParameters : Boolean

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
        drag.draggable("option", "stack", ".actionBlock")
        drag.draggable("option", "helper", "clone")
        drag.draggable("option", "appendTo", "#editor")
        drag.draggable("option", "zIndex", 99)
        drag.draggable("option", "opacity", 0.8)
        drag.on("dragstart", ::onDrag)
        drag.on("dragstop", ::onDragStop)
        element.innerHTML = action.name
        if (hasParameters){
            parameterElement.addClass("actionBlockParameter")
            parameterElement.onchange = ::parameterChanged
            element.appendChild(parameterElement)
        }

        drag.droppable()
        drag.droppable("option", "tolerance", "pointer")
        drag.droppable("option", "over", ::over)
        drag.droppable("option", "out", ::overout)
    }

    /**
     * Called when drag starts
     * @param event The event which started the drag
     * @param ui The ui being dragged
     */
    fun onDrag(event : Event, ui : dynamic){
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
        element.style.backgroundColor = ""
        element.style.boxShadow = ""
    }

    /**
     * Inserts a parameter into the parameter list
     * @param s The string representation of the parameter
     * @param onSelectedParameter The value that the parameter should be set to when this option is selected
     */
    fun insertParameter(s : String, onSelectedParameter: dynamic){
        val option = document.createElement("option") as HTMLElement
        option.innerHTML = s
        option.asDynamic().parameter = onSelectedParameter
        option.setAttribute("value", s)
        parameterElement.appendChild(option)
    }

    /**
     * Called when the parameter gets changed
     * @param e The event of the changed parameter
     */
    fun parameterChanged(e : Event) : dynamic{
        action.parameter = e.target.asDynamic().selectedOptions[0].parameter
        return 0
    }

    /**
     * Expands the bottom margin when dragged over. Only applies if the parent is a panel
     * @param event The over event
     * @param ui The element being hovered
     */
    fun over(event : Event, ui : dynamic){
        if (element.parentElement!!.classList.contains("panel")){
            element.parentElement!!.asDynamic().panelObject.lastHoveredBlock = this
            element.style.marginBottom = "10px"
        }
    }

    /**
     * Called when a draggable that was hovering over this block is moved out
     * @param event The out event
     * @param ui The element being moved
     */
    fun overout(event : Event, ui : dynamic){
        element.style.marginBottom = ""
    }
}
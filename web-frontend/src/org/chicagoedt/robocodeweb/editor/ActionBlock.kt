package org.chicagoedt.robocodeweb.editor

import jQuery
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.addClass
import org.chicagoedt.robocode.actions.*
import org.chicagoedt.robocodeweb.sensorconfig.SensorBlock
import org.w3c.dom.HTMLSelectElement
import kotlin.dom.removeClass

/**
 * Types of parameter selectors that a block can take
 * @property NONE The block takes no parameter
 * @property DROPDOWN The block parameter should be selected from a dropdown menu
 * @property NUMBER_INPUT The block parameter should be a number entered in a text box
 */
enum class BlockParameterType{
    NONE,
    DROPDOWN,
    NUMBER_INPUT,
    SENSOR
}

/**
 * The blocks that the user is dragging around
 * @property element The HTML element that this block corresponds to
 * @property parameterElement The HTML element containing the parameter selector for this block
 * @property action The action that this block contains
 * @property parameterType The type of parameter selector that this block uses
 * @property macroParent The macro if a macro is a direct parent of this block, null otherwise
 */
abstract class ActionBlock<T : Action<*>>(){
    val element = document.createElement("div") as HTMLElement
    lateinit var parameterElement : HTMLElement
    abstract val action : T
    protected var parameterType = BlockParameterType.NONE
        set(value) {
            field = value
            addParameterSelector()
        }
    var macroParent : ActionBlockMacro<*>? = null

    var blockClass = ""
        set(value) {
            element.removeClass(field)
            field = value
            element.addClass(value)
        }

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
        drag.draggable("option", "scope", "actions")
        drag.draggable("option", "appendTo", "#editor")
        drag.draggable("option", "zIndex", 99)
        drag.draggable("option", "opacity", 0.8)
        if (this is ActionBlockMacro) drag.draggable("option", "handle", ".macroHeader")
        drag.on("dragstart", ::onDrag)
        drag.on("dragstop", ::onDragStop)

        //add the name of the action to the block
        if (!(this is ActionBlockMacro))element.appendChild(document.createTextNode(action.name))

        if (!(this is ActionBlockMacro)){
            drag.droppable()
            drag.droppable("option", "tolerance", "pointer")
            drag.droppable("option", "scope", "actions")
            drag.droppable("option", "over", ::over)
            drag.droppable("option", "out", ::overout)
        }
    }

    /**
     * Adds the appropriate parameter selector to this block
     */
    fun addParameterSelector(){
        if (parameterType != BlockParameterType.NONE){
            if (parameterType == BlockParameterType.DROPDOWN){
                parameterElement = document.createElement("select") as HTMLSelectElement
            }
            else if (parameterType == BlockParameterType.NUMBER_INPUT){
                parameterElement = document.createElement("input") as HTMLElement
                parameterElement.asDynamic().type = "number"
                parameterElement.asDynamic().value = "1"
                action.parameter = parameterElement.asDynamic().value
            }
            else if (parameterType == BlockParameterType.SENSOR){
                parameterElement = document.createElement("div") as HTMLElement
                parameterElement.addClass("actionSensorDrop")
                addDroppableSensorField()
            }
            parameterElement.addClass("actionBlockParameter")
            element.appendChild(parameterElement)
            parameterElement.onchange = ::parameterChanged
        }
    }

    /**
     * Adds droppable properties to the sensor field for the sensor parameter type
     */
    fun addDroppableSensorField(){
        val dropSensor = { event : Event, ui : dynamic ->
            val sensorElement : HTMLElement = ui.draggable[0]
            val sensorBlock = sensorElement.asDynamic().block as SensorBlock<*>
            action.parameter = sensorBlock.sensor.asDynamic()
            val clone = sensorElement.cloneNode(true) as HTMLElement
            clone.style.backgroundColor = ""
            clone.addClass("sensorBlockInAction")
            parameterElement.appendChild(clone)

        }
        val drop = jQuery(parameterElement).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "scope", "sensors")
        drop.droppable("option", "drop", dropSensor)
    }

    /**
     * Adds droppable properties to the sensor for the sensor parameter type
     */
    fun addDroppableSensor(){

    }

    /**
     * Called when drag starts
     * @param event The event which started the drag
     * @param ui The ui being dragged
     */
    open fun onDrag(event : Event, ui : dynamic){
        cancelDragAllParents(event, ui)
        element.style.backgroundColor = "grey"
        ui.helper[0].style.width = element.clientWidth.toString() + "px"
        ui.helper[0].style.left = ui.position.left.toString() + "px"

        ui.helper[0].style.boxShadow = "0px 0px 50px grey"
        if (element.parentElement!!.classList.contains("panel")){
            (element.parentElement!!.asDynamic().panelObject as Panel).robot.removeAction(this.action)
        }
        else if (macroParent != null){
            this.macroParent!!.action.removeFromMacro(this.action)
        }
    }

    /**
     * Called when drag ends
     * @param event The event which ended the drag
     * @param ui The ui being dragged
     */
    open fun onDragStop(event : Event, ui : dynamic){
        element.style.backgroundColor = ""
        element.style.boxShadow = ""
    }

    /**
     * Inserts a parameter into the parameter list
     * @param s The string representation of the parameter
     * @param onSelectedParameter The value that the parameter should be set to when this option is selected
     */
    fun insertDropdownParameter(s : String, onSelectedParameter: dynamic){
        if (parameterType == BlockParameterType.DROPDOWN){
            val option = document.createElement("option") as HTMLElement
            val firstOption = (parameterElement.childNodes.length == 0)
            option.innerHTML = s
            option.asDynamic().parameter = onSelectedParameter
            option.setAttribute("value", s)
            parameterElement.appendChild(option)
            if (firstOption){
                action.parameter = onSelectedParameter
            }
        }
    }

    /**
     * Called when the parameter gets changed
     * @param e The event of the changed parameter
     */
    fun parameterChanged(e : Event) : dynamic{
        if (parameterType == BlockParameterType.DROPDOWN){
            action.parameter = e.target.asDynamic().selectedOptions[0].parameter
        }
        else if (parameterType == BlockParameterType.NUMBER_INPUT){
            action.parameter = e.target.asDynamic().value
        }
        return 1
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
        else if (macroParent != null){
            macroParent!!.lastHoveredBlock = this as ActionBlock<Action<Any>>
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

    //this has to exist because of a bug in Jquery UI where all draggable parents of a draggable receive a drag event
    fun cancelDragAllParents(event : Event, ui : dynamic){
        ui.draggable = arrayListOf<Any>()

        (ui.draggable as ArrayList<Any>).add(element)
        val element = this.element
        js("ui.draggable = [element]")

        var panel : Panel? = null

        if (this is ActionBlockMacro) panel = this.panelParent

        var currentParent = macroParent
        while (currentParent != null){
            currentParent.macroOver(event, ui)
            currentParent.cancelDrag = true
            panel = currentParent.panelParent
            currentParent = currentParent.macroParent
        }

        if (panel != null) {
            panel.over(event, ui)
        }
    }
}
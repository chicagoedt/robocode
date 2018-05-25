package org.chicagoedt.robocodeweb.editor

import jQuery
import org.chicagoedt.robocode.actions.Action
import org.chicagoedt.robocode.actions.ActionMacro
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.addClass

/**
 * An action block containing other action blocks
 * @param drawer The drawer than this drawer is initialized into
 * @property dropElement The element that, when hovered over, blocks will be dropped into this macro
 */
abstract class ActionBlockMacro<T : ActionMacro<*>>(override var drawer: Drawer) : ActionBlock<T>(), BlockList{
    private val headerElement = document.createElement("div") as HTMLElement
    override var dropElement = document.createElement("div") as HTMLElement
    override var lastHoveredBlock: ActionBlock<*>? = null
    override var firstIndexDrop = false

    init{
        element.addClass("actionBlockMacro")
        dropElement.addClass("dropHelper")
        element.appendChild(dropElement)
        headerElement.addClass("actionBlockMacroHeader")
        element.appendChild(headerElement)
    }

    override fun addAction(action: Action<*>, pos: Int) {
        this.action.addToMacro(action, pos)

        val parent : BlockList = element.parentElement.asDynamic().container
        parent.setDropInto(true)
        jQuery(element).asDynamic().droppable("enable")
    }

    override fun removeAction(action : Action<*>){
        this.action.removeFromMacro(action)
    }

    override fun setDropInto(status : Boolean){
        if (status){
            jQuery(dropElement).asDynamic().droppable("enable")
        }
        else{
            jQuery(dropElement).asDynamic().droppable("disable")
        }
    }

    /**
     * Sets the indicator that this block can be dropped into
     * @param status True if the indicator should show, false if it should hide
     */
    fun setIndicator(status : Boolean){
        if (status) {
            element.style.backgroundColor = "white"
        }
        else {
            element.style.backgroundColor = ""
        }
    }
}
package org.chicagoedt.robocodeweb.editor

import jQuery
import org.chicagoedt.robocode.actions.Action
import org.chicagoedt.robocode.actions.ActionMacro
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.addClass

abstract class ActionBlockMacro<T : ActionMacro<*>> : ActionBlock<T>(), BlockList{
    private val dropHelperElement = document.createElement("div") as HTMLElement
    override var lastHoveredBlock: ActionBlock<*>? = null

    init{
        element.addClass("actionBlockMacro")
        dropHelperElement.addClass("dropHelper")
        element.appendChild(dropHelperElement)
    }

    fun addDropHelperDroppable(){
        val drop = jQuery(dropHelperElement).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "drop", ::helperDrop)
        drop.droppable("option", "over", ::helperOver)
        drop.droppable("option", "out", ::helperOverOut)
    }

    fun helperDrop(event : Event, ui : dynamic){
        element.style.backgroundColor = ""
        val blockElement : HTMLElement = ui.draggable[0]
        action.addToMacro(blockElement.asDynamic().block.action)
        element.appendChild(blockElement)

        val panel : Panel = element.parentElement.asDynamic().panelObject
        jQuery(panel.element as HTMLElement).asDynamic().droppable("enable")
        jQuery(element).asDynamic().droppable("enable")

        panel.drawer.populate()
    }

    fun helperOver(){
        element.style.backgroundColor = "white"

        val panel : Panel = element.parentElement.asDynamic().panelObject
        jQuery(panel.element as HTMLElement).asDynamic().droppable("disable")
        jQuery(element).asDynamic().droppable("disable")
        element.style.marginBottom = ""
    }

    fun helperOverOut(event : Event, ui : dynamic){
        element.style.backgroundColor = ""
        val blockElement : HTMLElement = ui.draggable[0]
        action.removeFromMacro(blockElement.asDynamic().block.action)

        val panel : Panel = element.parentElement.asDynamic().panelObject
        jQuery(panel.element as HTMLElement).asDynamic().droppable("enable")
        jQuery(element).asDynamic().droppable("enable")
        element.style.marginBottom = "10px"
    }
}
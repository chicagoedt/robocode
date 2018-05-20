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
 * @property dropHelperElement The element that, when hovered over, blocks will be dropped into this macro
 */
abstract class ActionBlockMacro<T : ActionMacro<*>>(override var drawer: Drawer) : ActionBlock<T>(), BlockList{
    private val dropHelperElement = document.createElement("div") as HTMLElement
    private val headerElement = document.createElement("div") as HTMLElement
    override var lastHoveredBlock: ActionBlock<*>? = null
    override var firstIndexDrop = false

    init{
        element.addClass("actionBlockMacro")
        dropHelperElement.addClass("dropHelper")
        element.appendChild(dropHelperElement)
        headerElement.addClass("actionBlockMacroHeader")
        element.appendChild(headerElement)
    }

    /**
     * Adds droppable properties to [dropHelperElement]
     */
    fun addDropHelperDroppable(){
        val drop = jQuery(dropHelperElement).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "drop", ::dropInList)
        drop.droppable("option", "over", ::helperOver)
        drop.droppable("option", "out", ::helperOverOut)

        addHeaderDroppable(headerElement)
    }

    /**
     * Called when a draggable is dragged over [dropHelperElement]
     * @param event The over event
     * @param ui The element being hovered
     */
    fun helperOver(event : Event, ui : dynamic){
        val action = ui.draggable[0].block.action
        this.action.removeFromMacro(action)

        element.style.backgroundColor = "white"

        val panel : Panel = element.parentElement.asDynamic().panelObject
        jQuery(panel.element as HTMLElement).asDynamic().droppable("disable")
        jQuery(element).asDynamic().droppable("disable")
        element.style.marginBottom = ""
    }

    /**
     * Called when a draggable is dragged out from over [dropHelperElement]
     * @param event The out event
     * @param ui The element being hovered
     */
    fun helperOverOut(event : Event, ui : dynamic){
        element.style.backgroundColor = ""

        val panel : Panel = element.parentElement.asDynamic().panelObject
        jQuery(panel.element as HTMLElement).asDynamic().droppable("enable")
        jQuery(element).asDynamic().droppable("enable")
        element.style.marginBottom = "10px"
        lastHoveredBlock = null
    }

    override fun addAction(action: Action<*>, pos: Int) {
        this.action.addToMacro(action, pos)

        val panel : Panel = element.parentElement.asDynamic().panelObject
        jQuery(panel.element as HTMLElement).asDynamic().droppable("enable")
        jQuery(element).asDynamic().droppable("enable")
    }
}
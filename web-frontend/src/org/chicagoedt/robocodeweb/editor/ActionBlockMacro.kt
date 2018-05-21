package org.chicagoedt.robocodeweb.editor

import jQuery
import org.chicagoedt.robocode.actions.Action
import org.chicagoedt.robocode.actions.ActionMacro
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.ItemArrayLike
import org.w3c.dom.asList
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
        element.asDynamic().container = this
    }

    /**
     * Called when a draggable is dragged over [dropHelperElement]
     * @param event The over event
     * @param ui The element being hovered
     */
    fun helperOver(event : Event, ui : dynamic){
        val action = ui.draggable[0].block.action
        this.action.removeFromMacro(action)

        setIndicator(true)

        val parent : BlockList = element.parentElement.asDynamic().container
        parent.setDropInto(false)
        parent.element.style.backgroundColor = ""
        jQuery(element).asDynamic().droppable("disable")
        parent.lastHoveredBlock = null
        element.style.marginBottom = ""
    }

    /**
     * Called when a draggable is dragged out from over [dropHelperElement]
     * @param event The out event
     * @param ui The element being hovered
     */
    fun helperOverOut(event : Event, ui : dynamic){
        setIndicator(false)

        val parent : BlockList = element.parentElement.asDynamic().container
        parent.setDropInto(true)
        if (parent is ActionBlockMacro<*>){
            parent.setIndicator(true)
        }
        jQuery(element).asDynamic().droppable("enable")
        element.style.marginBottom = "10px"
        lastHoveredBlock = null
    }

    override fun addAction(action: Action<*>, pos: Int) {
        this.action.addToMacro(action, pos)

        val parent : BlockList = element.parentElement.asDynamic().container
        parent.setDropInto(true)
        jQuery(element).asDynamic().droppable("enable")
    }

    override fun setDropInto(status : Boolean){
        if (status){
            jQuery(dropHelperElement).asDynamic().droppable("enable")
        }
        else{
            jQuery(dropHelperElement).asDynamic().droppable("disable")
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
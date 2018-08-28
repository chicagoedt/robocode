package org.chicagoedt.robocodeweb.editor

import jQuery
import org.chicagoedt.robocode.actions.Action
import org.chicagoedt.robocode.actions.ActionMacro
import org.chicagoedt.robocode.robots.Robot
import org.chicagoedt.robocode.robots.RobotPlayer
import org.chicagoedt.robocodeweb.showActionBlockLimitPopup
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.ItemArrayLike
import org.w3c.dom.asList
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.addClass
import kotlin.dom.removeClass

/**
 * @param drawer That drawer to repopulate after a drop into this macro
 * @property hoverOverHeader True if the header was last hovered over, false otherwise
 * @property header The header element for this macro
 * @property footer The footer element for this macro
 * @property side The side element for this macro
 * @property panelParent The panel if the panel is a direct parent, null otherwise
 * @property cancelDrag True if drag should be ignored on the next call, false otherwise
 */
abstract class ActionBlockMacro<T : ActionMacro<*>>(val drawer : Drawer) : ActionBlock<T>() {
    private var hoverOverHeader = false
    var lastHoveredBlock : ActionBlock<Action<Any>>? = null
    var header : HTMLElement = document.createElement("div") as HTMLElement
    var footer : HTMLElement = document.createElement("div") as HTMLElement
    var side : HTMLElement = document.createElement("div") as HTMLElement

    var panelParent : Panel? = null

    var borderClass = ""
        set(value) {
            header.removeClass(field)
            footer.removeClass(field)
            side.removeClass(field)
            field = value
            header.addClass(value)
            footer.addClass(value)
            side.addClass(value)
        }

    init{
        element.classList.add("actionBlockMacro")
    }

    fun addHeader(){
        initHeader()
        initFooterAndSide()
        element.appendChild(header)
        element.appendChild(footer)
        element.appendChild(side)
        addDrop()
    }

    /**
     * Generates the header for the macro
     * @return The HTMLElement for the header
     */
    private fun initHeader(){
        header.addClass("macroHeader")

        header.innerHTML = action.name

        addHeaderDroppable(header)
    }

    /**
     * Adds droppable properties to the header. Necessary to determine block drop positions
     * @param header The header element
     */
    fun addHeaderDroppable(header : HTMLElement){
        val onOver = {
            header.style.marginBottom = "10px"
            hoverOverHeader = true
        }

        val onOverOut = {
            header.style.marginBottom = ""
            hoverOverHeader = false
        }

        val onDrop = {
            header.style.marginBottom = ""
        }

        val drop = jQuery(header).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "scope", "actions")
        drop.droppable("option", "drop", onDrop)
        drop.droppable("option", "over", onOver)
        drop.droppable("option", "out", onOverOut)
    }

    /**
     * Generates the header for the macro
     * @return The HTMLElement for the header
     */
    private fun initFooterAndSide(){
        footer.addClass("macroFooter")
        side.addClass("macroSide")

        addFooterAndSideDroppable(footer)
    }

    /**
     * Adds droppable properties to the footer. Necessary to drop directly after this macro
     * @param header The footer element
     */
    fun addFooterAndSideDroppable(footer : HTMLElement){
        val onFooterOver = ::over

        val onFooterOverOut = ::overout

        val onFooterDrop = { event : Event, ui : dynamic ->
            if (panelParent != null){
                panelParent!!.drop(event, ui)
            }
            else if (macroParent != null){
                macroParent!!.macroDrop(event, ui)
            }
        }

        var drop = jQuery(footer).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "scope", "actions")
        drop.droppable("option", "over", onFooterOver)
        drop.droppable("option", "out", onFooterOverOut)
        drop.droppable("option", "drop", onFooterDrop)
        drop.droppable("option", "greedy", true)

        drop = jQuery(side).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "scope", "actions")
        drop.droppable("option", "over", onFooterOver)
        drop.droppable("option", "out", onFooterOverOut)
        drop.droppable("option", "drop", onFooterDrop)
        drop.droppable("option", "greedy", true)
    }

    /**
     * Adds the necessary options for this macro to be a droppable
     */
    fun addDrop(){
        val drop = jQuery(element).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "scope", "actions")
        drop.droppable("option", "drop", ::macroDrop)
        drop.droppable("option", "over", ::macroOver)
        drop.droppable("option", "out", ::macroOverOut)
        drop.droppable("option", "greedy", true)
    }

    /**
     * Called when a draggable is dropped over this macro
     * @param event The drop event
     * @param ui The element being dropped
     */
    fun macroDrop(event : Event, ui : dynamic){
        if (panelParent == null && macroParent == null) return
        val blockElement : HTMLElement = ui.draggable[0]
        blockElement.style.top = "0px"
        blockElement.style.left = "0px"
        element.style.boxShadow = ""

        val newActionBlock : ActionBlock<*> = blockElement.asDynamic().block

        var insertBlockElement = {}
        var removeBlockElement = {}
        var undoRemoveBlockElement = {}

        if (blockElement.parentElement!!.classList.contains("panel")){
            val panel = blockElement.parentElement!!.asDynamic().panelObject as Panel
            val originalPosition = panel.robot.procedure.lastIndexOf(newActionBlock.action)
            removeBlockElement = {panel.robot.removeAction(newActionBlock.action)}
            undoRemoveBlockElement = {panel.robot.insertAction(newActionBlock.action, originalPosition)}
        }
        else if (newActionBlock.macroParent != null){
            val originalPosition = newActionBlock.macroParent!!.action.getMacro().lastIndexOf(newActionBlock.action)
            removeBlockElement = {newActionBlock.macroParent!!.action.removeFromMacro(newActionBlock.action)}
            val oldRobotParent = getRobotParent(newActionBlock.macroParent!!)
            undoRemoveBlockElement = {newActionBlock.macroParent!!.action.addToMacroAt(newActionBlock.action, originalPosition, oldRobotParent.getLimitDifference())}
        }

        var blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
        blocks = trimToDirectChildren(blocks.toMutableList())
        var pos = 0
        if (hoverOverHeader){
            if (blocks.size > 0) insertBlockElement = {element.insertBefore(blockElement, blocks[0])}
            else insertBlockElement = {element.appendChild(blockElement)}
            hoverOverHeader = false
        }
        else{
            try{
                lastHoveredBlock!!.element.style.marginBottom = ""
                pos = blocks.indexOf(lastHoveredBlock!!.element) + 1
                val block = blocks[pos] as HTMLElement
                insertBlockElement = {element.insertBefore(blockElement, block)}
            }
            catch(e : Exception){
                pos = blocks.size
                insertBlockElement = {element.appendChild(blockElement)}
            }
        }

        val robotParent = getRobotParent(this)

        lastHoveredBlock = null

        val newAction : Action<*> = blockElement.asDynamic().block.action
        removeBlockElement()
        val canInsert = action.canAddToMacro(newAction, robotParent.getLimitDifference())
        if (canInsert){
            insertBlockElement()
            action.addToMacroAt(newAction, pos, robotParent.getLimitDifference())
            newActionBlock.macroParent = this
        }
        else {
            showActionBlockLimitPopup()
            undoRemoveBlockElement()
        }

        drawer.populate()
    }

    /**
     * @return The robot that an ActionBlockMacro belongs to
     */
    fun getRobotParent(actionBlockMacro : ActionBlockMacro<*>) : RobotPlayer{
        var robotParent : RobotPlayer? = null
        var currentPanelParent = actionBlockMacro.panelParent
        var currentMacroParent= actionBlockMacro.macroParent
        while (currentPanelParent == null){
            currentPanelParent = currentMacroParent!!.panelParent
            if (currentPanelParent == null) currentMacroParent = currentMacroParent.macroParent
        }
        return currentPanelParent.robot
    }

    /**
     * Called when a draggable is hovered over this macro
     * @param event The over event
     * @param ui The element being hovered
     */
    fun macroOver(event : Event, ui : dynamic){
        element.style.boxShadow = "0px 0px 2px grey"
        element.style.backgroundColor = ""
    }

    /**
     * Called when a draggable that was hovering over this macro is moved out
     * @param event The out event
     * @param ui The element being moved
     */
    fun macroOverOut(event : Event, ui : dynamic){
        element.style.boxShadow = ""
        lastHoveredBlock = null
    }

    /**
     * Trims a list of elements to include only direct children of [element]
     * @param list The list to trim
     * @return A list of elements from [list] which are direct children of [element]
     */
    private fun trimToDirectChildren(list : MutableList<Element>) : List<Element>{
        val toRemove = arrayListOf<Element>()

        for (element in list){
            if (element.parentElement == null || element.parentElement != this.element){
                toRemove.add(element)
            }
        }

        for (element in toRemove){
            list.remove(element)
        }

        return list
    }

    override fun onDrag(event: Event, ui: dynamic) {
        if (cancelDrag){
            cancelDrag = false
        }
        else{
            header.style.backgroundColor = "#616161"
            footer.style.backgroundColor = "#616161"
            side.style.backgroundColor = "#616161"
            super.onDrag(event, ui)
        }
    }

    override fun onDragStop(event: Event, ui: dynamic) {
        header.style.backgroundColor = ""
        footer.style.backgroundColor = ""
        side.style.backgroundColor = ""
        super.onDragStop(event, ui)
    }
}
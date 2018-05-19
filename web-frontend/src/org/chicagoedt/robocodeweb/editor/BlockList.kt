package org.chicagoedt.robocodeweb.editor

import jQuery
import org.chicagoedt.robocode.actions.Action
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.ItemArrayLike
import org.w3c.dom.asList
import org.w3c.dom.events.Event

/**
 * An list containing blocks
 * @property lastHoveredBlock The last block to be hovered over
 * @property element The element to insert the blocks into
 * @property drawer The drawer to repopulate after a drop
 * @property firstIndexDrop True if the block should be placed at the first index in the list, false otherwise
 */
interface BlockList {
    var lastHoveredBlock : ActionBlock<*>?
    var element : HTMLElement
    var drawer : Drawer
    var firstIndexDrop : Boolean

    /**
     * Called when a draggable is dropped over this list
     * @param event The drop event
     * @param ui The element being dropped
     */
    fun dropInList(event : Event, ui : dynamic){
        val blockElement : HTMLElement = ui.draggable[0]
        blockElement.style.top = "0px"
        blockElement.style.left = "0px"
        element.style.boxShadow = ""

        val blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
        var pos = 0
        if (firstIndexDrop){
            if (blocks.size > 0) element.insertBefore(blockElement, blocks[0])
            else element.appendChild(blockElement)
            firstIndexDrop = false
        }
        else{
            try{
                pos = blocks.indexOf(lastHoveredBlock!!.element) + 1
                val block = blocks[pos] as HTMLElement
                element.insertBefore(blockElement, block)
            }
            catch(e : Exception){
                pos = blocks.size
                element.appendChild(blockElement)
            }
        }

        (blockElement.asDynamic().block as ActionBlock<*>).parentBlockList = this

        lastHoveredBlock = null

        if (blockElement.asDynamic().block is ActionBlockMacro<*>){
            blockElement.asDynamic().block.addDropHelperDroppable()
        }

        drawer.populate()

        addAction(blockElement.asDynamic().block.action, pos)
    }

    /**
     * Adds droppable properties to the header. Necessary to determine block drop positions
     * @param header The header element
     */
    fun addHeaderDroppable(header : HTMLElement){
        val onOver = {
            val blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
            try{
                (blocks[0] as HTMLElement).style.marginTop = "10px"
            }
            catch (e : Exception){}

            firstIndexDrop = true
        }

        val onOverOut = {
            val blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
            try{
                (blocks[0] as HTMLElement).style.marginTop = ""
            }
            catch (e : Exception){}
            firstIndexDrop = false
        }

        val onDrop = {
            val blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
            try{
                (blocks[1] as HTMLElement).style.marginTop = ""
            }
            catch (e : Exception){}
        }

        val drop = jQuery(header).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "drop", onDrop)
        drop.droppable("option", "over", onOver)
        drop.droppable("option", "out", onOverOut)
    }

    /**
     * Adds an action at a position to the list backend
     * @param action The action to add
     * @param pos The position to add it at
     */
    fun addAction(action : Action<*>, pos : Int)
}
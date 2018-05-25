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
    var dropElement : HTMLElement
    var drawer : Drawer
    var firstIndexDrop : Boolean

    /**
     * Adds the necessary options for this list to be a droppable
     */
    fun addDrop(){
        val drop = jQuery(dropElement).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "drop", ::dropInList)
        drop.droppable("option", "dropOver", ::dropOver)
        drop.droppable("option", "out", ::dropOverOut)
    }

    /**
     * Called when a draggable is dropped over this list
     * @param event The drop event
     * @param ui The element being dropped
     */
    fun dropInList(event : Event, ui : dynamic){
        val blockElement : HTMLElement = ui.draggable[0]

        val block = (blockElement.asDynamic().block as ActionBlock<*>)

        blockElement.style.top = "0px"
        blockElement.style.left = "0px"
        element.style.boxShadow = ""

        var blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList()
        blocks = trimToDirectChildren(blocks.toMutableList())
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

        block.parentBlockList = this

        lastHoveredBlock = null

        drawer.populate()

        addAction(blockElement.asDynamic().block.action, pos)

        //console.log("adding ${blockElement.asDynamic().block.action.name} to ${element.classList}")
    }

    /**
     * Adds droppable properties to the header. Necessary to determine block drop positions
     * @param header The header element
     */
    fun addHeaderDroppable(header : HTMLElement){
        val onOver = {
            var blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
            blocks = trimToDirectChildren(blocks.toMutableList())
            try{
                (blocks[0] as HTMLElement).style.marginTop = "10px"
            }
            catch (e : Exception){}

            firstIndexDrop = true
        }

        val onOverOut = {
            var blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
            blocks = trimToDirectChildren(blocks.toMutableList())
            try{
                (blocks[0] as HTMLElement).style.marginTop = ""
            }
            catch (e : Exception){}
            firstIndexDrop = false
        }

        val onDrop = {
            var blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
            blocks = trimToDirectChildren(blocks.toMutableList())
            try{
                (blocks[0] as HTMLElement).style.marginTop = ""
                (blocks[1] as HTMLElement).style.marginTop = ""
            }
            catch (e : Exception){}
        }

        val drop = jQuery(header).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "drop", onDrop)
        drop.droppable("option", "dropOver", onOver)
        drop.droppable("option", "out", onOverOut)
    }

    /**
     * Adds an action at a position to the list backend
     * @param action The action to add
     * @param pos The position to add it at
     */
    fun addAction(action : Action<*>, pos : Int)

    /**
     * Removes an action from the list backend
     * @param action The action to remove
     */
    fun removeAction(action : Action<*>)

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

    /**
     * Called when a draggable is hovered over this list
     * @param event The over event
     * @param ui The element being hovered
     */
    fun dropOver(event : Event, ui : dynamic){
        element.style.boxShadow = "0px 0px 2px grey"
        val blockElement : HTMLElement = ui.draggable[0]
        removeAction(blockElement.asDynamic().block.action)
    }

    /**
     * Called when a draggable that was hovering over this list is moved out
     * @param event The out event
     * @param ui The element being moved
     */
    fun dropOverOut(event : Event, ui : dynamic){
        element.style.boxShadow = ""
        lastHoveredBlock = null
    }

    /**
     * Enables or disables dropping into this list
     * @param status True to enable dropping into, false otherwise
     */
    fun setDropInto(status : Boolean)
}
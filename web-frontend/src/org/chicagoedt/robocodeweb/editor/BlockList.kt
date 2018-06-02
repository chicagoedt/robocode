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
 * @property header The header that, when dropped into, the element will be added as the first index of the list
 * @property lastHoveredBlock The last block to be hovered over
 * @property element The element to insert the blocks into
 * @property dropElement The element that, when dropped into, elements are added to this list
 * @property drawer The drawer to repopulate after a drop
 * @property firstIndexDrop True if the block should be placed at the first index in the list, false otherwise
 * @property acceptMacros True if this list can have macros added to it, false otherwise
 * @property parentList The BlockList that this BlockList belongs to
 */
interface BlockList {
    var header : HTMLElement
    var lastHoveredBlock : ActionBlock<*>?
    var element : HTMLElement
    var dropElement : HTMLElement
    var drawer : Drawer
    var firstIndexDrop : Boolean
    var acceptMacros : Boolean
    var parentList : BlockList?

    /**
     * Adds the necessary options for this list to be a droppable
     */
    fun addDrop(){
        val drop = jQuery(dropElement).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "greedy", true)
        drop.droppable("option", "drop", ::dropInList)
        drop.droppable("option", "over", ::dropOver)
        drop.droppable("option", "out", ::dropOverOut)
        if (acceptMacros) drop.droppable("option", "accept", ".actionBlock")
        else drop.droppable("option", "accept", ".nonMacroActionBlock")

        addHeaderDroppable()
    }

    /**
     * Called when a draggable is dropped over this list
     * @param event The drop event
     * @param ui The element being dropped
     */
    fun dropInList(event : Event, ui : dynamic){
        val blockElement : HTMLElement = ui.draggable[0]

        blockElement.style.top = "0px"
        blockElement.style.left = "0px"

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

        if (blockElement.asDynamic().block is ActionBlockMacro<*>){
            (blockElement.asDynamic().block).addDrop()
        }

        val isDroppable = jQuery(blockElement).data("uiDroppable") != undefined

        if (isDroppable){
            if (acceptMacros) jQuery(blockElement).asDynamic().droppable("option", "accept", ".actionBlock")
            else jQuery(blockElement).asDynamic().droppable("option", "accept", ".nonMacroActionBlock")
        }

        if (blockElement.asDynamic().block is BlockList) (blockElement.asDynamic().block as BlockList).parentList = this

        (blockElement.asDynamic().block as ActionBlock<*>).parentBlockList = this

        lastHoveredBlock = null

        drawer.populate()

        addAction(blockElement.asDynamic().block.action, pos)

        showOverOutToAllParents(this)
    }

    /**
     * Adds droppable properties to the header. Necessary to determine block drop positions
     * @param header The header element
     */
    fun addHeaderDroppable(){
        val onOver = {
            var blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
            blocks = trimToDirectChildren(blocks.toMutableList())
            try{
                (blocks[0] as HTMLElement).style.marginTop = "20px"
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

        val onDrop = { event : Event, ui : dynamic ->
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
        drop.droppable("option", "over", onOver)
        drop.droppable("option", "out", onOverOut)
        if (acceptMacros) drop.droppable("option", "accept", ".actionBlock")
        else drop.droppable("option", "accept", ".nonMacroActionBlock")
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
        val blockElement : HTMLElement = ui.draggable[0]
        removeAction(blockElement.asDynamic().block.action)
        showOver()
    }

    /**
     * Called when a draggable that was hovering over this list is moved out
     * @param event The out event
     * @param ui The element being moved
     */
    fun dropOverOut(event : Event, ui : dynamic){
        lastHoveredBlock = null
        showOverOut()
    }

    /**
     * To be run when a block is hovered over the drop area. It should show an indicator that the block can be dropped
     */
    fun showOver()

    /**
     * To be run when a block is hovered out of the drop area. It should remove the indicator that the block can be dropped
     */
    fun showOverOut()

    /**
     * Calls [showOverOut] on this list and all (direct and indirect) parents of this list
     */
    fun showOverOutToAllParents(list : BlockList){
        var currentList : BlockList? = list
        while (currentList != null){
            currentList.showOverOut()
            currentList = currentList.parentList
        }
    }

    /**
     * Calls [showOver] on this list and all (direct and indirect) parents of this list
     */
    fun showOverToAllParents(list : BlockList){
        var currentList : BlockList? = list
        while (currentList != null){
            currentList.showOver()
            currentList = currentList.parentList
        }
    }
}
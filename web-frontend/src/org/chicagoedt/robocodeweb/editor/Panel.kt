package org.chicagoedt.robocodeweb.editor

import jQuery
import org.chicagoedt.robocode.robots.RobotPlayer
import org.chicagoedt.robocode.actions.*
import org.w3c.dom.events.Event
import org.w3c.dom.*
import kotlin.browser.*
import kotlin.dom.addClass
import kotlin.js.*
import kotlin.*

/**
 * The area to show and manage the code corresponding to the robot
 * @param parent The parent element for this panel
 * @param robot The robot corresponding to this panel
 * @param drawer The drawer where the blocks are being dragged from
 * @property element The HTML element for this panel
 * @property lastHoveredBlock The last block that a draggable was hovered over
 * @property hoverOverHeader True if the header is being hovered over, false otherwise
 */
class Panel(val parent : HTMLElement, val robot : RobotPlayer, val drawer : Drawer){
    lateinit var element : HTMLDivElement
    var lastHoveredBlock : ActionBlock<Action<Any>>? = null
    private var hoverOverHeader = false

    init {
        val tdElement = document.createElement("td") as HTMLElement
        tdElement.addClass("panelTd")

        element = document.createElement("div") as HTMLDivElement
        element.addClass("panel")
        element.asDynamic().panelObject = this


        element.appendChild(getHeader())
        tdElement.appendChild(element)
        parent.appendChild(tdElement)

        addDrop()
    }

    /**
     * Adds the necessary options for this panel to be a droppable
     */
    fun addDrop(){
        val drop = jQuery(element).asDynamic()
        drop.droppable()
        drop.droppable("option", "tolerance", "pointer")
        drop.droppable("option", "scope", "actions")
        drop.droppable("option", "drop", ::drop)
        drop.droppable("option", "over", ::over)
        drop.droppable("option", "out", ::overout)
    }

    /**
     * Called when a draggable is dropped over this panel
     * @param event The drop event
     * @param ui The element being dropped
     */
    fun drop(event : Event, ui : dynamic){
        val blockElement : HTMLElement = ui.draggable[0]
        blockElement.style.top = "0px"
        blockElement.style.left = "0px"
        element.style.boxShadow = ""

        var blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
        blocks = trimToDirectChildren(blocks.toMutableList())
        var pos = 0
        if (hoverOverHeader){
            if (blocks.size > 0) element.insertBefore(blockElement, blocks[0])
            else element.appendChild(blockElement)
            hoverOverHeader = false
        }
        else{
            try{
                lastHoveredBlock!!.element.style.marginBottom = ""
                pos = blocks.indexOf(lastHoveredBlock!!.element) + 1
                val block = blocks[pos] as HTMLElement
                element.insertBefore(blockElement, block)
            }
            catch(e : Exception){
                pos = blocks.size
                element.appendChild(blockElement)
            }
        }

        lastHoveredBlock = null

        robot.insertAction(blockElement.asDynamic().block.action, pos)

        val newActionBlock = blockElement.asDynamic().block
        if (newActionBlock is ActionBlockMacro<*>){
            newActionBlock.panelParent = this
        }

        drawer.populate()
    }

    /**
     * Called when a draggable is hovered over this panel
     * @param event The over event
     * @param ui The element being hovered
     */
    fun over(event : Event, ui : dynamic){
        element.style.boxShadow = "0px 0px 2px grey"
        val blockElement : HTMLElement = ui.draggable[0]
        robot.removeAction(blockElement.asDynamic().block.action)
    }

    /**
     * Called when a draggable that was hovering over this panel is moved out
     * @param event The out event
     * @param ui The element being moved
     */
    fun overout(event : Event, ui : dynamic){
        element.style.boxShadow = ""
        lastHoveredBlock = null
    }

    /**
     * Generates the header for the panel
     * @return The HTMLElement for the header
     */
    private fun getHeader() : HTMLElement{
        val header = document.createElement("div") as HTMLElement
        header.addClass("panelHeader")

        header.innerHTML = robot.name

        addHeaderDroppable(header)

        val runButton = document.createElement("button") as HTMLElement
        runButton.addClass("panelHeaderButton")
        runButton.innerHTML = "Go"

        runButton.onclick = {
            var interval = 0
            robot.runInstructions(true,
                    {runner ->
                        interval = window.setInterval(runner, 500)
                    },
                    {
                        window.clearInterval(interval)
                    })
        }
        header.appendChild(runButton)

        return header
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
}
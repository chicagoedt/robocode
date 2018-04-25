package org.chicagoedt.rosetteweb.editor

import jQuery
import org.chicagoedt.rosette.robots.RobotPlayer
import org.chicagoedt.rosetteweb.*
import org.chicagoedt.rosette.actions.*
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
 */
class Panel(val parent : HTMLElement, val robot : RobotPlayer, val drawer : Drawer){
    lateinit var element : HTMLDivElement
    var lastHoveredBlock : ActionBlock<Action<Any>>? = null

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

        val blocks = (element.querySelectorAll(".actionBlock") as ItemArrayLike<Element>).asList<Element>()
        var pos = 0

        try{
            lastHoveredBlock!!.element.style.marginTop = ""
            pos = blocks.indexOf(lastHoveredBlock!!.element)
            val block = blocks[pos] as HTMLElement
            element.insertBefore(blockElement, block)
        }
        catch(e : Exception){
            pos = blocks.size
            element.appendChild(blockElement)
        }

        lastHoveredBlock = null
        
        robot.insertAction(blockElement.asDynamic().block.action, pos)

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
     * Gets the block that the new block should be when dropped in this position
     * @param x The x value of the mouse
     * @param y The y value of the mouse
     * @return The position that the new block should be inserted at
     */
    private fun whichBlockOver(x : Int, y : Int) : Int{
        val blocks = element.querySelectorAll(".actionBlock")
        var position = blocks.length
        
        for (i in 0 until blocks.length){
            val block = (blocks.item(i) as HTMLElement).getBoundingClientRect()
            if (y < block.top){
                position = i
                break
            }
        }

        return position;
    }
}
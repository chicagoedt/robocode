package org.chicagoedt.rosetteweb.editor

import jQuery
import org.chicagoedt.rosette.robots.RobotPlayer
import org.chicagoedt.rosetteweb.*
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.dom.addClass

/**
 * The area to show and manage the code corresponding to the robot
 * @param parent The parent element for this panel
 * @param robot The robot corresponding to this panel
 * @param drawer The drawer where the blocks are being dragged from
 * @property element The HTML element for this panel
 */
class Panel(val parent : HTMLElement, val robot : RobotPlayer, val drawer : Drawer){
    lateinit var element : HTMLDivElement

    init {
        val tdElement = document.createElement("td") as HTMLElement
        
        element = document.createElement("div") as HTMLDivElement
        element.addClass("panel")

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
        val block : HTMLElement = ui.draggable.context
        block.style.top = "0px"
        block.style.left = "0px"
        block.style.width = "100%"
    	element.appendChild(block)
        element.style.border = ""
        drawer.populate()
    }

    /**
     * Called when a draggable is hovered over this panel
     * @param event The over event
     * @param ui The element being hovered
     */
    fun over(event : Event, ui : dynamic){
        element.style.border = "thick solid #000000"
    }

    /**
     * Called when a draggable that was hovering over this panel is moved out
     * @param event The out event
     * @param ui The element being moved
     */
    fun overout(event : Event, ui : dynamic){
        element.style.border = ""
    }

    private fun getHeader() : HTMLElement{
        val header = document.createElement("div") as HTMLElement
        header.addClass("panelHeader")

        val name = document.createElement("p") as HTMLElement
        name.addClass("panelHeaderName")
        name.innerHTML = robot.name
        
        header.appendChild(name)

        return header
    }

}
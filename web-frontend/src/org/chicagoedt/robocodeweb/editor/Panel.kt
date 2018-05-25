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
class Panel(val parent : HTMLElement, val robot : RobotPlayer, override var drawer : Drawer) : BlockList{
    override var element : HTMLElement = document.createElement("div") as HTMLDivElement
    override var dropElement: HTMLElement = element
    override var lastHoveredBlock : ActionBlock<*>? = null
    override var firstIndexDrop = false

    init {
        val tdElement = document.createElement("td") as HTMLElement
        tdElement.addClass("panelTd")

        element.addClass("panel")
        element.asDynamic().container = this
        element.asDynamic().panelObject = this

        element.appendChild(getHeader())
        tdElement.appendChild(element)
        parent.appendChild(tdElement)

        addDrop()
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

    override fun addAction(action : Action<*>, pos : Int){
        robot.insertAction(action, pos)
    }

    override fun removeAction(action : Action<*>){
        robot.removeAction(action)
    }

    override fun setDropInto(status : Boolean){
        if (status){
            jQuery(element).asDynamic().droppable("enable")
        }
        else{
            jQuery(element).asDynamic().droppable("disable")
        }
    }
}
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
 */
class Panel(val parent : HTMLElement, val robot : RobotPlayer, override var drawer : Drawer) : BlockList{
    override var element : HTMLElement = document.createElement("div") as HTMLDivElement
    override var dropElement: HTMLElement = element
    override var lastHoveredBlock : ActionBlock<*>? = null
    override var firstIndexDrop = false
    override var acceptMacros = true
    override var header = document.createElement("div") as HTMLElement
    override var parentList: BlockList? = null

    private var originalHeight = ""
    private var originalMargin = ""

    init {
        val tdElement = document.createElement("td") as HTMLElement
        tdElement.addClass("panelTd")

        element.addClass("panel")
        element.asDynamic().container = this
        element.asDynamic().panelObject = this

        setHeader()

        element.appendChild(header)
        tdElement.appendChild(element)
        parent.appendChild(tdElement)

        addDrop()
    }

    /**
     * Generates the header for the panel
     * @return The HTMLElement for the header
     */
    private fun setHeader(){
        header.addClass("panelHeader")

        header.innerHTML = robot.name

        addHeaderDroppable()

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
    }

    override fun addAction(action : Action<*>, pos : Int){
        robot.insertAction(action, pos)
    }

    override fun removeAction(action : Action<*>){
        robot.removeAction(action)
    }

    override fun showOver() {
        originalHeight = jQuery(element).css("height")
        originalMargin = jQuery(element).css("margin")
        val margin = window.getComputedStyle(element).margin
        val marginVal = margin.substring(0, margin.length - 2).toDouble().toInt()
        val height = window.getComputedStyle(element).height
        val heightVal = height.substring(0, height.length - 2).toDouble().toInt()

        val animation : Any = {}
        animation.asDynamic().margin = "0px"
        animation.asDynamic().height = (heightVal + (marginVal * 2)).toString() + "px"
        jQuery(element).animate(animation, 100)
    }

    override fun showOverOut() {
        val animation : Any = {}
        animation.asDynamic().margin = originalMargin
        animation.asDynamic().height = originalHeight
        jQuery(element).animate(animation, 100)
    }
}
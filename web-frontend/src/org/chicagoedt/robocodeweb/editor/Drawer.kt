package org.chicagoedt.robocodeweb.editor

import jQuery
import JQueryEventObject
import org.chicagoedt.robocode.sensors.EmptySensor
import org.chicagoedt.robocodeweb.editor.actionblocks.*
import org.chicagoedt.robocodeweb.sensorconfig.SensorBlock
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList
import kotlin.browser.document

/**
 * The section of the screen where users can select blocks
 * @param parent The parent element for this drawer
 * @property element The HTML element for this drawer
 */
class Drawer(val parent : HTMLElement) {
    val element = document.getElementById("drawer") as HTMLElement
    val actionDeleteElement = document.getElementById("actionDelete") as HTMLElement
    val sensorDeleteElement = document.getElementById("sensorDelete") as HTMLElement

    init{
        element.asDynamic().drawer = this
    }

    /**
     * Removes and re-adds all blocks in the drawer
     */
    fun refresh(){
        val blockList = element.getElementsByClassName("actionBlock").asList()
        for (block in blockList){
            element.removeChild(block)
        }
        populate()
    }

    /**
     * Sets the droppable properties for this drawer
     */
    fun setDroppable() {
        var drag = jQuery(actionDeleteElement).asDynamic()
        drag.droppable()
        drag.droppable("option", "tolerance", "pointer")
        drag.droppable("option", "scope", "actions")
        drag.droppable("option", "drop", ::actionDrop)

        drag = jQuery(sensorDeleteElement).asDynamic()
        drag.droppable()
        drag.droppable("option", "tolerance", "pointer")
        drag.droppable("option", "scope", "sensors")
        drag.droppable("option", "drop", ::sensorDrop)
    }

    /**
     * Called when an action draggable is dropped over this drawer
     * @param event The Jquery event corresponding to the drop
     * @param ui The element being dropped
     */
    fun actionDrop(event: dynamic, ui: dynamic) {
        val uiElement = ui.draggable[0] as HTMLElement

        val actionBlock : ActionBlock<*> = uiElement.asDynamic().block

        if (uiElement.parentElement!!.classList.contains("panel")){
            (uiElement.parentElement!!.asDynamic().panelObject as Panel).robot.removeAction(actionBlock.action)
        }
        else if (actionBlock.macroParent != null){
            actionBlock.macroParent!!.action.removeFromMacro(actionBlock.action)
        }

        uiElement.parentElement!!.removeChild(uiElement)

        populate()
    }

    /**
     * Called when a sensor draggable is dropped over this drawer
     * @param event The Jquery event corresponding to the drop
     * @param ui The element being dropped
     */
    fun sensorDrop(event: JQueryEventObject, ui: dynamic) {
        val uiElement = ui.draggable[0] as HTMLElement
        val block : SensorBlock<*> = uiElement.asDynamic().block

        if (block.sensorPanel != null && uiElement.asDynamic().actionSensor == false){
            block.sensor.player!!.removeSensorFrom(block.sensor.sensorPosition!!, block.sensor)
            block.sensorPanel!!.updateRemaining()
            block.removeAllChildren()
        }
        else if (uiElement.asDynamic().actionSensor == true){
            val originalBlock = uiElement.asDynamic().block as SensorBlock<*>
            originalBlock.actionSensorChildren.remove(uiElement)

            val action = uiElement.asDynamic().action as ActionBlock<*>
            action.removeSensorParameter()

            val parameterElement : HTMLElement = uiElement.parentElement!! as HTMLElement
            val toolTip : HTMLElement = parameterElement.asDynamic().toolTip
            parameterElement.asDynamic().sensor = null
            parameterElement.onmouseover = {
                jQuery(toolTip).show()
            }
            parameterElement.onmouseout = {
                jQuery(toolTip).hide()
            }
        }

        uiElement.parentElement!!.removeChild(uiElement)

        block.drawer.populate()
    }

    /**
     * Checks to make sure the drawer population is updates
     */
    fun populate() {
        checkMoveActionBlock(0)
        checkTurnActionBlock(1)
        checkItemPickupActionBlock(2)
        checkItemDropActionBlock(3)
        checkForLoopActionBlockMacro(4)
        checkReadSensorActionBlock(5)
    }


    //there is a more elegant way to check these blocks, but kotlinJS doesn't support the necessary reflection methods yet
    fun checkMoveActionBlock(index: Int) {
        if (element.children.length <= index ||
                !(element.children.item(index).asDynamic().block is MoveActionBlock)) {
            val block = MoveActionBlock()
            block.addDraggable()

            try {
                element.insertBefore(block.element, element.children.item(index))
            } catch (e: Exception) {
                element.appendChild(block.element)
            }
        }
    }

    fun checkTurnActionBlock(index: Int) {
        if (element.children.length <= index ||
                !(element.children.item(index).asDynamic().block is TurnActionBlock)) {
            val block = TurnActionBlock()
            block.addDraggable()

            try {
                element.insertBefore(block.element, element.children.item(index))
            } catch (e: Exception) {
                element.appendChild(block.element)
            }
        }
    }

    fun checkItemPickupActionBlock(index: Int) {
        if (element.children.length <= index ||
                !(element.children.item(index).asDynamic().block is ItemPickupActionBlock)) {
            val block = ItemPickupActionBlock()
            block.addDraggable()

            try {
                element.insertBefore(block.element, element.children.item(index))
            } catch (e: Exception) {
                element.appendChild(block.element)
            }
        }
    }

    fun checkItemDropActionBlock(index: Int) {
        if (element.children.length <= index ||
                !(element.children.item(index).asDynamic().block is ItemDropActionBlock)) {
            val block = ItemDropActionBlock()
            block.addDraggable()

            try {
                element.insertBefore(block.element, element.children.item(index))
            } catch (e: Exception) {
                element.appendChild(block.element)
            }
        }
    }

    fun checkForLoopActionBlockMacro(index: Int) {
        if (element.children.length <= index ||
                !(element.children.item(index).asDynamic().block is ForLoopActionBlockMacro)) {
            val block = ForLoopActionBlockMacro(this)
            block.addDraggable()

            try {
                element.insertBefore(block.element, element.children.item(index))
            } catch (e: Exception) {
                element.appendChild(block.element)
            }
        }
    }

    fun checkReadSensorActionBlock(index: Int) {
        if (element.children.length <= index ||
                !(element.children.item(index).asDynamic().block is ReadSensorActionBlock)) {
            val block = ReadSensorActionBlock()
            block.addDraggable()

            try {
                element.insertBefore(block.element, element.children.item(index))
            } catch (e: Exception) {
                element.appendChild(block.element)
            }
        }
    }
}
package org.chicagoedt.robocodeweb.editor

import jQuery
import JQueryEventObject
import org.chicagoedt.robocodeweb.editor.actionblocks.*
import org.w3c.dom.HTMLElement
import kotlin.browser.document

/**
 * The section of the screen where users can select blocks
 * @param parent The parent element for this drawer
 * @property element The HTML element for this drawer
 */
class Drawer(val parent : HTMLElement) {
    val element = document.getElementById("drawer") as HTMLElement

    /**
     * Sets the droppable properties for this drawer
     */
    fun setDroppable() {
        val drag = jQuery(element).asDynamic()
        drag.droppable()
        drag.droppable("option", "tolerance", "pointer")
        drag.droppable("option", "drop", ::drop)
    }

    /**
     * Called when a draggable is dropped over this drawer
     * @param event The Jquery event corresponding to the drop
     * @param ui The element being dropped
     */
    fun drop(event: JQueryEventObject, ui: dynamic) {
        val uiElement = ui.draggable[0] as HTMLElement
        uiElement.parentElement!!.removeChild(uiElement)
        populate()
    }

    /**
     * Checks to make sure the drawer population is updates
     */
    fun populate() {
        checkMoveActionBlock(0)
        checkTurnActionBlock(1)
        checkItemPickupActionBlock(2)
        checkItemDropActionBlock(3)
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
}
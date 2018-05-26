package org.chicagoedt.robocodeweb.editor

import jQuery
import org.chicagoedt.robocode.actions.Action
import org.chicagoedt.robocode.actions.ActionMacro
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.addClass

/**
 * An action block containing other action blocks
 * @param drawer The drawer than this drawer is initialized into
 */
abstract class ActionBlockMacro<T : ActionMacro<*>>(override var drawer: Drawer) : ActionBlock<T>(), BlockList{
    override var header = document.createElement("div") as HTMLElement
    override var dropElement = document.createElement("div") as HTMLElement
    override var lastHoveredBlock: ActionBlock<*>? = null
    override var firstIndexDrop = false
    override var acceptMacros = false

    init{
        element.addClass("actionBlockMacro")
        dropElement.addClass("dropHelper")
        element.appendChild(dropElement)
        header.addClass("actionBlockMacroHeader")
        element.appendChild(header)
        element.classList.remove("nonMacroActionBlock")
    }

    override fun addAction(action: Action<*>, pos: Int) {
        this.action.addToMacro(action, pos)
    }

    override fun removeAction(action : Action<*>){
        this.action.removeFromMacro(action)
    }
}
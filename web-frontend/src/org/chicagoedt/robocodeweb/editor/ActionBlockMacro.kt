package org.chicagoedt.robocodeweb.editor

import org.chicagoedt.robocode.actions.Action
import kotlin.dom.addClass

abstract class ActionBlockMacro<T : Action<*>> : ActionBlock<T>(){
    init{
        element.addClass("actionBlockMacro")
    }
}
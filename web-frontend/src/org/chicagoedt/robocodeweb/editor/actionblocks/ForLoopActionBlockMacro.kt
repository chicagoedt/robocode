package org.chicagoedt.robocodeweb.editor.actionblocks

import org.chicagoedt.robocode.actions.robotActions.ForLoopAction
import org.chicagoedt.robocodeweb.editor.ActionBlockMacro
import org.chicagoedt.robocodeweb.editor.BlockParameterType
import org.chicagoedt.robocodeweb.editor.Drawer
import kotlin.dom.addClass

class ForLoopActionBlockMacro(drawer : Drawer) : ActionBlockMacro<ForLoopAction>(drawer) {
    override val action = ForLoopAction()

    init{
        parameterType = BlockParameterType.NUMBER_INPUT
        blockClass = "controlBlock"
        borderClass = "controlBlockBorder"
        addHeader()
    }
}
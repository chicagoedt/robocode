package org.chicagoedt.robocodeweb.editor.actionblocks

import org.chicagoedt.robocode.actions.robotActions.ForLoopAction
import org.chicagoedt.robocode.collectibles.ItemManager
import org.chicagoedt.robocodeweb.editor.ActionBlockMacro
import org.chicagoedt.robocodeweb.editor.BlockParameterType

class ForLoopActionBlock : ActionBlockMacro<ForLoopAction>() {
    override val action = ForLoopAction()

    init{
        parameterType = BlockParameterType.NUMBER_INPUT
        element.classList.add("controlBlock")
    }
}
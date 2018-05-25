package org.chicagoedt.robocodeweb.editor.actionblocks

import org.chicagoedt.robocode.actions.robotActions.ForLoopAction
import org.chicagoedt.robocode.collectibles.ItemManager
import org.chicagoedt.robocodeweb.editor.ActionBlockMacro
import org.chicagoedt.robocodeweb.editor.BlockParameterType
import org.chicagoedt.robocodeweb.editor.Drawer

/**
 * A block representing the ForLoopAction
 * @param drawer The drawer that this block is coming from
 */
class ForLoopActionBlock(override var drawer: Drawer) : ActionBlockMacro<ForLoopAction>(drawer) {
    override val action = ForLoopAction()

    init{
        parameterType = BlockParameterType.NUMBER_INPUT
        element.classList.add("controlBlock")
        addDrop()
    }
}
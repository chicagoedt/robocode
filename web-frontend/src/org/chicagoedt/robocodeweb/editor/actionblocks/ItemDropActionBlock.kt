package org.chicagoedt.robocodeweb.editor.actionblocks

import org.chicagoedt.robocode.actions.robotActions.ItemDropAction
import org.chicagoedt.robocode.collectibles.ItemManager
import org.chicagoedt.robocodeweb.editor.ActionBlock
import org.chicagoedt.robocodeweb.editor.BlockParameterType

class ItemDropActionBlock : ActionBlock<ItemDropAction>() {
    override val action =  ItemDropAction()

    init{
        parameterType = BlockParameterType.DROPDOWN
        element.classList.add("itemsBlock")
        for (item in ItemManager.getAllItems()){
            insertDropdownParameter(item.name, item.id)
        }
    }
}
package org.chicagoedt.robocodeweb.editor.actionblocks

import org.chicagoedt.robocode.actions.robotActions.ItemDropAction
import org.chicagoedt.robocode.actions.robotActions.ItemPickupAction
import org.chicagoedt.robocode.collectibles.ItemManager
import org.chicagoedt.robocode.collectibles.etc.Sand
import org.chicagoedt.robocodeweb.editor.ActionBlock

class ItemDropActionBlock : ActionBlock<ItemDropAction>() {
    override val action =  ItemDropAction()
    override val hasParameters = true

    init{
        element.classList.add("itemsBlock")
        for (item in ItemManager.getAllItems()){
            insertParameter(item.name, item.id)
        }
    }
}
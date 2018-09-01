package org.chicagoedt.robocodeweb.editor.actionblocks

import org.chicagoedt.robocode.actions.robotActions.ItemPickupAction
import org.chicagoedt.robocode.collectibles.ItemManager
import org.chicagoedt.robocodeweb.editor.ActionBlock
import org.chicagoedt.robocodeweb.editor.BlockParameterType
import kotlin.dom.addClass

class ItemPickupActionBlock : ActionBlock<ItemPickupAction>() {
    override val action =  ItemPickupAction()

    init{
        parameterType = BlockParameterType.DROPDOWN
        blockClass = "itemsBlock"
        element.addClass("itemPickupActionBlock")
        for (item in ItemManager.getAllItems()){
            insertDropdownParameter(item.name, item.id, item.graphic)
        }
    }
}
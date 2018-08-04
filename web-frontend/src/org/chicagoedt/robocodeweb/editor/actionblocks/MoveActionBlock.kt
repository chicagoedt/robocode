package org.chicagoedt.robocodeweb.editor.actionblocks

import jQuery
import org.chicagoedt.robocode.actions.robotActions.*
import org.chicagoedt.robocodeweb.currentLevelConditions
import org.chicagoedt.robocodeweb.editor.*
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement

/**
 * The ActionBlock representing the MoveAction
 */
class MoveActionBlock() : ActionBlock<MoveActionMacro>(){
	override val action = MoveActionMacro()

	init{
		parameterType = BlockParameterType.NUMBER_INPUT
		blockClass = "movementBlock"

		if (currentLevelConditions.topicOnlyForMove) enableTopicOnly()
	}

	fun enableTopicOnly(){
		val topicSelector = parameterElement.querySelector(".topicSelectorInput") as HTMLElement

		parameterElement.onmouseover!!.invoke(undefined.asDynamic())
		topicSelector.onclick!!.invoke(undefined.asDynamic())

		topicSelector.onclick = {}

		topicSelector.style.cursor = "default"
	}
}

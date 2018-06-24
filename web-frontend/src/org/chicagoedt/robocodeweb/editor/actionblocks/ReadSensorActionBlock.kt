package org.chicagoedt.robocodeweb.editor.actionblocks

import org.chicagoedt.robocode.actions.robotActions.ReadSensorAction
import org.chicagoedt.robocode.mainTopic
import org.chicagoedt.robocodeweb.editor.ActionBlock
import org.chicagoedt.robocodeweb.editor.BlockParameterType
import org.chicagoedt.robocodeweb.game

/**
 * The ActionBlock representing the ReadSensorAction
 */
class ReadSensorActionBlock : ActionBlock<ReadSensorAction>() {
    override val action = ReadSensorAction(mainTopic)

    init{
        parameterType = BlockParameterType.SENSOR
        blockClass = "sensorActionBlock"
    }
}
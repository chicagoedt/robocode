package org.chicagoedt.robocodeweb.sensorconfig

import org.chicagoedt.robocode.robots.RobotPosition
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.dom.addClass


class SensorPanel (val position : RobotPosition){
    val element = document.createElement("div") as HTMLElement

    init{
        element.addClass("sensorList")

        when(position){
            RobotPosition.FRONT -> element.addClass("frontSensorList")
            RobotPosition.BACK -> element.addClass("backSensorList")
            RobotPosition.LEFT -> element.addClass("leftSensorList")
            RobotPosition.RIGHT -> element.addClass("rightSensorList")
        }

        if (position == RobotPosition.LEFT || position == RobotPosition.RIGHT){
            element.addClass("sideSensorList")
        }
    }
}
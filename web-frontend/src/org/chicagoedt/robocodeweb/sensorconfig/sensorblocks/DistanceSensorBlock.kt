package org.chicagoedt.robocodeweb.sensorconfig.sensorblocks

import org.chicagoedt.robocode.sensors.DistanceSensor
import org.chicagoedt.robocodeweb.sensorconfig.SensorBlock
import org.chicagoedt.robocodeweb.sensorconfig.SensorDrawer

class DistanceSensorBlock(sensorNum : Int, drawer : SensorDrawer) : SensorBlock<DistanceSensor>(sensorNum, drawer) {
    override val sensor = DistanceSensor()

    init{
        blockClass = "distanceSensorBlock"
    }
}
package org.chicagoedt.robocodeweb.sensorconfig.sensorblocks

import org.chicagoedt.robocode.sensors.DistanceSensor
import org.chicagoedt.robocodeweb.sensorconfig.SensorBlock

class DistanceSensorBlock(sensorNum : Int) : SensorBlock<DistanceSensor>(sensorNum) {
    override val sensor = DistanceSensor()

    init{
        blockClass = "distanceSensorBlock"
    }
}
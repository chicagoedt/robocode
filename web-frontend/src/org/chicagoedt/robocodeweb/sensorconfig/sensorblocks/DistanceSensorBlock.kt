package org.chicagoedt.robocodeweb.sensorconfig.sensorblocks

import org.chicagoedt.robocode.sensors.DistanceSensor
import org.chicagoedt.robocodeweb.sensorconfig.SensorBlock

class DistanceSensorBlock : SensorBlock<DistanceSensor>() {
    override val sensor = DistanceSensor()

    init{
        blockClass = "distanceSensorBlock"
    }
}
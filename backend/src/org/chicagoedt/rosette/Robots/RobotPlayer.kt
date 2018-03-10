package org.chicagoedt.rosette.Robots

import org.chicagoedt.rosette.Instructions.Instruction
import org.chicagoedt.rosette.Sensors.*

enum class RobotPosition{
    FRONT,
    LEFT,
    RIGHT,
    BACK
}

class RobotPlayer(val name: String,
                  var x: Int,
                  var y: Int,
                  var direction: RobotOrientation){

    internal val instructions = arrayListOf<Instruction>()
    private val sensors = hashMapOf<RobotPosition, MutableList<Sensor>>()
    private val sensorCounts = hashMapOf<RobotPosition, Int>()

    init{
        setSensorCountAt(RobotPosition.FRONT, 1)
        setSensorCountAt(RobotPosition.BACK, 1)
        setSensorCountAt(RobotPosition.LEFT, 1)
        setSensorCountAt(RobotPosition.RIGHT, 1)
    }

    fun sensorCountAt(pos: RobotPosition) : Int{
        return sensorCounts[pos]!!
    }

    fun setSensorCountAt(pos: RobotPosition, count : Int){
        if (sensorCounts[pos] == null){
            sensorCounts[pos] = count
            sensors[pos] = mutableListOf()
        }
        else {
            val currentCount = sensorCounts[pos]!!
            sensorCounts[pos] = count
            val actualSize = sensors[pos]!!.size

            if (count < currentCount && count < actualSize) {
                for (i in count..actualSize-1) {
                    sensors[pos]!!.removeAt(i)
                }
            }
        }
    }

    fun addSensorTo(pos: RobotPosition, sensor: Sensor): Boolean{
        if (sensors[pos]!!.size + 1 <= sensorCounts[pos]!!){
            sensors[pos]!!.add(sensor)
            return true
        }
        return false
    }

    fun removeSensorFrom(pos: RobotPosition, sensor: Sensor){
        sensors[pos]!!.remove(sensor)
    }

    fun removeSensorFrom(pos: RobotPosition, index: Int){
        sensors[pos]!!.removeAt(index)
    }

    fun getSensors(pos: RobotPosition) : ArrayList<Sensor>{
        val list = arrayListOf<Sensor>()
        list.addAll(sensors[pos]!!)
        if (list.size < sensorCountAt(pos)){
            for (i in list.size..sensorCountAt(pos)-1){
                list.add(EmptySensor())
            }
        }
        return list
    }
}
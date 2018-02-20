package org.chicagoedt.rosette

class Level(var properties: LevelProperties, val players: HashMap<String, RobotPlayer>, val playerOrder: ArrayList<String>) {
    internal val grid = arrayListOf<ArrayList<Tile>>()//ArrayList<ArrayList<Tile>>()
    init{
        for (i in 0..properties.height - 1){
            val list = ArrayList<Tile>()
            for (j in 0..properties.height - 1){
                list.add(Tile(TYPE_NEUTRAL))
            }
            grid.add(list)
        }
    }
}

class RobotPlayer(val name: String,
                  var x: Int,
                  var y: Int,
                  var direction: RobotOrientation){
    val instructions = arrayListOf<Instruction>()
}
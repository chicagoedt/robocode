package org.chicagoedt.rosette

class Level(var properties: LevelProperties, val players: HashMap<String, RobotPlayer>, val playerOrder: ArrayList<String>) {
    var grid = arrayListOf<ArrayList<Tile>>()//ArrayList<ArrayList<Tile>>()
    init{
        for (i in 0..properties.height - 1){
            val list = ArrayList<Tile>()
            for (j in 0..properties.width - 1){
                list.add(Tile(TileType.NEUTRAL))
            }
            grid.add(list)
        }
    }

    fun makeGrid(newGrid: ArrayList<ArrayList<TileType>>){
        grid = arrayListOf<ArrayList<Tile>>()
        for (i in 0..properties.height - 1){
            val list = ArrayList<Tile>()
            for (j in 0..properties.width - 1){
                list.add(Tile(newGrid[i][properties.width - 1 - j]))
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
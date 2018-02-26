package org.chicagoedt.rosette

class Level(var properties: LevelProperties, val players: HashMap<String, RobotPlayer>, val playerOrder: ArrayList<String>) {
    private var grid = arrayListOf<ArrayList<Tile>>()//ArrayList<ArrayList<Tile>>()
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
                list.add(Tile(newGrid[properties.height - 1 - i][j]))
            }
            grid.add(list)
        }
    }

    fun tileAt(x: Int, y: Int): Tile{
        return grid[y][x]
    }
}

class RobotPlayer(val name: String,
                  var x: Int,
                  var y: Int,
                  var direction: RobotOrientation){
    val instructions = arrayListOf<Instruction>()
}
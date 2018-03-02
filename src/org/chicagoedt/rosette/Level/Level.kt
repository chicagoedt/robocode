package org.chicagoedt.rosette.Level

import org.chicagoedt.rosette.Instructions.Instruction
import org.chicagoedt.rosette.Robots.RobotOrientation
import org.chicagoedt.rosette.Tiles.Tile
import org.chicagoedt.rosette.Tiles.TileType
import org.chicagoedt.rosette.Robots.RobotPlayer

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

    fun tileAt(x: Int, y: Int): Tile {
        return grid[y][x]
    }
}
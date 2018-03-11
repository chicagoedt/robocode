package org.chicagoedt.rosette.Levels

import org.chicagoedt.rosette.Tiles.Tile
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Tiles.NeutralTile

//class Level(var properties: LevelProperties, val players: HashMap<String, RobotPlayer>, val playerOrder: ArrayList<String>) {
class Level(var properties: LevelProperties, val playersList: ArrayList<RobotPlayer>) {
    private var grid = arrayListOf<ArrayList<Tile>>()//ArrayList<ArrayList<Tile>>()
    internal val players: HashMap<String, RobotPlayer> = hashMapOf()
    internal val playerOrder: ArrayList<String> = arrayListOf()

    init{
        for (i in 0..properties.height - 1){
            val list = ArrayList<Tile>()
            for (j in 0..properties.width - 1){
                list.add(NeutralTile())
            }
            grid.add(list)
        }

        for (player in playersList){
            playerOrder.add(player.name)
            players[player.name] = player
        }
    }

    internal fun makeGrid(newGrid: ArrayList<ArrayList<out Tile>>){
        grid = arrayListOf<ArrayList<Tile>>()
        for (i in 0..properties.height - 1){
            val list = ArrayList<Tile>()
            for (j in 0..properties.width - 1){
                list.add(newGrid[properties.height - 1 - i][j])
            }
            grid.add(list)
        }
    }

    fun tileAt(x: Int, y: Int): Tile {
        return grid[y][x]
    }
}
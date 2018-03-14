package org.chicagoedt.rosette.Levels

import org.chicagoedt.rosette.Event
import org.chicagoedt.rosette.Instructions.Instruction
import org.chicagoedt.rosette.Tiles.Tile
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Tiles.NeutralTile
import org.chicagoedt.rosette.Tiles.TileType
import org.chicagoedt.rosette.eventListener

class Level(var properties: LevelProperties, val playersList: ArrayList<RobotPlayer>) {
    private var grid = arrayListOf<ArrayList<Tile>>()
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

    fun attachInstruction(name: String, inst: Instruction<*>){
        players[name]!!.instructions.add(inst)
    }

    fun removeInstruction(name: String, inst: Instruction<*>){
        players[name]!!.instructions.remove(inst)
    }

    fun getInstructions(name: String) : List<Instruction<*>>{
        return players[name]!!.instructions
    }

    fun runInstructionsFor(name: String){
        val robot = players[name]!!
        for(inst: Instruction<*> in robot.instructions){
            inst.function(this, players[name]!!, inst.parameter!!)

            //check to see if the player won after the instruction
            if (tileAt(players[name]!!.x, players[name]!!.y).type == TileType.VICTORY){
                eventListener.invoke(Event.LEVEL_VICTORY)
                break
            }
        }
    }
}
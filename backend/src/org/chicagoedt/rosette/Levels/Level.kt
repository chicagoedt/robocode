package org.chicagoedt.rosette.Levels

import org.chicagoedt.rosette.Event
import org.chicagoedt.rosette.Instructions.Instruction
import org.chicagoedt.rosette.Tiles.Tile
import org.chicagoedt.rosette.Robots.RobotPlayer
import org.chicagoedt.rosette.Tiles.NeutralTile
import org.chicagoedt.rosette.Tiles.TileType
import org.chicagoedt.rosette.eventListener

/**
 * A single challenge for the user to complete
 * @param properties The properties object for the level
 * @param playersList A list of all robot players in the level. The names should correspond to names of robots passed to the Game object
 * @property grid A 2D-Arraylist of all tiles in the level
 * @property players All of the RobotPlayers in the level
 * @property playerOrder The order of the players in the level
 */
class Level(var properties: Properties, val playersList: ArrayList<RobotPlayer>) {
    /**
     * The properties to be stored in a Level
     * @param name The name of the level
     * @param difficulty The difficulty of the level. This is just for categorization, it will not affect the level at all.
     * @param width The width of the grid on the level
     * @param height The height of the grid on the level
     */
    data class Properties(val name: String,
                          val difficulty : Int,
                          val width : Int,
                          val height : Int)

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

    /**
     * Creates a grid with 0,0 in the bottom left corner
     *
     * This method exists because it's easier to create a 2D array visually with 0,0 in the top left
     * @param newGrid A grid with 0,0 in the top left corner
     */
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

    /**
     * @param x The X value of the requested point
     * @param y the Y value of the requested point
     * @return The tile at a specific point
     */
    fun tileAt(x: Int, y: Int): Tile {
        return grid[y][x]
    }

    /**
     * Attaches an instruction to a robot
     * @param name The name of the robot to attach the instruction to
     * @param inst The instruction to attach
     */
    fun attachInstruction(name: String, inst: Instruction<*>){
        players[name]!!.instructions.add(inst as Instruction<Any>)
    }

    /**
     * Removes an instruction from a robot
     * @param name The name of the robot to remove the instruction from
     * @param inst The instruction to remove from the robot
     */
    fun removeInstruction(name: String, inst: Instruction<*>){
        players[name]!!.instructions.remove(inst)
    }

    /**
     * @param name The name of the robot to retrieve the instructions for
     * @return A list of instructions on the robot
     */
    fun getInstructions(name: String) : List<Instruction<*>>{
        return players[name]!!.instructions
    }

    /**
     * Executes all of the instructions attached to a robot
     * @param name The name of the robot to run instructions for
     */
    fun runInstructionsFor(name: String){
        val robot = players[name]!!
        for(inst: Instruction<Any> in robot.instructions){
            inst.function(this, players[name]!!, inst.parameter)

            //check to see if the player won after the instruction
            if (tileAt(players[name]!!.x, players[name]!!.y).type == TileType.VICTORY){
                eventListener.invoke(Event.LEVEL_VICTORY)
                break
            }
        }
    }
}
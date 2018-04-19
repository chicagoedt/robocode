package org.chicagoedt.rosetteweb

import kotlin.browser.*
import org.chicagoedt.rosette.*
import org.chicagoedt.rosette.tiles.*
import org.w3c.dom.HTMLElement
import org.chicagoedt.rosetteweb.grid.*

/**
 * The driver to run a Grid canvas for the current game
 * @param game The game that the program is running
 */

class GridDriver(val game: Game){
    val tableBody = document.getElementById("grid")!!.children.item(0)!! as HTMLElement
    var level = game.currentLevel
    var gridTiles = arrayListOf<ArrayList<GridTile>>()

    /**
     * Calculates all of the necessary information when switching levels
     */
    fun calculateNewLevel(){
        level = game.currentLevel
        gridTiles.clear()

        for (y in 0 until level.properties.height){
            val row = arrayListOf<GridTile>()
            val tableRow = document.createElement("tr") as HTMLElement

            for (x in 0 until level.properties.width){
                val tile = GridTile(level, x, y)
                row.add(tile)
                tableRow.appendChild(tile.tableElement)
            }
            gridTiles.add(row)
            tableBody.appendChild(tableRow)
        }

        calculatePlayers()
    }

    fun calculatePlayers(){
        for ((name, player) in level.players){
            val x = player.x
            val y = player.y

            gridTiles[y][x].player = player
            gridTiles[y][x].refresh()
        }
    }

    /**
     * Refreshes the grid display
     */
    fun refresh(){
        for (row in gridTiles){
            for (tile in row){
                tile.player = null
                tile.refresh()
            }
        }

        calculatePlayers()
    }
}
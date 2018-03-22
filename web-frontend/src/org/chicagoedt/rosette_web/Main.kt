package org.chicagoedt.rosette_web

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*

/**
 * The context for the grid canvas
 */
private lateinit var gridContext: CanvasRenderingContext2D

/**
 * The context for the editor canvas
 */
private lateinit var editorContext: CanvasRenderingContext2D

/**
 * The game that the browser is running
 */
internal val game = Game(getLevels(), getRobots())

/**
 * The driver for the grid canvas
 */
private lateinit var gridDriver : GridDriver

/**
 * The driver for the editor canvas
 */
private lateinit var editorDriver : EditorDriver

/**
 * The main function to run when the page loads
 * @param args The arguments to run. Not currently used at all.
 */
fun main(args: Array<String>) {
    window.onload = {
        val gridCanvas = document.createElement("canvas") as HTMLCanvasElement
        gridContext = gridCanvas.getContext("2d") as CanvasRenderingContext2D
        gridContext.canvas.width = (document.documentElement!!.clientWidth.toDouble() / 3.0).toInt()
        gridContext.canvas.style.position = "absolute"
        gridDriver = GridDriver(game, gridContext)
        
        val editorCanvas = document.createElement("canvas") as HTMLCanvasElement
        editorContext = editorCanvas.getContext("2d") as CanvasRenderingContext2D
        editorContext.canvas.height = document.documentElement!!.clientHeight
        editorContext.canvas.style.position = "absolute"
        editorDriver = EditorDriver(game, editorContext)

        gridDriver.calculateNewLevel()
        editorDriver.calculateNewLevel()
        positionCanvases()

        document.body!!.appendChild(editorCanvas)
        document.body!!.appendChild(gridCanvas)

        
        draw()
    }

    window.onresize = {
        if (::gridContext.isInitialized && ::editorContext.isInitialized) {
            positionCanvases()
            draw()
        }
    }
}

/**
 * Defines the position for the canvases on the screen
 */
 fun positionCanvases(){
    gridContext.canvas.width = (document.documentElement!!.clientWidth.toDouble() / 3.0).toInt()
    gridContext.canvas.height = gridContext.canvas.width

    editorContext.canvas.width = (2.0 * document.documentElement!!.clientWidth.toDouble() / 3.0).toInt()
    editorContext.canvas.style.left = gridContext.canvas.width.toString() + "px"
    gridDriver.calculateTiles()
    gridDriver.calculatePlayers()
    editorDriver.calculatePanels()
 }

/**
 * Draws the view on the screen
 */
fun draw(){
    gridDriver.drawGrid()
    editorDriver.drawEditor()
}
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
 * The main function to run when the page loads
 * @param args The arguments to run. Not currently used at all.
 */
fun main(args: Array<String>) {
    window.onload = {
        val gridCanvas = document.createElement("canvas") as HTMLCanvasElement
        gridContext = gridCanvas.getContext("2d") as CanvasRenderingContext2D
        document.body!!.appendChild(gridCanvas)
        gridContext.canvas.style.width = "33%"
        gridContext.canvas.style.position = "absolute"
        gridDriver = GridDriver(game, gridContext)
        
        val editorCanvas = document.createElement("canvas") as HTMLCanvasElement
        editorContext = editorCanvas.getContext("2d") as CanvasRenderingContext2D
        document.body!!.appendChild(editorCanvas)
        editorContext.canvas.style.height = "100%"
        editorContext.canvas.style.position = "absolute"

        gridDriver.calculateNewLevel()
        positionCanvases()
        
        draw()
    }

    window.onresize = {
        if (::gridContext.isInitialized) {
            positionCanvases()
            draw()
        }
    }
}
/**
 * Defines the position for the canvases on the screen
 */
 fun positionCanvases(){
    gridContext.canvas.height = gridContext.canvas.width

    editorContext.canvas.style.width = "67%"
    editorContext.canvas.style.left = gridContext.canvas.style.width
    gridDriver.calculateTiles()
    gridDriver.calculatePlayers()
 }

/**
 * Draws the view on the screen
 */
fun draw(){
    gridDriver.drawGrid()
}
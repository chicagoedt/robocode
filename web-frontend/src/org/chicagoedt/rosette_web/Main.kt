package org.chicagoedt.rosette_web

import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.*
import org.chicagoedt.rosette.*

private lateinit var gridContext: CanvasRenderingContext2D
private lateinit var editorContext: CanvasRenderingContext2D

internal val game = Game(getLevels(), getRobots())

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
        gridContext.canvas.style.width = "400px"
        gridContext.canvas.style.height = "50%"
        gridContext.canvas.style.position = "absolute"
        gridDriver = GridDriver(game, gridContext)

        val editorCanvas = document.createElement("canvas") as HTMLCanvasElement
        editorContext = editorCanvas.getContext("2d") as CanvasRenderingContext2D
        document.body!!.appendChild(editorCanvas)
        editorContext.canvas.style.height = "100%"
        editorContext.canvas.style.position = "absolute"

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
    editorContext.canvas.style.width = (window.innerWidth - 400).toString() + "px"
    editorContext.canvas.style.left = gridContext.canvas.style.width
 }

/**
 * Draws the view on the screen
 */
fun draw(){
    gridDriver.drawGrid()
    // gridContext.fillStyle = "blue"
    // gridContext.fillRect(0.0, 0.0, gridContext.canvas.width.toDouble(), gridContext.canvas.height.toDouble())

    // editorContext.fillStyle = "black"
    // editorContext.fillRect(0.0, 0.0, editorContext.canvas.width.toDouble(), editorContext.canvas.height.toDouble())
}